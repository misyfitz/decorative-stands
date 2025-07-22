package com.misyfitz.decorative_stands.util;

import com.misyfitz.decorative_stands.DecorativeStands;
import com.misyfitz.decorative_stands.content.entity.DummyEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DSEntities {
	 public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
			 DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DecorativeStands.MODID);
	 
	 
	 public static final RegistryObject<EntityType<DummyEntity>> DUMMY = 
			 ENTITY_TYPES.register("dummy", () -> EntityType.Builder.of(DummyEntity::new, MobCategory.MISC)            
					 	.sized(0.7f, 2f)
			            .clientTrackingRange(10)
			            .updateInterval(20)
			            .build("dummy"));
	  
	 public static void register(IEventBus eventBus) {
		 ENTITY_TYPES.register(eventBus);
	 }
}
