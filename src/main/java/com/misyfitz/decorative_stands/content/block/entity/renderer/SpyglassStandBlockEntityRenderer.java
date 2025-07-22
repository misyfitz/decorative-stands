package com.misyfitz.decorative_stands.content.block.entity.renderer;

import com.misyfitz.decorative_stands.client.ClientZoomHandler;
import com.misyfitz.decorative_stands.client.model.SpyglassTubeModel;
import com.misyfitz.decorative_stands.content.block.SpyglassStandBlock;
import com.misyfitz.decorative_stands.content.block.entity.SpyglassStandBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

public class SpyglassStandBlockEntityRenderer implements BlockEntityRenderer<SpyglassStandBlockEntity> {

    private final SpyglassTubeModel tubeModel;
	
    public SpyglassStandBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.tubeModel = new SpyglassTubeModel(context.bakeLayer(SpyglassTubeModel.SPYTUBE_LAYER));
    }
    
    @SuppressWarnings({ "unused", "removal" })
	@Override
    public void render(SpyglassStandBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        
        if (ClientZoomHandler.isZooming() && blockEntity.isUser(mc.player)) {
            return; // Hide block for local player only
        }

        poseStack.pushPose();

        // Position the model at the center-top of the block
        poseStack.translate(0.5, 1.05, 0.5);
        Direction facing = blockEntity.getBlockState().getValue(SpyglassStandBlock.FACING);
        float yawDegrees = switch (facing) {
            case NORTH -> 180f;
            case SOUTH -> 0f;
            case WEST  -> -90f;
            case EAST  -> 90f;
            default    -> 0f;
        };
        poseStack.mulPose(Axis.YP.rotationDegrees(yawDegrees));
        if (blockEntity.isBeingUsed() && blockEntity.getUser() != null) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            Player player = mc.level.getPlayerByUUID(blockEntity.getUser());
            if (player != null) {
            	// Player's look vector in world space
            	Vec3 lookVec = player.getLookAngle();

            	// Rotate look vector into block-local space
            	double blockYaw = yawDegrees;
            	double rad = Math.toRadians(-blockYaw);
            	double cos = Math.cos(rad);
            	double sin = Math.sin(rad);

            	double localX = lookVec.x * cos - lookVec.z * sin;
            	double localZ = lookVec.x * sin + lookVec.z * cos;
            	double localY = lookVec.y;

            	float zDegrees = (float) Math.toDegrees(localZ);
            	float xDegrees = (float) Math.toDegrees(localX);
            	float yDegrees = -(float) Math.toDegrees(localY);

            	// Disable movement in one axis depending on block facing
            	switch (facing) {
            	    case NORTH, SOUTH -> {
            	    	xDegrees = -xDegrees;
            	    }
            	    case EAST, WEST -> {
            	    }
            	    default -> {}
            	}

            	// Apply final rotation
            	poseStack.mulPose(Axis.ZP.rotationDegrees(xDegrees));
            	poseStack.mulPose(Axis.XP.rotationDegrees(yDegrees));

            } else {
            	System.out.println("[Spyglass Renderer] Player is null (probably left the dimension or server)");
            }
        } else {
            poseStack.mulPose(Axis.XP.rotationDegrees(115));
            //System.out.println("[Spyglass Renderer] Block is NOT being used");
        }
        RenderType renderType = RenderType.entitySolid(new ResourceLocation("decorative_stands", "textures/item/spyglass_tube.png"));
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);

        tubeModel.renderToBuffer(poseStack, vertexConsumer, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    @SuppressWarnings("unused")
	private int getLightLevel(Level level, BlockPos pos) {
    	int bLight = level.getBrightness(LightLayer.BLOCK, pos);
    	int sLight = level.getBrightness(LightLayer.SKY, pos);
    	return LightTexture.pack(bLight, sLight);
    }
}
