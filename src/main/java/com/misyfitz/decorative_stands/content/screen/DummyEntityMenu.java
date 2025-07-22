package com.misyfitz.decorative_stands.content.screen;

import com.misyfitz.decorative_stands.content.entity.DummyEntity;
import com.misyfitz.decorative_stands.util.DSMenus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DummyEntityMenu extends AbstractContainerMenu {
    
	private final DummyEntity dummy;
    
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    //COUNT OF INV SLOTS
    private static final int TE_INVENTORY_SLOT_COUNT = 6; 
    
    public DummyEntityMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, (DummyEntity) inv.player.level().getEntity(buf.readVarInt()));
    }

    public DummyEntityMenu(int id, Inventory inv, DummyEntity dummy) {
        super(DSMenus.DUMMY_ENTITY_MENU.get(), id);
        this.dummy = dummy;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        IItemHandler handler = dummy.getInventory();

        this.addSlot(new ArmorSlot(handler, 0, 107, 62, EquipmentSlot.FEET));
        this.addSlot(new ArmorSlot(handler, 1, 89, 62, EquipmentSlot.LEGS));
        this.addSlot(new ArmorSlot(handler, 2, 71, 62, EquipmentSlot.CHEST));
        this.addSlot(new ArmorSlot(handler, 3, 53, 62, EquipmentSlot.HEAD));
        this.addSlot(new SlotItemHandler(handler, 4, 30, 62));  // Main hand
        this.addSlot(new SlotItemHandler(handler, 5, 130, 62)); // Offhand
    }

    @Override
    public boolean stillValid(Player player) {
        return dummy.isAlive() && player.distanceToSqr(dummy) < 64.0;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < PLAYER_INVENTORY_ROW_COUNT; ++i) {
            for (int j = 0; j < PLAYER_INVENTORY_COLUMN_COUNT; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < HOTBAR_SLOT_COUNT; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public static class ArmorSlot extends SlotItemHandler {
        private final EquipmentSlot slotType;

        public ArmorSlot(IItemHandler handler, int index, int x, int y, EquipmentSlot slotType) {
            super(handler, index, x, y);
            this.slotType = slotType;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem().canEquip(stack, slotType, null);
        }
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }
    
    public DummyEntity getDummy() {
        return this.dummy;
    }

}

