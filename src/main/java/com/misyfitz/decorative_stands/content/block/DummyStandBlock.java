package com.misyfitz.decorative_stands.content.block;

import java.util.Map;

import javax.annotation.Nullable;

import com.misyfitz.decorative_stands.content.block.entity.DummyStandBlockEntity;
import com.misyfitz.decorative_stands.util.DSBlockEntities;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DummyStandBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public DummyStandBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
        		.setValue(FACING, Direction.NORTH));
	}

    // --- Block Shape and Rendering ---

    public static final MapCodec<DummyStandBlock> CODEC = simpleCodec(DummyStandBlock::new);

	private static final VoxelShape BASE_SHAPE = Block.box(2, 0, 2, 14, 1, 14);
	

	@SuppressWarnings("unused")
	private static final Map<Direction, VoxelShape> SHAPE_MAP = Map.of(
		    Direction.NORTH, BASE_SHAPE,
		    Direction.EAST, BASE_SHAPE,
		    Direction.SOUTH, BASE_SHAPE,
		    Direction.WEST, BASE_SHAPE
		);

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
	    return BASE_SHAPE;
	}



    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
  
    // --- Placement and State Management ---

//    @Override
//    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
//        if (!level.isClientSide && stack.hasCustomHoverName()) {
//            BlockEntity be = level.getBlockEntity(pos);
//            if (be instanceof DummyStandBlockEntity dummyBE) {
//                String customName = stack.getHoverName().getString().trim();
//                String defaultName = Component.translatable("block.decorative_stands.dummy_stand").getString().trim();
//
//                if (!customName.equalsIgnoreCase(defaultName)) {
//                    dummyBE.setPlayerSkinName(customName); // trigger later skin fetch
//                }
//            }
//        }
//    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
	@Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    	return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    	pBuilder.add(FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            if (pLevel.getBlockEntity(pPos) instanceof DummyStandBlockEntity dummyEntity) {
                dummyEntity.drops();
            	dummyEntity.removeDummyEntity();

            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    
    // --- Block Entity ---
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    	return type == DSBlockEntities.DUMMY_STAND_BE.get()
    	         ? (lvl, pos, st, be) -> DummyStandBlockEntity.tick(lvl, pos, st, (DummyStandBlockEntity) be) : null;

    }
    
	@Nullable	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
	    return new DummyStandBlockEntity(pos, state);
	}
}
