package com.misyfitz.decorative_stands.content.entity;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import com.misyfitz.decorative_stands.content.screen.DummyEntityMenu;
import com.misyfitz.decorative_stands.util.DSBlocks;
import com.misyfitz.decorative_stands.util.DSUtils;
import com.mojang.authlib.GameProfile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.api.distmarker.Dist;


public class DummyEntity extends LivingEntity implements MenuProvider {

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY); // HEAD, CHEST, LEGS, FEET
    private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);  // MAIN, OFFHAND
    
    @SuppressWarnings("removal")
	private ResourceLocation skinTexture = new ResourceLocation("decorative_stands", "textures/entity/dummy.png");
    
    public ResourceLocation getSkinTexture() {
        return skinTexture;
    }
    
    public void setSkinTexture(ResourceLocation texture) {
        this.skinTexture = texture;
    }
    
    private GameProfile skinProfile;
    private UUID skinUUID;

    public void setSkinProfile(GameProfile profile) {
        this.skinProfile = profile;
        this.skinUUID = profile.getId();
    }

    public UUID getSkinUUID() {
        return skinUUID;
    }

    public GameProfile getSkinProfile() {
        return skinProfile;
    }

    
    Direction facing;
    BlockState blockState;
    
    public static final Map<EquipmentSlot, AABB> SLOT_BOUNDING_BOXES = new EnumMap<>(EquipmentSlot.class);


    
    private float damageAccumulated = 0f;
    private int lastHitTick = 0;

    private final ItemStackHandler inventory = new ItemStackHandler(6) {
        @Override
        protected void onContentsChanged(int slot) {
            setItemFromSlot(slot, getStackInSlot(slot));
        }
    };

    private void setItemFromSlot(int slot, ItemStack stack) {
        switch (slot) {
            case 0 -> armorItems.set(0, stack); // FEET
            case 1 -> armorItems.set(1, stack); // LEGS
            case 2 -> armorItems.set(2, stack); // CHEST
            case 3 -> armorItems.set(3, stack); // HEAD
            case 4 -> handItems.set(0, stack);  // MAINHAND
            case 5 -> handItems.set(1, stack);  // OFFHAND
        }
    }

    static {
        SLOT_BOUNDING_BOXES.put(EquipmentSlot.HEAD, new AABB(0.3, 1.6, 0.3, 0.7, 2.0, 0.7));
        SLOT_BOUNDING_BOXES.put(EquipmentSlot.CHEST, new AABB(0.25, 1.2, 0.25, 0.75, 1.6, 0.75));
        SLOT_BOUNDING_BOXES.put(EquipmentSlot.LEGS, new AABB(0.25, 0.8, 0.25, 0.75, 1.2, 0.75));
        SLOT_BOUNDING_BOXES.put(EquipmentSlot.FEET, new AABB(0.25, 0.0, 0.25, 0.75, 0.8, 0.75));
        SLOT_BOUNDING_BOXES.put(EquipmentSlot.MAINHAND, new AABB(0.0, 1.0, 0.25, 0.25, 1.5, 0.75));
        SLOT_BOUNDING_BOXES.put(EquipmentSlot.OFFHAND, new AABB(0.75, 1.0, 0.25, 1.0, 1.5, 0.75));
    }

    
    public DummyEntity(EntityType<? extends LivingEntity> type, Level level) {
        
    	super(type, level);     // and make sure it's not visible

    }

    // === Attributes ===
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D);
    }

    // === Item Slot Management ===
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return armorItems;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        int index = switch (slot) {
            case FEET -> 0;
            case LEGS -> 1;
            case CHEST -> 2;
            case HEAD -> 3;
            case MAINHAND -> 4;
            case OFFHAND -> 5;
            default -> -1;
        };
        return (index >= 0) ? inventory.getStackInSlot(index) : ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        int index = switch (slot) {
            case FEET -> 0;
            case LEGS -> 1;
            case CHEST -> 2;
            case HEAD -> 3;
            case MAINHAND -> 4;
            case OFFHAND -> 5;
            default -> -1;
        };
        if (index >= 0) inventory.setStackInSlot(index, stack);
    }


    
    public void syncFromInventory(ItemStackHandler handler) {
        for (int i = 0; i < 4; i++) {
            armorItems.set(i, handler.getStackInSlot(i));
        }
        handItems.set(0, handler.getStackInSlot(4)); // MAINHAND
        handItems.set(1, handler.getStackInSlot(5)); // OFFHAND
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 localHit, InteractionHand hand) {
        if (player.isSpectator()) return InteractionResult.SUCCESS;

        if (player.isShiftKeyDown()) {
            if (!level().isClientSide && player instanceof ServerPlayer serverPlayer) {
                openMenu(serverPlayer);
            }
            return InteractionResult.CONSUME;
        }


        if (level().isClientSide) return InteractionResult.SUCCESS;

        EquipmentSlot slot = getClickedSlot(localHit);
//        System.out.println(slot);
        ItemStack held = player.getItemInHand(hand);

        if (!held.isEmpty()) {
        	if (slot == EquipmentSlot.OFFHAND) {
                swapItem(player, slot, held, hand);
        	}
        	else {
                EquipmentSlot itemSlot = Mob.getEquipmentSlotForItem(held);
                swapItem(player, itemSlot, held, hand);
        	}
        		
        } else {
            swapItem(player, slot, held, hand);
        }

        return InteractionResult.SUCCESS;
    }

    
    private void swapItem(Player player, EquipmentSlot slot, ItemStack heldItem, InteractionHand hand) {
        int index = switch (slot) {
            case FEET -> 0;
            case LEGS -> 1;
            case CHEST -> 2;
            case HEAD -> 3;
            case MAINHAND -> 4;
            case OFFHAND -> 5;
            default -> -1;
        };
        if (index == -1) return;

        ItemStack current = inventory.getStackInSlot(index);
        inventory.setStackInSlot(index, heldItem);
        player.setItemInHand(hand, current);
    }


    private EquipmentSlot getClickedSlot(Vec3 localHit) {
        double y = localHit.y;
        double x = localHit.x;
        double z = localHit.z;

//        System.out.println("localHit.y = " + y);
//        System.out.println("localHit.x = " + x);
//        System.out.println("localHit.z = " + z);

        // Vertical slots (independent of facing)
        if (y >= 1.6D && hasItemInSlot(EquipmentSlot.HEAD)) {
            return EquipmentSlot.HEAD;
        } else if (y >= 0.4D && y < 0.9D && hasItemInSlot(EquipmentSlot.LEGS)) {
            return EquipmentSlot.LEGS;
        } else if (y < 0.4D && hasItemInSlot(EquipmentSlot.FEET)) {
            return EquipmentSlot.FEET;
        }

        // Chest + Hands: depend on horizontal direction
        boolean prefersLeftSide;
        boolean chestCondition;
        
//        System.out.println(facing);
        switch (facing) {
            case NORTH -> {
                prefersLeftSide = x < -0.15D;
                chestCondition = x > -0.15D;
            }
            case SOUTH -> {
                prefersLeftSide = x > 0.15D;
                chestCondition = x < 0.15D;
            }
            case WEST -> {
                prefersLeftSide = z > 0.15D;
                chestCondition = z < 0.15D;
            }
            case EAST -> {
                prefersLeftSide = z < -0.15D;
                chestCondition = z > -0.15D;
            }
            default -> {
                prefersLeftSide = false;
                chestCondition = false;
            }
        }
//        System.out.println("hand condition = " + prefersLeftSide);
//        System.out.println("chest condition = " +chestCondition);
        if (y >= 0.9D && y < 1.6D && chestCondition && hasItemInSlot(EquipmentSlot.CHEST)) {
            return EquipmentSlot.CHEST;
        }

        return prefersLeftSide ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
    }



    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Inventory")) {
            inventory.deserializeNBT(tag.getCompound("Inventory"));
            syncFromInventory(inventory); // Optional, to refresh visuals
        }
    }
    
    // === Optional Visual/Main Hand Side ===
    
    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return 1.62F;
    }

    
    @Override
    public void tick() {
        super.tick();
        
        BlockPos below = this.blockPosition();
        blockState = level().getBlockState(below);

    	if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
    	    facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
    	} else {
    	    // fallback: derive from entity yaw (Y-axis rotation)
    	    facing = Direction.fromYRot(this.getYRot());
    	}
        if (level().isEmptyBlock(below)) {
            setNoGravity(false);
        } else {
            setNoGravity(true);
            setDeltaMovement(0, 0, 0);
            setOnGround(true);
        }
        
        if (!level().isClientSide && isAlive()) {
            int ticksSinceLastHit = tickCount - lastHitTick;

            if (ticksSinceLastHit > 100) { // after 5 seconds of no damage

                damageAccumulated = 0f;
            }
        }
    }

    public void openMenu(ServerPlayer player) {
//        player.openMenu(new SimpleMenuProvider(
//            (id, inv, p) -> new DummyEntityMenu(id, inv, DummyEntity.this),
//            DummyEntity.this.getDisplayName()
//        ));
    	player.openMenu(this);
    }


    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new DummyEntityMenu(id, playerInventory, this);
    }



    
    @Override
    public Component getDisplayName() {
        return Component.literal("Dummy Stand");
    }
    
    public ItemStackHandler getInventory() {
        return inventory;
    }

    
    public static void copyItemStackHandler(ItemStackHandler from, ItemStackHandler to) {
        for (int i = 0; i < from.getSlots(); i++) {
            to.setStackInSlot(i, from.getStackInSlot(i).copy());
        }
    }

    // === Prevent Drops / Damage / Interference ===

    @SuppressWarnings("deprecation")
	@Override
    public boolean hurt(DamageSource source, float amount) {
        if (level().isClientSide || !isAlive()) return true;

        Entity attacker = source.getEntity();
        float toolEfficiency = 1.0f; // Base multiplier
        float baseDamage = amount;

        if (attacker instanceof Player player) {
            // Creative players instantly destroy it
            if (player.isCreative()) {
                discard();
                die(source);
                return true;
            }

            ItemStack held = player.getMainHandItem();

            // Axe or modded axe tool handling
            if (held.canPerformAction(net.minecraftforge.common.ToolActions.AXE_DIG)) {
                toolEfficiency = 2.0f;
                int effLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, held);
                if (effLevel > 0) {
                    toolEfficiency += effLevel * 0.5f;
                }
            } else {
                // Bare hands or inappropriate tools do flat boosted damage
                baseDamage = 1.5f; // So ~4 hits (6.0 threshold) instead of 20
                toolEfficiency = 1.0f;
            }
        }

        float hardness = 2.0f; // Like wood
        damageAccumulated += baseDamage * toolEfficiency;
        lastHitTick = tickCount;

        // Show breaking particles/sound
        level().levelEvent(2001, blockPosition().above(), Block.getId(DSBlocks.DUMMY_STAND.get().defaultBlockState()));

        if (damageAccumulated >= hardness * 3f) { // Adjusted to ~6.0 HP for faster breaking
            discard();
            die(source);
        }

        return true;
    }

    
    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (!level().isClientSide) {

            BlockPos standPos = blockPosition();
            BlockState blockState = level().getBlockState(standPos);
            
            boolean isCreativePlayer = false;
            Entity attacker = source.getEntity();
            if (attacker instanceof Player player) {
                isCreativePlayer = player.isCreative();
            }

            if (blockState.getBlock() == DSBlocks.DUMMY_STAND.get()) {
                // Don't drop items if player is in Creative
                level().destroyBlock(standPos, !isCreativePlayer, getKillCredit());
            } else {
                // Optional: show breaking effect
                level().levelEvent(2001, standPos, Block.getId(blockState));
            }
        }
    }

    
    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        
        
        if (!this.level().isClientSide && reason.shouldDestroy()) {
            DSUtils.dropDummyInventory(this.level(), this.blockPosition(), this.inventory);
        }
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(DSBlocks.DUMMY_STAND.get());
    }

    
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.EMPTY; // No sound
    }

    
    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        // No loot
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return false; // Makes dummy invulnerable (configurable)
    }

    @Override
    public boolean canBeCollidedWith() {
        return true; // Allows interaction hitbox
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
        // Disable collision push
    }

    @Override
    protected void pushEntities() {
        // Disable pushing other entities
    }

    @Override
    public boolean isSpectator() {
        return false;
    }
    
    @Override
    public boolean shouldBeSaved() {
        return true;
    }



    
}
