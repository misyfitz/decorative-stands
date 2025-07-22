package com.misyfitz.decorative_stands.content.entity.renderer;

import com.misyfitz.decorative_stands.client.model.DummyModel;
import com.misyfitz.decorative_stands.content.entity.DummyEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;


public class DummyEntityRenderer extends LivingEntityRenderer<DummyEntity, HumanoidModel<DummyEntity>> {
    @SuppressWarnings("removal")
	private static final ResourceLocation DUMMY_TEXTURE = new ResourceLocation("decorative_stands", "textures/entity/dummy.png");

    public DummyEntityRenderer(Context context) {
        super(context, new DummyModel<>(context.bakeLayer(DummyModel.DUMMY_LAYER)), 0f);

        this.addLayer(new HumanoidArmorLayer<>(
            this,
            new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
            new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
            context.getModelManager()
        ));

        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public void render(DummyEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // ( 0.0625 = 1 pixel)
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.1250D, 0.0D); // y = upward

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        poseStack.popPose();
    }
    
    @Override
    public ResourceLocation getTextureLocation(DummyEntity entity) {
        return DUMMY_TEXTURE;
    }

    @Override
    protected boolean shouldShowName(DummyEntity entity) {
        return false;
    }
}

