package com.misyfitz.decorative_stands.datagen;

import com.misyfitz.decorative_stands.DSBlocks;
import com.misyfitz.decorative_stands.DecorativeStands;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItemModelProvider extends ItemModelProvider {

	public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, DecorativeStands.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		
	    // Register item models for all spyglass stand variants
	    DSBlocks.SPYGLASS_STANDS.forEach((type, block) -> {
	        String registryName = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
	        withExistingParent(registryName, modLoc("block/spyglass_stand/" + type + "_item"));
	    });
		/* for newer fordge ver
		AcousticBlocks.SPYGLASS_STANDS.forEach((type, block) -> {
			BlockItem item = (BlockItem) block.get().asItem();
			withExistingParent(item.getRegistryName().getPath(), modLoc("block/spyglass_stand/" + type));
		});*/
	}

}
