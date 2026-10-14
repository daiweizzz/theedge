package com.theedge;

import com.theedge.config.TheEdgeConfig;
import com.theedge.core.ServerEventHandler;
import com.theedge.init.ModItems;
import com.theedge.network.ModNetwork;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(TheEdgeMod.MODID)
public final class TheEdgeMod {
    public static final String MODID = "theedge";

    public TheEdgeMod(IEventBus modEventBus, ModContainer container) {
        ModItems.ITEMS.register(modEventBus);
        ModItems.CREATIVE_MODE_TABS.register(modEventBus);
        TheEdgeConfig.register(container);
        modEventBus.addListener(ModNetwork::register);
        NeoForge.EVENT_BUS.register(ServerEventHandler.class);
    }
}
