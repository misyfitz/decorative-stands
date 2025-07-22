package com.misyfitz.decorative_stands.datagen;

import com.misyfitz.decorative_stands.DecorativeStands;
import com.misyfitz.decorative_stands.util.DSBlocks;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, DecorativeStands.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
//        blockWithItem(DSBlocks.BINOCULAR_STAND);
//        blockWithItem(DSBlocks.DUMMY_STAND);
    	
        // Register spyglass stand variants
        generateSpyglassStandVariants();
        generateSpyglassStandItemVariants();
    }

    @SuppressWarnings("unused")
	private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    @SuppressWarnings("unused")
	private void generateSpyglassStandVariants() {

        for (String type : DSBlocks.LOG_TYPES) {
            String name = "spyglass_stand_" + type;
            RegistryObject<Block> block = DSBlocks.SPYGLASS_STANDS.get(type); // Ensure this map exists

            // Build model with dynamic texture
            if (type == "crimson" || type == "warped") {
            	String texturePath = "minecraft:block/stripped_" + type + "_stem";
                ModelFile model = models().withExistingParent("block/spyglass_stand/" + type,
                        modLoc("block/spyglass_stand/block"))
                        .texture("3", texturePath)
                        .texture("4", texturePath)
                        .texture("particle", texturePath);
                // Build blockstate
                VariantBlockStateBuilder builder = getVariantBuilder(block.get());
                builder.forAllStates(state -> {
                    int yRot = (int) state.getValue(HorizontalDirectionalBlock.FACING).getOpposite().toYRot();
                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY(yRot)
                            .build();
                });
            } else {
            	String texturePath = "minecraft:block/stripped_" + type + "_log";
                ModelFile model = models().withExistingParent("block/spyglass_stand/" + type,
                        modLoc("block/spyglass_stand/block"))
                        .texture("3", texturePath)
                        .texture("4", texturePath)
                        .texture("particle", texturePath);
                // Build blockstate
                VariantBlockStateBuilder builder = getVariantBuilder(block.get());
                builder.forAllStates(state -> {
                    int yRot = (int) state.getValue(HorizontalDirectionalBlock.FACING).getOpposite().toYRot();
                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY(yRot)
                            .build();
                });
            }
        }
    }
    
    @SuppressWarnings("unused")
	private void generateSpyglassStandItemVariants() {

        for (String type : DSBlocks.LOG_TYPES) {
            String name = "spyglass_stand_" + type;
            RegistryObject<Block> block = DSBlocks.SPYGLASS_STANDS.get(type); // Ensure this map exists

            // Build model with dynamic texture
            if (type == "crimson" || type == "warped") {
            	String texturePath = "minecraft:block/stripped_" + type + "_stem";
                ModelFile model = models().withExistingParent("block/spyglass_stand/" + type + "_item",
                        modLoc("block/spyglass_stand/item"))
                        .texture("3", texturePath)
                        .texture("4", texturePath)
                        .texture("particle", texturePath);
            } else {
            	String texturePath = "minecraft:block/stripped_" + type + "_log";
                ModelFile model = models().withExistingParent("block/spyglass_stand/" + type + "_item",
                        modLoc("block/spyglass_stand/item"))
                        .texture("3", texturePath)
                        .texture("4", texturePath)
                        .texture("particle", texturePath);
            }
        }
    }

    
    /* for newer fordge veriosn
    private void generateSpyglassStandVariants() {
        Set<String> logTypes = ForgeRegistries.BLOCKS.getTagNames().stream()
                .filter(tag -> tag.location().getPath().equals("stripped_logs"))
                .flatMap(tag -> ForgeRegistries.BLOCKS.getTag(tag).stream())
                .map(block -> ForgeRegistries.BLOCKS.getKey(block))
                .filter(rl -> rl != null && rl.getPath().startsWith("stripped_"))
                .map(rl -> rl.getPath().replace("stripped_", "").replace("_log", ""))
                .collect(Collectors.toSet());

        for (String type : logTypes) {
            RegistryObject<Block> block = AcousticBlocks.SPYGLASS_STANDS.get(type);
            if (block == null) continue;

            String name = type;
            String modelName = "block/spyglass_stand/" + name;
            String texturePath = "minecraft:block/stripped_" + name + "_log";

            ModelFile model = models().withExistingParent(modelName, modLoc("block/spyglass_stand/block"))
                    .texture("3", texturePath)
                    .texture("4", texturePath);

            VariantBlockStateBuilder builder = getVariantBuilder(block.get());
            builder.forAllStates(state -> {
                int yRot = (int) state.getValue(HorizontalDirectionalBlock.FACING).getOpposite().toYRot();
                return ConfiguredModel.builder()
                        .modelFile(model)
                        .rotationY(yRot)
                        .build();
            });
        }
    }*/
}
