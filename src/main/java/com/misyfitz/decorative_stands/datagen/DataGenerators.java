package com.misyfitz.decorative_stands.datagen;

import java.util.concurrent.CompletableFuture;

import com.misyfitz.decorative_stands.DecorativeStands;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DecorativeStands.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		
		//generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));
		generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
		
		//BlockTagsProvider blockTagsPorvider = new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
		//generator.addProvider(event.includeServer(), blockTagsPorvider);
		//generator.addProvider(event.includeServer(), new ModItemTagProvider(packOutput, lookupProvider, blockTagsPorvider.contentsGetter(), existingFileHelper));
		//client only
		generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
		generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
	}
}
