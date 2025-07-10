package com.misyfitz.decorative_stands.content.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import com.misyfitz.decorative_stands.client.ClientZoomHandler;

import java.util.function.Consumer;

public class BinocularItem extends Item {
    public static final int USE_DURATION = 1200;
    public static final float ZOOM_FOV_MODIFIER = 0.1F;

    public BinocularItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return USE_DURATION;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.playSound(SoundEvents.SPYGLASS_USE, 1.0F, 1.0F);
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        ClientZoomHandler.startCustomZoom();
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
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
        pUser.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CUSTOM; // Important: use CUSTOM so we can override it fully
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private static final HumanoidModel.ArmPose BINOCULAR = HumanoidModel.ArmPose.create("BINOCULAR", true, (model, entity, arm) -> {
                model.rightArm.xRot = -1.5F;
                model.rightArm.yRot = -0.35F;
                model.leftArm.xRot = -1.5F;
                model.leftArm.yRot = 0.35F;
            });

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
                if (entity.getUseItem() == stack && entity.getUsedItemHand() == hand) {
                    return BINOCULAR;
                }
                return HumanoidModel.ArmPose.EMPTY;
            }
        });
    }

}
