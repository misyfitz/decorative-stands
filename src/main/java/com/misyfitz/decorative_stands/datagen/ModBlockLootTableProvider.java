package com.misyfitz.decorative_stands.datagen;

import com.misyfitz.decorative_stands.DSBlocks;

import java.util.Set;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
	protected ModBlockLootTableProvider(Set<Item> pExplosionResistant, FeatureFlagSet pEnabledFeatures) {
		super(pExplosionResistant, pEnabledFeatures);
		// TODO Auto-generated constructor stub
	}

	//protected ModBlockLootTableProvider(HolderLookup.Provider pRegistries) {
		//super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
	//}
	@Override
	protected void generate() {
		dropSelf(DSBlocks.BINOCULAR_STAND.get());
		dropSelf(DSBlocks.SPYGLASS_STAND.get());
	}
	
	@Override
	protected Iterable<Block> getKnownBlocks(){
		return DSBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
	}
}