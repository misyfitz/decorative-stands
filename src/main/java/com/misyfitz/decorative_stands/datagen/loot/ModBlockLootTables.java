package com.misyfitz.decorative_stands.datagen.loot;

import java.util.Set;

import com.misyfitz.decorative_stands.util.DSBlocks;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTables extends BlockLootSubProvider {
	
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }
	
    @Override
    protected void generate() {
        // Static stand blocks
        dropSelf(DSBlocks.BINOCULAR_STAND.get());
        dropSelf(DSBlocks.DUMMY_STAND.get());

        // Dynamic spyglass stand variants
        DSBlocks.SPYGLASS_STANDS.values().forEach(stand -> dropSelf(stand.get()));
    }
	
	@Override
	protected Iterable<Block> getKnownBlocks(){
		return DSBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
	}
}