package com.misyfitz.decorative_stands.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import com.misyfitz.decorative_stands.DSBlocks;
import com.misyfitz.decorative_stands.DecorativeStands;
import com.misyfitz.decorative_stands.util.DSTags;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagGenerator extends ItemTagsProvider {

	public ModItemTagGenerator(PackOutput pOutput, CompletableFuture<Provider> completableFuture,
			CompletableFuture<TagLookup<Block>> lookupCompletableFuture, @Nullable ExistingFileHelper existingFileHeler) {
		super(pOutput, completableFuture, lookupCompletableFuture, DecorativeStands.MODID, existingFileHeler);
	}

	@Override
	protected void addTags(Provider pProvider) {

	    // Add the Binocular Stand block as item
	    this.tag(DSTags.Items.STANDS)
	        .add(DSBlocks.BINOCULAR_STAND.get().asItem());

	    // Add all spyglass stand variants as items
	    DSBlocks.SPYGLASS_STANDS.values().forEach(entry -> {
	        this.tag(DSTags.Items.STANDS).add(entry.get().asItem());
	    });

	    // Optionally: Add vanilla Armor Stand
	    this.tag(DSTags.Items.STANDS)
	        .add(Items.ARMOR_STAND);
	}

	
}
