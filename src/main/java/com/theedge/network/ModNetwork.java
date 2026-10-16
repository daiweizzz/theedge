package com.theedge.network;

import com.theedge.client.ClientSandevistan;
import com.theedge.core.SandevistanState;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNetwork {
    private ModNetwork() {
    }

    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                ActivateSandevistanPayload.TYPE,
                ActivateSandevistanPayload.STREAM_CODEC,
                ModNetwork::handleActivate);
        registrar.playToClient(
                SandevistanSyncPayload.TYPE,
                SandevistanSyncPayload.STREAM_CODEC,
                ModNetwork::handleSync);
    }

    private static void handleActivate(final ActivateSandevistanPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (payload.overload()) {
                    SandevistanState.toggleOverload(player);
                } else {
                    SandevistanState.toggle(player);
                }
            }
        });
    }

    private static void handleSync(final SandevistanSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> ClientSandevistan.onSync(payload));
    }
}
