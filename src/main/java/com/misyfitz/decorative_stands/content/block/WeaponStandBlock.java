package com.misyfitz.decorative_stands.content.block;

import javax.annotation.Nullable;

import com.misyfitz.decorative_stands.content.block.entity.WeaponStandBlockEntity;
import com.misyfitz.decorative_stands.util.DSBlockEntities;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WeaponStandBlock extends HorizontalDirectionalBlock implements EntityBlock{

	// Lower base: full from y=0 to y=32 (2 blocks tall)
	private static final VoxelShape BASE = Block.box(2, 0, 2, 14, 2, 14);

	// Upper tube: centered, narrower (e.g. 4x4), from y=32 to y=48
	private static final VoxelShape TUBE = Block.box(6, 2, 6, 10, 16, 10);

	// Combined shape
	public static final VoxelShape SHAPE = Shapes.or(BASE, TUBE);
	
	public static final MapCodec<WeaponStandBlock> CODEC = simpleCodec(WeaponStandBlock::new);
	
	public WeaponStandBlock(Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.stateDefinition.any()
			    .setValue(FACING, Direction.NORTH));
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
	    return RenderShape.MODEL;
	}
	

	
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    	return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    	pBuilder.add(FACING);
    }

    
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult pHitResult) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof WeaponStandBlockEntity stand) {
                ItemStack held = player.getItemInHand(hand);

                if (stand.isEmpty() && !held.isEmpty()) {
                    // Insert one item
                    ItemStack toInsert = held.copy();
                    toInsert.setCount(1);
                    stand.setItem(toInsert);
                    held.shrink(1);
                    return ItemInteractionResult.CONSUME;
                } else if (!stand.isEmpty()) {
                    // Remove item
                    ItemStack removed = stand.getItem().copy();
                    stand.clearContents();
                    if (!player.addItem(removed)) {
                        player.drop(removed, false);
                    }
                    return ItemInteractionResult.CONSUME;
                }
            }
        }

        return ItemInteractionResult.SUCCESS;
    }
    
	@Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof WeaponStandBlockEntity weaponBE) {
                weaponBE.drops(); // call drop logic from your block entity
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }

    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    	return type == DSBlockEntities.WEAPON_STAND_BE.get()
    		    ? (level1, pos1, state1, be) -> WeaponStandBlockEntity.tick(level1, pos1, state1, (WeaponStandBlockEntity) be)
    		    : null;
    }
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new WeaponStandBlockEntity(pPos, pState);
	}
	
}
