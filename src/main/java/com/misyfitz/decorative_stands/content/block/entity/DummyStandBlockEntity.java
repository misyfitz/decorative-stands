package com.misyfitz.decorative_stands.content.block.entity;

import com.misyfitz.decorative_stands.content.block.DummyStandBlock;
import com.misyfitz.decorative_stands.content.entity.DummyEntity;
import com.misyfitz.decorative_stands.util.DSBlockEntities;
import com.misyfitz.decorative_stands.util.DSEntities;
import com.misyfitz.decorative_stands.util.DSUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import java.util.UUID;

public class DummyStandBlockEntity extends BlockEntity {

	private String playerSkinName;

	public void setPlayerSkinName(String name) {
	    this.playerSkinName = name;
	    setChanged();
	}

	public String getCustomNameFromPlacedBlock() {
	    return playerSkinName;
	}
	
    private final ItemStackHandler inventory = new ItemStackHandler(6) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide) {
                DummyEntity dummy = findLinkedDummyEntity(level);
                if (dummy != null) dummy.syncFromInventory(this);
            }
        }
    };

    private final LazyOptional<ItemStackHandler> optionalHandler = LazyOptional.of(() -> inventory);
    private UUID dummyEntityUUID;

    public DummyStandBlockEntity(BlockPos pos, BlockState state) {
        super(DSBlockEntities.DUMMY_STAND_BE.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public void clearContents() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void drops() {
        DSUtils.dropDummyInventory(this.level, this.worldPosition, inventory);
    }

    public void setDummyEntity(DummyEntity entity) {
        this.dummyEntityUUID = entity.getUUID();
    }

    public DummyEntity findLinkedDummyEntity(Level level) {
        if (dummyEntityUUID == null || level.isClientSide) return null;
        for (var entity : level.getEntitiesOfClass(DummyEntity.class, new AABB(worldPosition).inflate(2))) {
            if (dummyEntityUUID.equals(entity.getUUID())) return entity;
        }
        return null;
    }

    @Override
    public void onLoad() {
        if (level instanceof ServerLevel server && dummyEntityUUID == null) {
            DummyEntity dummy = DSEntities.DUMMY.get().create(server);
            if (dummy != null) {
                // Determine block rotation based on FACING
                BlockState state = getBlockState();
                Direction facing = state.hasProperty(DummyStandBlock.FACING)
                        ? state.getValue(DummyStandBlock.FACING)
                        : Direction.SOUTH; // fallback in case property is missing

                float rotationYaw = switch (facing) {
                    case NORTH -> 180f;
                    case SOUTH -> 0f;
                    case WEST -> 90f;
                    case EAST -> -90f;
                    default -> 0f;
                };

                dummy.setYRot(rotationYaw);
                dummy.setYHeadRot(rotationYaw);
//                dummy.setBodyRot(rotationYaw);

                // Move to position and spawn
                dummy.moveTo(worldPosition.getX() + 0.5, worldPosition.getY(), worldPosition.getZ() + 0.5);
                
                // === Try get player skin by block custom name ===
//                String customName = getCustomNameFromPlacedBlock();
//                if (customName != null && customName.matches("^[a-zA-Z0-9_]{3,16}$")) {
//                	MinecraftServer minecraftServer = level.getServer();
//                	GameProfileCache cache = minecraftServer.getProfileCache();
//                	Optional<GameProfile> maybeProfile = cache.get(customName);
//
//                	maybeProfile.ifPresent(profile -> {
//                	    minecraftServer.getProfileRepository().findProfileByName(profile.getName(), result -> {
//                	        if (result.isPresent()) {
//                	            GameProfile filled = result.get();
//                	            Property textures = filled.getProperties().get("textures").stream().findFirst().orElse(null);
//                	            if (textures != null) {
//                	                dummy.setSkinTexture(filled); // You’ll need to handle ResourceLocation conversion
//                	            }
//                	        }
//                	    }, MinecraftServer::submit);
//                	});
//
//                    maybeProfile.ifPresent(profile -> {
//                        // This triggers the skin texture to be loaded
//                    	GameProfile filled = minecraftServer.getProfileCache().get(customName).orElse(null);
//
//                        Property textures = filled.getProperties().get("textures").stream().findFirst().orElse(null);
//                        if (textures != null) {
//                            dummy.setSkinTexture(filled); // Or extract texture and use setSkinTexture()
//                        }
//                    });
//                }
                
                
                // Spawn and store
                server.addFreshEntity(dummy);
                setDummyEntity(dummy);
            }
        }
    }


    public void removeDummyEntity() {
        if (level instanceof ServerLevel server) {
            DummyEntity dummy = findLinkedDummyEntity(server);
            if (dummy != null) dummy.discard();
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return optionalHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        optionalHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, Provider provider) {
        super.saveAdditional(tag, provider);
        if (dummyEntityUUID != null) tag.putUUID("DummyUUID", dummyEntityUUID);
	    if (playerSkinName != null) { tag.putString("PlayerSkinName", playerSkinName); }
    }

    @Override
    public void loadAdditional(CompoundTag tag, Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.hasUUID("DummyUUID")) dummyEntityUUID = tag.getUUID("DummyUUID");
	    if (tag.contains("PlayerSkinName")) { playerSkinName = tag.getString("PlayerSkinName"); }
    }

    
    @SuppressWarnings("deprecation")
	@Override
    public CompoundTag getUpdateTag(Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        tag.put("dummy", inventory.serializeNBT());
        return tag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, DummyStandBlockEntity entity) {
        // Optional tick logic
    }
}
