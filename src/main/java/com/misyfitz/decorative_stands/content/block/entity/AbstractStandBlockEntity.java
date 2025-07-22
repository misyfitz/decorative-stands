package com.misyfitz.decorative_stands.content.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

import javax.annotation.Nullable;

public abstract class AbstractStandBlockEntity extends BlockEntity implements MenuProvider {

    protected int time;
    protected float rot, oRot, tRot;
    protected float bobOffset, oBobOffset;
    protected UUID user;

    public AbstractStandBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state); // Placeholder — set proper type in subclass
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    public UUID getUser() {
        return user;
    }

    public boolean isBeingUsed() {
        return user != null;
    }

    public boolean isUser(Player player) {
        return player != null && user != null && player.getUUID().equals(user);
    }

    public void clearUser() {
        this.user = null;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AbstractStandBlockEntity blockEntity) {
        if (blockEntity.isBeingUsed()) {
            Player player = level.getPlayerByUUID(blockEntity.getUser());
            if (player == null || player.isCrouching() || player.distanceToSqr(Vec3.atCenterOf(pos)) > 64) {
                blockEntity.clearUser();
                blockEntity.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
            }
        }

        blockEntity.time++;

        blockEntity.oBobOffset = blockEntity.bobOffset;
        blockEntity.bobOffset = 0.1f + Mth.sin(blockEntity.time * 0.1f) * 0.01f;

        blockEntity.oRot = blockEntity.rot;
        blockEntity.tRot += 0.02f;
        blockEntity.tRot = wrapAngle(blockEntity.tRot);

        float deltaRot = wrapAngle(blockEntity.tRot - blockEntity.rot);
        blockEntity.rot += deltaRot * 0.4f;
    }

    private static float wrapAngle(float angle) {
        while (angle >= Math.PI) angle -= Math.PI * 2f;
        while (angle < -Math.PI) angle += Math.PI * 2f;
        return angle;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Time", time);
        tag.putFloat("Rot", rot);
        tag.putFloat("TRot", tRot);
        if (user != null)
            tag.putUUID("User", user);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        time = tag.getInt("Time");
        rot = tag.getFloat("Rot");
        tRot = tag.getFloat("TRot");
        if (tag.hasUUID("User"))
            user = tag.getUUID("User");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        if (user != null)
            tag.putUUID("User", user);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        user = tag.hasUUID("User") ? tag.getUUID("User") : null;
    }
    
    @Nullable
    @Override
    public net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }
}
