package com.misyfitz.decorative_stands.datagen;

import com.misyfitz.decorative_stands.DecorativeStands;
import com.misyfitz.decorative_stands.util.DSBlocks;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Item;

public class ModItemModelProvider extends ItemModelProvider {

	public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, DecorativeStands.MODID, existingFileHelper);
	}


    @Override
    protected void registerModels() {
        // All spyglass stand variants
	    DSBlocks.SPYGLASS_STANDS.forEach((type, block) -> {
	        String registryName = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
	        withExistingParent(registryName, modLoc("block/spyglass_stand/" + type + "_item"));
	    });

    }

    @SuppressWarnings( "unused" )
	private ItemModelBuilder simpleItems(RegistryObject<Item> binocular) {
    	return withExistingParent(binocular.getId().getPath(),
    			ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
    			ResourceLocation.fromNamespaceAndPath(DecorativeStands.MODID, "item/" + binocular.getId().getPath()));
    }

//	@Override
//	protected void registerModels() {
//		
//	    // Register item models for all spyglass stand variants
//	    DSBlocks.SPYGLASS_STANDS.forEach((type, block) -> {
//	        String registryName = ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
//	        withExistingParent(registryName, modLoc("block/spyglass_stand/" + type + "_item"));
//	    });
//		/* for newer fordge ver
//		AcousticBlocks.SPYGLASS_STANDS.forEach((type, block) -> {
//			BlockItem item = (BlockItem) block.get().asItem();
//			withExistingParent(item.getRegistryName().getPath(), modLoc("block/spyglass_stand/" + type));
//		});*/
//	}

}
