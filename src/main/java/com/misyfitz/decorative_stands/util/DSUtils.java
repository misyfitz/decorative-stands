package com.misyfitz.decorative_stands.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

public class DSUtils {
    public static void dropDummyInventory(Level level, BlockPos pos, ItemStackHandler inventory) {
        SimpleContainer container = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            container.setItem(i, inventory.getStackInSlot(i));
        }
        Containers.dropContents(level, pos, container);
    }
}
