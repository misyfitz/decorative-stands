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
		
		generator.addProvider(event.includeServer(), 
				new ModRecipeProvider(packOutput));
		generator.addProvider(event.includeServer(),
				ModLootTableProvider.create(packOutput));
		
		//client only
		generator.addProvider(event.includeClient(),
				new ModBlockStateProvider(packOutput, existingFileHelper));
		generator.addProvider(event.includeClient(),
				new ModItemModelProvider(packOutput, existingFileHelper));
		
		ModBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(), 
				new ModBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), 
				new ModItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
	}
}
