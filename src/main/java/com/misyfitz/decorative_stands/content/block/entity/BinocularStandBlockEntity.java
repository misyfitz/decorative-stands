package com.misyfitz.decorative_stands.content.block.entity;

import com.misyfitz.decorative_stands.DSBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BinocularStandBlockEntity extends AbstractStandBlockEntity {
	
	public BinocularStandBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(DSBlockEntities.BINOCULAR_STAND_BE.get(), pPos, pBlockState);
	}
	
    public static void tick(Level level, BlockPos pos, BlockState state, BinocularStandBlockEntity entity) {
        AbstractStandBlockEntity.tick(level, pos, state, entity);
    }
}
