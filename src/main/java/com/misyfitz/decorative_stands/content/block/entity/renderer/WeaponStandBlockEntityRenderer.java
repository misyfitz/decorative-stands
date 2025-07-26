package com.misyfitz.decorative_stands.content.block.entity.renderer;

import com.misyfitz.decorative_stands.content.block.entity.WeaponStandBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class WeaponStandBlockEntityRenderer implements BlockEntityRenderer<WeaponStandBlockEntity> {

    public WeaponStandBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

	@Override
    public void render(WeaponStandBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
    }
    
    @SuppressWarnings("unused")
	private int getLightLevel(Level level, BlockPos pos) {
    	int bLight = level.getBrightness(LightLayer.BLOCK, pos);
    	int sLight = level.getBrightness(LightLayer.SKY, pos);
    	return LightTexture.pack(bLight, sLight);
    }
}
