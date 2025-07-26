package com.misyfitz.decorative_stands.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import com.misyfitz.decorative_stands.DecorativeStands;
import com.misyfitz.decorative_stands.util.DSBlocks;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagGenerator extends BlockTagsProvider {

	public ModBlockTagGenerator(PackOutput output, CompletableFuture<Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, DecorativeStands.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider provider) {
	    this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
	        .add(DSBlocks.BINOCULAR_STAND.get());

	    this.tag(BlockTags.MINEABLE_WITH_AXE)
	        .add(DSBlocks.DUMMY_STAND.get())
	    	.add(DSBlocks.WEAPON_STAND.get());

	    DSBlocks.SPYGLASS_STANDS.values().forEach(entry ->
	        this.tag(BlockTags.MINEABLE_WITH_AXE).add(entry.get())
	    );
	}


}
