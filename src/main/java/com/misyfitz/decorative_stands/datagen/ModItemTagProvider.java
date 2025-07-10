package com.misyfitz.decorative_stands.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import com.misyfitz.decorative_stands.DecorativeStands;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagProvider extends ItemTagsProvider {

	public ModItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> completableFuture,
			CompletableFuture<TagLookup<Block>> lookupCompletableFuture, @Nullable ExistingFileHelper existingFileHeler) {
		super(pOutput, completableFuture, lookupCompletableFuture, DecorativeStands.MODID, existingFileHeler);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider) {

	}
	
}
