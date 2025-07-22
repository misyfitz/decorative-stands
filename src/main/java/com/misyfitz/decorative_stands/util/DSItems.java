package com.misyfitz.decorative_stands.util;


import com.misyfitz.decorative_stands.DecorativeStands;
import com.misyfitz.decorative_stands.content.item.BinocularItem;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DSItems {
	public static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(ForgeRegistries.ITEMS, DecorativeStands.MODID);

	public static final RegistryObject<Item> BINOCULAR = ITEMS.register("binocular",
			() -> new BinocularItem((new Item.Properties()).stacksTo(1)));
	
	
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
	//public static void register() {
	//}
}
