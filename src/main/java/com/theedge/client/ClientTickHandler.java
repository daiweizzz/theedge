package com.theedge.client;

import com.theedge.TheEdgeMod;
import com.theedge.network.ActivateSandevistanPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = TheEdgeMod.MODID, value = Dist.CLIENT)
public final class ClientTickHandler {
    private ClientTickHandler() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        ClientSandevistan.onClientTick();
        while (ClientModEvents.ACTIVATE_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new ActivateSandevistanPayload());
        }
    }
}
