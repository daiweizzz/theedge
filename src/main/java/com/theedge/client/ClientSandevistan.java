package com.theedge.client;

import com.theedge.entity.AfterimageEntity;
import com.theedge.init.ModEntities;
import com.theedge.network.SandevistanSyncPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public final class ClientSandevistan {
    private ClientSandevistan() {
    }

    public static final int SANDY_TINT = 0x2442FF78;

    private static boolean active = false;
    private static boolean overload = false;
    private static double energy = 10.0;
    private static double capacity = 10.0;
    private static int afterimageTimer = 0;

    public static void onSync(SandevistanSyncPayload payload) {
        active = payload.active();
        overload = payload.overload();
        energy = payload.energy();
        capacity = payload.capacity();
    }

    public static boolean isActive() {
        return active;
    }

    public static void spawnAfterimages() {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.level == null || !active) {
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
            afterimage.setSkin(minecraft.player.getSkin().texture());
            minecraft.level.addFreshEntity(afterimage);
        }
    }

    public static void renderOverlay(GuiGraphics guiGraphics) {
        if (!active) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        guiGraphics.fill(0, 0, width, height, SANDY_TINT);
        renderHud(guiGraphics);
    }

    private static void renderHud(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        Component text;
        if (active) {
            text = Component.translatable("hud.theedge.active", (int) Math.ceil(energy));
        } else if (energy < capacity - 0.05) {
            text = Component.translatable("hud.theedge.recharge", (int) Math.ceil(energy));
        } else {
            return;
        }
        int width = minecraft.getWindow().getGuiScaledWidth();
        String message = text.getString();
        int x = (width - minecraft.font.width(message)) / 2;
        guiGraphics.drawString(minecraft.font, message, x, 24, 0xFFFFFFFF);
    }
}
