package com.theedge.core;

import com.theedge.config.TheEdgeConfig;
import com.theedge.init.ModItems;
import com.theedge.network.SandevistanSyncPayload;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SandevistanState {
    private SandevistanState() {
    }

    private static final Map<UUID, Integer> REMAINING_TICKS = new ConcurrentHashMap<>();

    public static void tryActivate(ServerPlayer player) {
        if (player.getCooldowns().isOnCooldown(ModItems.SANDEVISTAN.get())) {
            player.displayClientMessage(
                    Component.translatable("message.theedge.cooldown").withStyle(ChatFormatting.RED), true);
            return;
        }
        if (!player.getInventory().contains(new ItemStack(ModItems.SANDEVISTAN.get()))) {
            player.displayClientMessage(
                    Component.translatable("message.theedge.no_item").withStyle(ChatFormatting.RED), true);
            return;
        }
        activate(player);
    }

    private static void activate(ServerPlayer player) {
        int duration = TheEdgeConfig.DURATION_TICKS.get();
        int cooldown = TheEdgeConfig.COOLDOWN_TICKS.get();

        REMAINING_TICKS.put(player.getUUID(), duration);
        player.getCooldowns().addCooldown(ModItems.SANDEVISTAN.get(), cooldown);

        int selfSpeed = TheEdgeConfig.SELF_SPEED_AMPLIFIER.get();
        if (selfSpeed >= 0) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, selfSpeed, false, false, true));
        }

        player.level().playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.6F);
        player.displayClientMessage(
                Component.translatable("message.theedge.activated").withStyle(ChatFormatting.GOLD), true);

        PacketDistributor.sendToPlayer(player, new SandevistanSyncPayload(duration, cooldown));
    }

    public static boolean isActive(Player player) {
        return REMAINING_TICKS.getOrDefault(player.getUUID(), 0) > 0;
    }

    public static void onServerTick(MinecraftServer server) {
        if (REMAINING_TICKS.isEmpty()) {
            return;
        }

        Set<UUID> active = new HashSet<>();
        Iterator<Map.Entry<UUID, Integer>> iterator = REMAINING_TICKS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                iterator.remove();
                ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
                if (player != null) {
                    player.displayClientMessage(
                            Component.translatable("message.theedge.ended").withStyle(ChatFormatting.GRAY), true);
                }
            } else {
                entry.setValue(remaining);
                active.add(entry.getKey());
            }
        }

        if (active.isEmpty()) {
            return;
        }

        int radius = TheEdgeConfig.SLOW_RADIUS.get();
        double motionSlowFactor = TheEdgeConfig.MOTION_SLOW_FACTOR.get();
        int slownessAmplifier = TheEdgeConfig.SLOWNESS_AMPLIFIER.get();

        for (ServerLevel level : server.getAllLevels()) {
            List<Vec3> activePositions = new ArrayList<>();
            for (UUID id : active) {
                ServerPlayer player = server.getPlayerList().getPlayer(id);
                if (player != null && player.level() == level) {
                    activePositions.add(player.position());
                }
            }
            if (activePositions.isEmpty()) {
                continue;
            }

            boolean global = radius <= 0;
            double radiusSq = (double) radius * radius;

            for (Entity entity : level.getAllEntities()) {
                if (entity instanceof ServerPlayer serverPlayer && active.contains(serverPlayer.getUUID())) {
                    continue;
                }
                if (!global) {
                    boolean nearActivePlayer = false;
                    for (Vec3 pos : activePositions) {
                        if (entity.distanceToSqr(pos) <= radiusSq) {
                            nearActivePlayer = true;
                            break;
                        }
                    }
                    if (!nearActivePlayer) {
                        continue;
                    }
                }
                applySlowdown(entity, motionSlowFactor, slownessAmplifier);
            }
        }
    }

    private static void applySlowdown(Entity entity, double motionSlowFactor, int slownessAmplifier) {
        if (entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 25, slownessAmplifier, false, false, false));
            living.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 25, 2, false, false, false));
        }
        if (motionSlowFactor < 1.0) {
            Vec3 motion = entity.getDeltaMovement();
            entity.setDeltaMovement(motion.x * motionSlowFactor, motion.y * motionSlowFactor, motion.z * motionSlowFactor);
        }
    }
}
