package com.misyfitz.decorative_stands.datagen;

import com.misyfitz.decorative_stands.DecorativeStands;
import com.misyfitz.decorative_stands.util.DSBlocks;
import com.misyfitz.decorative_stands.util.DSItems;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

	public ModRecipeProvider(PackOutput pOutput) {
		super(pOutput, null);
	}

	@Override
	protected void buildRecipes(RecipeOutput output) {
		//shaped
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, DSBlocks.BINOCULAR_STAND.get())
			.pattern(" B ")
			.pattern(" S ")
			.pattern("SSS")
			.define('S', Items.STONE)
			.define('B', DSItems.BINOCULAR.get())
			.unlockedBy(getHasName(DSItems.BINOCULAR.get()), has(DSItems.BINOCULAR.get()))
			.save(output);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, DSItems.BINOCULAR.get())
			.pattern("I I")
			.pattern("AIA")
			.pattern("I I")
			.define('I', Items.IRON_INGOT)
			.define('A', Items.AMETHYST_SHARD)
			.unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
			.save(output);
		
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, DSBlocks.DUMMY_STAND.get())
			.pattern("I I")
			.pattern(" I ")
			.pattern("I I")
			.define('I', Items.OAK_PLANKS)
			.unlockedBy(getHasName(Items.OAK_PLANKS), has(Items.OAK_PLANKS))
			.save(output);

		// Generate shaped recipe for each spyglass stand variant
		
		DSBlocks.LOG_TYPES.forEach(data -> {
		    var block = DSBlocks.SPYGLASS_STANDS.get(data.type());
		    Item logItem = BuiltInRegistries.ITEM.get(data.itemId());

		    if (logItem == Items.AIR) {
		        throw new IllegalArgumentException("Missing log item: " + data.itemId());
		    }

		    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, block.get())
		        .pattern(" S ")
		        .pattern(" W ")
		        .pattern("WWW")
		        .define('S', Items.SPYGLASS)
		        .define('W', logItem)
		        .unlockedBy(RecipeProvider.getHasName(Items.SPYGLASS), RecipeProvider.has(Items.SPYGLASS))
		        .save(output, DecorativeStands.MODID + ":" + data.type() + "_spyglass_stand");
		});

		// Generic recipe using forge:stripped_logs
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, DSBlocks.SPYGLASS_STANDS.get("oak").get())
		    .pattern(" S ")
		    .pattern(" W ")
		    .pattern("WWW")
		    .define('S', Items.SPYGLASS)
		    .define('W', ItemTags.LOGS)
		    .unlockedBy(getHasName(Items.SPYGLASS), has(Items.SPYGLASS))
		    .save(output, DecorativeStands.MODID + ":spyglass_stand_from_any_log");
	}
}
