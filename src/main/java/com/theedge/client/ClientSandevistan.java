package com.theedge.client;

import com.theedge.entity.AfterimageEntity;
import com.theedge.init.ModEntities;
import com.theedge.network.SandevistanSyncPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

public final class ClientSandevistan {
    private ClientSandevistan() {
    }

    public static final int SANDY_TINT = 0x00E5FF;

    private static int activeTicks = 0;
    private static int maxActiveTicks = 0;
    private static int cooldownTicks = 0;
    private static int afterimageTimer = 0;

    public static void onSync(SandevistanSyncPayload payload) {
        activeTicks = payload.activeTicks();
        maxActiveTicks = payload.activeTicks();
        cooldownTicks = payload.cooldownTicks();
    }

    public static void onClientTick() {
        if (activeTicks > 0) {
            activeTicks--;
        }
        if (cooldownTicks > 0) {
            cooldownTicks--;
        }
    }

    public static boolean isActive() {
        return activeTicks > 0;
    }

    public static void spawnAfterimages() {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.level == null || !isActive()) {
            return;
        }
        double horizontalSpeed = player.getDeltaMovement().horizontalDistance();
        if (horizontalSpeed < 0.15) {
            afterimageTimer = 0;
            return;
        }
        afterimageTimer++;
        if (afterimageTimer >= 2) {
            afterimageTimer = 0;
            AfterimageEntity afterimage = new AfterimageEntity(ModEntities.AFTERIMAGE.get(), minecraft.level);
            afterimage.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
            minecraft.level.addFreshEntity(afterimage);
        }
    }

    public static void renderOverlay(GuiGraphics guiGraphics) {
        if (activeTicks <= 0) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        double pulse = 0.5 + 0.5 * Math.sin(System.currentTimeMillis() / 120.0);
        int alpha = (int) (24 + 22 * pulse);
        guiGraphics.fill(0, 0, width, height, (alpha << 24) | SANDY_TINT);
    }
}
