package com.misyfitz.decorative_stands.content.item;

import com.misyfitz.decorative_stands.client.ClientZoomHandler;
import com.misyfitz.decorative_stands.client.ScopeOverlay;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;


import java.util.function.Consumer;

public class BinocularItem extends Item {
    public static final int USE_DURATION = 1200;
    public static final float ZOOM_FOV_MODIFIER = 0.1F;

    public BinocularItem(Properties pProperties) {
        super(pProperties);
    }

//    @Override
//    public int getUseDuration(ItemStack pStack) {
//        return USE_DURATION;
//    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        player.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
        player.awardStat(Stats.ITEM_USED.get(this));

        if (level.isClientSide) {
        	ClientZoomHandler.startZoomFromItem(ScopeOverlay.BINOCULAR_SCOPE, 1.5F);
        }

        player.startUsingItem(hand); // Triggers use animation and property override
        return InteractionResultHolder.consume(stack);
    }


    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        stopUsing(pLivingEntity);
        return pStack;
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        stopUsing(pLivingEntity);
    }

    private void stopUsing(LivingEntity pUser) {

    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPYGLASS;
    }
    

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

        	@Override
        	public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProgress, float swingProgress) {
        	    if (Minecraft.getInstance().options.getCameraType().isFirstPerson()
        	        && player.getUseItem() == itemInHand
        	        && player.isUsingItem()
        	        && ClientZoomHandler.isZooming()) {

        	        // Move item off screen to the side: right if right hand, left if left hand
        	        float sideOffset = 3.0F; // Try increasing if it’s still slightly visible
        	        int direction = arm == HumanoidArm.RIGHT ? 1 : -1;
        	        poseStack.translate(direction * sideOffset, 0F, 0F);  // Shift sideways

        	        return true; // Still apply transform (but it's way off-screen)
        	    }

        	    // Default transform
        	    int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        	    poseStack.translate(i * 0.56F, -0.52F, -0.72F);
        	    return true;
        	}


        });
    }

}
