package com.theedge.network;

import com.theedge.TheEdgeMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ActivateSandevistanPayload(boolean overload) implements CustomPacketPayload {
    public static final Type<ActivateSandevistanPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TheEdgeMod.MODID, "activate_sandevistan"));

    public static final StreamCodec<FriendlyByteBuf, ActivateSandevistanPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL, ActivateSandevistanPayload::overload,
                    ActivateSandevistanPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
