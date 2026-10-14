package com.theedge.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.theedge.entity.AfterimageEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class AfterimageRenderer extends EntityRenderer<AfterimageEntity> {
    private static final ResourceLocation WHITE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("theedge", "textures/misc/white.png");

    private final PlayerModel<Player> model;

    public AfterimageRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false);
    }

    @Override
    public ResourceLocation getTextureLocation(AfterimageEntity entity) {
        return WHITE_TEXTURE;
    }

    @Override
    public void render(AfterimageEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        this.model.setAllVisible(true);
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            this.model.setupAnim(minecraft.player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        }

        float life = 1.0F - (float) entity.getAge() / (float) entity.getLifetime();
        int alpha = (int) (170 * life);
        int color = (alpha << 24) | (ClientSandevistan.SANDY_TINT & 0x00FFFFFF);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(WHITE_TEXTURE));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
