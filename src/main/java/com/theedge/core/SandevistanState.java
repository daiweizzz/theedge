package com.theedge.core;

import com.theedge.config.TheEdgeConfig;
import com.theedge.init.ModItems;
import com.theedge.network.SandevistanSyncPayload;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTickRateManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class SandevistanState {
    private SandevistanState() {
    }

    private static final Set<UUID> ACTIVE = new HashSet<>();
    private static final Set<UUID> OVERLOAD = new HashSet<>();
    private static final Map<UUID, Double> ENERGY = new HashMap<>();
    private static final Map<UUID, Long> LAST_UPDATE = new HashMap<>();

    public static volatile boolean slowing = false;
    public static volatile int divisor = 8;
    private static boolean ownsTickRate = false;

    public static boolean isActive(Player player) {
        return ACTIVE.contains(player.getUUID());
    }

    public static boolean isOverload(Player player) {
        return OVERLOAD.contains(player.getUUID());
    }

    public static double energy(Player player) {
        return ENERGY.getOrDefault(player.getUUID(), capacity());
    }

    public static double capacity() {
        return TheEdgeConfig.CAPACITY_SECONDS.get();
    }

    public static void toggle(ServerPlayer player) {
        if (!hasSandevistan(player)) {
            player.displayClientMessage(
                    Component.translatable("message.theedge.no_item").withStyle(ChatFormatting.RED), true);
            return;
        }
        if (ACTIVE.contains(player.getUUID())) {
            deactivate(player);
            return;
        }
        if (energy(player) <= 0.01) {
            player.displayClientMessage(
                    Component.translatable("message.theedge.empty").withStyle(ChatFormatting.RED), true);
            return;
        }
        ACTIVE.add(player.getUUID());
        LAST_UPDATE.put(player.getUUID(), System.nanoTime());
        player.level().playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.6F);
        player.displayClientMessage(
                Component.translatable("message.theedge.activated").withStyle(ChatFormatting.GOLD), true);
    }

    public static void toggleOverload(ServerPlayer player) {
        if (!ACTIVE.contains(player.getUUID())) {
            return;
        }
        if (OVERLOAD.contains(player.getUUID())) {
            OVERLOAD.remove(player.getUUID());
        } else {
            OVERLOAD.add(player.getUUID());
        }
    }

    private static void deactivate(ServerPlayer player) {
        ACTIVE.remove(player.getUUID());
        OVERLOAD.remove(player.getUUID());
        player.displayClientMessage(
                Component.translatable("message.theedge.ended").withStyle(ChatFormatting.GRAY), true);
    }

    private static boolean hasSandevistan(ServerPlayer player) {
        return player.getInventory().contains(new ItemStack(ModItems.SANDEVISTAN.get()));
    }

    public static void onServerTick(MinecraftServer server) {
        long now = System.nanoTime();
        double capacity = capacity();
        double recharge = TheEdgeConfig.RECHARGE_PER_SECOND.get();

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            UUID id = player.getUUID();
            ENERGY.putIfAbsent(id, capacity);
            LAST_UPDATE.putIfAbsent(id, now);
        }

        Iterator<Map.Entry<UUID, Double>> iterator = new HashMap<>(ENERGY).entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Double> entry = iterator.next();
            UUID id = entry.getKey();
            ServerPlayer player = server.getPlayerList().getPlayer(id);
            if (player == null) {
                if (!ACTIVE.contains(id)) {
                    ENERGY.remove(id);
                    LAST_UPDATE.remove(id);
                }
                continue;
            }

            long last = LAST_UPDATE.getOrDefault(id, now);
            double elapsed = Math.max(0.0, (now - last) / 1_000_000_000.0);
            LAST_UPDATE.put(id, now);

            double value = entry.getValue();
            if (ACTIVE.contains(id)) {
                double drain = OVERLOAD.contains(id) ? TheEdgeConfig.OVERLOAD_DRAIN_MULTIPLIER.get() : 1.0;
                value -= elapsed * drain;
                if (value <= 0.0) {
                    value = 0.0;
                    deactivate(player);
                }
            } else {
                value = Math.min(capacity, value + elapsed * recharge);
            }
            ENERGY.put(id, value);
        }

        boolean anyActive = false;
        int maxDivisor = 1;
        Iterator<UUID> activeIt = new HashSet<>(ACTIVE).iterator();
        while (activeIt.hasNext()) {
            UUID id = activeIt.next();
            ServerPlayer player = server.getPlayerList().getPlayer(id);
            if (player == null) {
                ACTIVE.remove(id);
                OVERLOAD.remove(id);
                continue;
            }
            anyActive = true;
            int d = OVERLOAD.contains(id) ? TheEdgeConfig.OVERLOAD_DIVISOR.get() : TheEdgeConfig.NORMAL_DIVISOR.get();
            maxDivisor = Math.max(maxDivisor, d);
        }

        slowing = anyActive;
        divisor = Math.max(1, maxDivisor);

        ServerTickRateManager manager = server.tickRateManager();
        if (slowing) {
            float target = 20.0F / divisor;
            if (!ownsTickRate) {
                ownsTickRate = true;
                manager.setTickRate(target);
            } else if (Math.abs(manager.tickrate() - target) > 0.01F) {
                manager.setTickRate(target);
            }
        } else if (ownsTickRate) {
            ownsTickRate = false;
            manager.setTickRate(20.0F);
        }

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            PacketDistributor.sendToPlayer(player, new SandevistanSyncPayload(
                    ACTIVE.contains(player.getUUID()),
                    OVERLOAD.contains(player.getUUID()),
                    energy(player),
                    capacity));
        }
    }

    public static void onServerStopped() {
        ACTIVE.clear();
        OVERLOAD.clear();
        ENERGY.clear();
        LAST_UPDATE.clear();
        slowing = false;
        divisor = 8;
        ownsTickRate = false;
    }
}
