package com.misyfitz.decorative_stands.content.block.entity.renderer;

import com.misyfitz.decorative_stands.client.ClientZoomHandler;
import com.misyfitz.decorative_stands.content.block.BinocularStandBlock;
import com.misyfitz.decorative_stands.content.block.entity.BinocularStandBlockEntity;
import com.misyfitz.decorative_stands.util.DSItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

public class BinocularStandBlockEntityRenderer implements BlockEntityRenderer<BinocularStandBlockEntity> {

    public BinocularStandBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @SuppressWarnings("unused")
	@Override
    public void render(BinocularStandBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int combinedLight, int combinedOverlay) {

        Minecraft mc = Minecraft.getInstance();

        if (ClientZoomHandler.isZooming() && blockEntity.isUser(mc.player)) {
            return; // Hide block for local player only
        }
        
        poseStack.pushPose();

        // Center and slightly raise the model
        poseStack.translate(0.5, 1.39, 0.5);
        poseStack.scale(1.07f, 1.07f, 1.07f);

        var facing = blockEntity.getBlockState().getValue(BinocularStandBlock.FACING);
        float yawDegrees = switch (facing) {
            case NORTH -> 180f;
            case SOUTH -> 0f;
            case WEST  -> -90f;
            case EAST  -> 90f;
            default    -> 0f;
        };

        poseStack.mulPose(Axis.YP.rotationDegrees(yawDegrees));

        if (blockEntity.isBeingUsed() && blockEntity.getUser() != null) {
            poseStack.mulPose(Axis.XP.rotationDegrees(-90));

            Player player = mc.level.getPlayerByUUID(blockEntity.getUser());
            if (player != null) {
                Vec3 lookVec = player.getLookAngle();

                double rad = Math.toRadians(-yawDegrees);
                double cos = Math.cos(rad);
                double sin = Math.sin(rad);

                double localX = lookVec.x * cos - lookVec.z * sin;
                double localZ = lookVec.x * sin + lookVec.z * cos;
                double localY = lookVec.y;

				float zDegrees = (float) Math.toDegrees(localZ);
                float xDegrees = (float) Math.toDegrees(localX);
                float yDegrees = -(float) Math.toDegrees(localY);

                // Optional: tweak the rotation to look natural
                switch (facing) {
                    case NORTH, SOUTH -> xDegrees = -xDegrees;
                    default -> {}
                }

                poseStack.mulPose(Axis.ZP.rotationDegrees(-xDegrees));
                poseStack.mulPose(Axis.XP.rotationDegrees(yDegrees));

            } else {
                System.out.println("[Binocular Renderer] Player is null");
            }
        } else {
            poseStack.mulPose(Axis.XP.rotationDegrees(-90));
        }

        // Render the binocular item model directly
        ItemStack binocularStack = new ItemStack(DSItems.BINOCULAR.get());
        Minecraft.getInstance().getItemRenderer().renderStatic(
            binocularStack,
            ItemDisplayContext.HEAD,
            combinedLight,
            combinedOverlay,
            poseStack,
            buffer,
            mc.level,
            0
        );

        poseStack.popPose();
    }

    
    @SuppressWarnings("unused")
	private int getLightLevel(Level level, BlockPos pos) {
    	int bLight = level.getBrightness(LightLayer.BLOCK, pos);
    	int sLight = level.getBrightness(LightLayer.SKY, pos);
    	return LightTexture.pack(bLight, sLight);
    }
}
