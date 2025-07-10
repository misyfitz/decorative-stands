package com.misyfitz.decorative_stands.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import com.misyfitz.decorative_stands.DecorativeStands;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagProvider extends BlockTagsProvider {

	public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
			@Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, DecorativeStands.MODID, existingFileHelper);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void addTags(HolderLookup.Provider pProvider) {
		// TODO Auto-generated method stub
		
	}
	
}
