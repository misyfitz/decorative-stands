package com.misyfitz.decorative_stands.content.block;

import javax.annotation.Nullable;

import com.misyfitz.decorative_stands.client.ClientZoomHandler;
import com.misyfitz.decorative_stands.client.ScopeOverlay;
import com.misyfitz.decorative_stands.content.block.entity.BinocularStandBlockEntity;
import com.misyfitz.decorative_stands.util.DSBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BinocularStandBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final VoxelShape SHAPE = Block.box(6, 0, 6, 10, 24, 10);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BinocularStandBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
        		.setValue(FACING, Direction.NORTH));
        
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.player != null && mc.level != null && ClientZoomHandler.isZooming()) {
//            return RenderShape.INVISIBLE;
//        }

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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if (!level.isClientSide && level.getBlockEntity(pos) instanceof BinocularStandBlockEntity stand) {
            // Set the player as user
            stand.setUser(player.getUUID());
            stand.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);

            // Teleport player behind the block (opposite of facing)
            Direction facing = state.getValue(FACING);
            Vec3 behind = Vec3.atCenterOf(pos).relative(facing.getOpposite(), 1);
            player.setDeltaMovement(0, 0, 0);
            player.teleportTo(behind.x, player.getY(), behind.z);
            player.setYRot(facing.toYRot());
            player.setXRot(0);
            ClientZoomHandler.startCustomZoom(pos, 1.5F, ScopeOverlay.BINOCULAR_SCOPE); // Start zoom with custom overlay
            level.playSound(null, pos, SoundEvents.SPYGLASS_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    	return type == DSBlockEntities.BINOCULAR_STAND_BE.get()
    		    ? (level1, pos1, state1, be) -> BinocularStandBlockEntity.tick(level1, pos1, state1, (BinocularStandBlockEntity) be)
    		    : null;
    }

	@Nullable	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
	    return new BinocularStandBlockEntity(pos, state);
	}

}
