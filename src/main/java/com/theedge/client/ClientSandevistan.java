package com.theedge.client;

import com.theedge.network.SandevistanSyncPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public final class ClientSandevistan {
    private ClientSandevistan() {
    }

    private static int activeTicks = 0;
    private static int maxActiveTicks = 0;
    private static int cooldownTicks = 0;

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

    public static void renderOverlay(GuiGraphics guiGraphics) {
        if (activeTicks <= 0) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();
        double pulse = 0.5 + 0.5 * Math.sin(System.currentTimeMillis() / 120.0);
        int alpha = (int) (24 + 22 * pulse);
        guiGraphics.fill(0, 0, width, height, (alpha << 24) | 0xFF7A00);
    }
}
