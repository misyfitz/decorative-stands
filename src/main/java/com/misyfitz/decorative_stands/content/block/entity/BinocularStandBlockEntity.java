package com.misyfitz.decorative_stands.content.block.entity;

import com.misyfitz.decorative_stands.util.DSBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BinocularStandBlockEntity extends AbstractStandBlockEntity {
	
	public BinocularStandBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(DSBlockEntities.BINOCULAR_STAND_BE.get(), pPos, pBlockState);
	}
	
    public static void tick(Level level, BlockPos pos, BlockState state, BinocularStandBlockEntity entity) {
        AbstractStandBlockEntity.tick(level, pos, state, entity);
    }

	@Override
	public Component getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
		// TODO Auto-generated method stub
		return null;
	}
}
