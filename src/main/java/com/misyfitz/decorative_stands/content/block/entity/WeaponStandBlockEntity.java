package com.misyfitz.decorative_stands.content.block.entity;

import com.misyfitz.decorative_stands.util.DSBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class WeaponStandBlockEntity extends BlockEntity {

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
		@Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public WeaponStandBlockEntity(BlockPos pos, BlockState state) {
        super(DSBlockEntities.WEAPON_STAND_BE.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public boolean isEmpty() {
        return inventory.getStackInSlot(0).isEmpty();
    }

    public ItemStack getItem() {
        return inventory.getStackInSlot(0);
    }

    public void setItem(ItemStack stack) {
        inventory.setStackInSlot(0, stack);
    }

    public void clearContents() {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void saveAdditional(CompoundTag tag, Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("inventory", inventory.serializeNBT());
    }

    @SuppressWarnings("deprecation")
	@Override
    public void loadAdditional(CompoundTag tag, Provider provider) {
        super.loadAdditional(tag, provider);
        inventory.deserializeNBT(tag.getCompound("inventory"));
    }

    @Override
    public CompoundTag getUpdateTag(Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

//    @Override
//    public Component getDisplayName() {
//        return Component.translatable("block.decorative_stands.weapon_stand");
//    }

    public static void tick(Level level, BlockPos pos, BlockState state, WeaponStandBlockEntity entity) {
        // Placeholder for future ticking logic, if needed.
    }
}
