package com.misyfitz.decorative_stands.content.block.entity;

import com.misyfitz.decorative_stands.DSBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SpyglassStandBlockEntity extends AbstractStandBlockEntity {
    public SpyglassStandBlockEntity(BlockPos pos, BlockState state) {
        super(DSBlockEntities.SPYGLASS_STAND_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SpyglassStandBlockEntity entity) {
        AbstractStandBlockEntity.tick(level, pos, state, entity);
    }
}
