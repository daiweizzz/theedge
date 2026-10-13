package com.theedge.network;

import com.theedge.TheEdgeMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SandevistanSyncPayload(int activeTicks, int cooldownTicks) implements CustomPacketPayload {
    public static final Type<SandevistanSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(TheEdgeMod.MODID, "sandevistan_sync"));

    public static final StreamCodec<FriendlyByteBuf, SandevistanSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, SandevistanSyncPayload::activeTicks,
                    ByteBufCodecs.VAR_INT, SandevistanSyncPayload::cooldownTicks,
                    SandevistanSyncPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
