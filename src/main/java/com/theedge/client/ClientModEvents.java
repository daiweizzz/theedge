package com.theedge.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.theedge.TheEdgeMod;
import com.theedge.init.ModEntities;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = TheEdgeMod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ClientModEvents {
    private ClientModEvents() {
    }

    public static final KeyMapping ACTIVATE_KEY = new KeyMapping(
            "key.theedge.activate",
            InputConstants.KEY_R,
            "key.categories.theedge");

    public static final KeyMapping OVERLOAD_KEY = new KeyMapping(
            "key.theedge.overload",
            InputConstants.KEY_G,
            "key.categories.theedge");

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ACTIVATE_KEY);
        event.register(OVERLOAD_KEY);
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(TheEdgeMod.MODID, "sandevistan_overlay"),
                (guiGraphics, deltaTracker) -> ClientSandevistan.renderOverlay(guiGraphics));
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.AFTERIMAGE.get(), AfterimageRenderer::new);
    }
}
