package com.misyfitz.decorative_stands;

import java.util.List;

import com.misyfitz.decorative_stands.content.block.entity.BinocularStandBlockEntity;
import com.misyfitz.decorative_stands.content.block.entity.SpyglassStandBlockEntity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DSBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DecorativeStands.MODID);
	
	public static final RegistryObject<BlockEntityType<SpyglassStandBlockEntity>> SPYGLASS_STAND_BE =
			BLOCK_ENTITIES.register("spyglass_stand_be", () -> {
				// Collect all spyglass stand blocks into an array
				List<Block> validBlocks = new java.util.ArrayList<>();
				validBlocks.add(DSBlocks.SPYGLASS_STAND.get());
				DSBlocks.SPYGLASS_STANDS.values().forEach(ro -> validBlocks.add(ro.get()));

				// Pass all blocks to the builder
				return BlockEntityType.Builder.of(SpyglassStandBlockEntity::new,
						validBlocks.toArray(new Block[0])
				).build(null);
			});


	
	public static final RegistryObject<BlockEntityType<BinocularStandBlockEntity>> BINOCULAR_STAND_BE =
			BLOCK_ENTITIES.register("binocular_stand_be", () -> BlockEntityType.Builder.of(
					BinocularStandBlockEntity::new, DSBlocks.BINOCULAR_STAND.get()).build(null));
	
	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}

}
