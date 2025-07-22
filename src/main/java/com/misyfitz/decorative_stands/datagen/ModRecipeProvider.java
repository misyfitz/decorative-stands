package com.misyfitz.decorative_stands.datagen;

import java.util.function.Consumer;

import com.misyfitz.decorative_stands.DecorativeStands;
import com.misyfitz.decorative_stands.util.DSBlocks;
import com.misyfitz.decorative_stands.util.DSItems;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

	public ModRecipeProvider(PackOutput pOutput) {
		super(pOutput);
	}

	@SuppressWarnings({ "removal", "deprecation" })
	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
		//shaped
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, DSBlocks.BINOCULAR_STAND.get())
			.pattern(" B ")
			.pattern(" S ")
			.pattern("SSS")
			.define('S', Items.STONE)
			.define('B', DSItems.BINOCULAR.get())
			.unlockedBy(getHasName(DSItems.BINOCULAR.get()), has(DSItems.BINOCULAR.get()))
			.save(pWriter);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, DSItems.BINOCULAR.get())
			.pattern("I I")
			.pattern("AIA")
			.pattern("I I")
			.define('I', Items.IRON_INGOT)
			.define('A', Items.AMETHYST_SHARD)
			.unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
			.save(pWriter);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, DSBlocks.DUMMY_STAND.get())
			.pattern("I I")
			.pattern(" I ")
			.pattern("I I")
			.define('I', Items.OAK_PLANKS)
			.unlockedBy(getHasName(Items.OAK_PLANKS), has(Items.OAK_PLANKS))
			.save(pWriter);

		// Generate shaped recipe for each spyglass stand variant
		DSBlocks.LOG_TYPES.forEach(type -> {
		    var block = DSBlocks.SPYGLASS_STANDS.get(type);
		    Item logItem;

		    switch (type) {
		        case "crimson" -> logItem = Items.STRIPPED_CRIMSON_STEM;
		        case "warped" -> logItem = Items.STRIPPED_WARPED_STEM;
		        default -> {
		            var resLoc = new ResourceLocation("minecraft", "stripped_" + type + "_log");
		            logItem = BuiltInRegistries.ITEM.get(resLoc);
		            if (logItem == Items.AIR) {
		                throw new IllegalArgumentException("Missing log item: " + resLoc);
		            }
		        }
		    }

		    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, block.get())
		        .pattern(" S ")
		        .pattern(" W ")
		        .pattern("WWW")
		        .define('S', Items.SPYGLASS)
		        .define('W', logItem)
		        .unlockedBy(getHasName(Items.SPYGLASS), has(Items.SPYGLASS))
		        .save(pWriter, DecorativeStands.MODID + ":" + type + "_spyglass_stand");
		});

		// Generic recipe using forge:stripped_logs
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, DSBlocks.SPYGLASS_STANDS.get("oak").get())
		    .pattern(" S ")
		    .pattern(" W ")
		    .pattern("WWW")
		    .define('S', Items.SPYGLASS)
		    .define('W', ItemTags.create(new ResourceLocation("forge", "stripped_logs")))
		    .unlockedBy(getHasName(Items.SPYGLASS), has(Items.SPYGLASS))
		    .save(pWriter, DecorativeStands.MODID + ":spyglass_stand_from_any_log");
		
	    /* for newer versions 1.21.1+
	    var logTypes = java.util.List.of("acacia", "birch", "cherry", "dark_oak", "jungle", "mangrove", "oak", "spruce", "crimson", "warped");
	     

	    // Generate shaped recipe for each spyglass stand variant
	    logTypes.forEach(type -> {
	        var block = AcousticBlocks.SPYGLASS_STANDS.get(type);
	        var logItem = switch (type) {
	            case "crimson" -> Items.STRIPPED_CRIMSON_STEM;
	            case "warped" -> Items.STRIPPED_WARPED_STEM;
	            default -> Items.byName(new net.minecraft.resources.ResourceLocation("minecraft", "stripped_" + type + "_log"));
	        };

	        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, block.get())
	            .pattern(" S ")
	            .pattern(" W ")
	            .pattern("WWW")
	            .define('S', Items.SPYGLASS)
	            .define('W', logItem)
	            .unlockedBy(getHasName(Items.SPYGLASS), has(Items.SPYGLASS))
	            .save(pWriter, CreateAcoustics.MODID + ":" + type + "_spyglass_stand");
	    });
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, AcousticBlocks.SPYGLASS_STAND.get())
	    .pattern(" S ")
	    .pattern(" W ")
	    .pattern("WWW")
	    .define('S', Items.SPYGLASS)
	    .define('W', net.minecraft.tags.ItemTags.create(new net.minecraftforge.resources.ResourceLocation("forge", "stripped_logs")))
	    .unlockedBy(getHasName(Items.SPYGLASS), has(Items.SPYGLASS))
	    .save(pWriter, CreateAcoustics.MODID + ":spyglass_stand_from_any_log");
	    */

		//shapeless
		/*ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, AcousticBlocks.SPYGLASS_STAND.get(), 1)
			.requires(Items.SPYGLASS)
			.requires(Items.STRIPPED_DARK_OAK_LOG)
			.unlockedBy(getHasName(Items.SPYGLASS), has(Items.SPYGLASS))
			.save(pWriter, CreateAcoustics.MODID+":dark_oak_spyglass_stand");*/
	}
}
