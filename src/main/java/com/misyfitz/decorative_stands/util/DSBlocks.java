package com.misyfitz.decorative_stands.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.misyfitz.decorative_stands.DecorativeStands;
import com.misyfitz.decorative_stands.content.block.BinocularStandBlock;
import com.misyfitz.decorative_stands.content.block.DummyStandBlock;
import com.misyfitz.decorative_stands.content.block.SpyglassStandBlock;
import com.misyfitz.decorative_stands.content.block.WeaponStandBlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("removal")
public class DSBlocks {

	public record LogTypeData(String type, String texturePath, ResourceLocation itemId) {}

	public static final List<LogTypeData> LOG_TYPES = new ArrayList<>();

	static {
	    // Vanilla & Nether logs
	    LOG_TYPES.addAll(List.of(
	        new LogTypeData("acacia", "minecraft:block/stripped_acacia_log", new ResourceLocation("minecraft", "stripped_acacia_log")),
	        new LogTypeData("birch", "minecraft:block/stripped_birch_log", new ResourceLocation("minecraft", "stripped_birch_log")),
	        new LogTypeData("cherry", "minecraft:block/stripped_cherry_log", new ResourceLocation("minecraft", "stripped_cherry_log")),
	        new LogTypeData("dark_oak", "minecraft:block/stripped_dark_oak_log", new ResourceLocation("minecraft", "stripped_dark_oak_log")),
	        new LogTypeData("jungle", "minecraft:block/stripped_jungle_log", new ResourceLocation("minecraft", "stripped_jungle_log")),
	        new LogTypeData("mangrove", "minecraft:block/stripped_mangrove_log", new ResourceLocation("minecraft", "stripped_mangrove_log")),
	        new LogTypeData("oak", "minecraft:block/stripped_oak_log", new ResourceLocation("minecraft", "stripped_oak_log")),
	        new LogTypeData("spruce", "minecraft:block/stripped_spruce_log", new ResourceLocation("minecraft", "stripped_spruce_log")),
	        new LogTypeData("crimson", "minecraft:block/stripped_crimson_stem", new ResourceLocation("minecraft", "stripped_crimson_stem")),
	        new LogTypeData("warped", "minecraft:block/stripped_warped_stem", new ResourceLocation("minecraft", "stripped_warped_stem"))
	    ));

	    // Biomes O' Plenty logs — conditional on BOP being loaded
	    if (ModList.get().isLoaded("biomesoplenty")) {
	        LOG_TYPES.addAll(List.of(
	            new LogTypeData("fir", "biomesoplenty:block/stripped_fir_log", new ResourceLocation("biomesoplenty", "stripped_fir_log")),
	            new LogTypeData("pine", "biomesoplenty:block/stripped_pine_log", new ResourceLocation("biomesoplenty", "stripped_pine_log")),
	            new LogTypeData("maple", "biomesoplenty:block/stripped_maple_log", new ResourceLocation("biomesoplenty", "stripped_maple_log")),
	            new LogTypeData("redwood", "biomesoplenty:block/stripped_redwood_log", new ResourceLocation("biomesoplenty", "stripped_redwood_log")),
	            new LogTypeData("mahogany", "biomesoplenty:block/stripped_mahogany_log", new ResourceLocation("biomesoplenty", "stripped_mahogany_log")),
	            new LogTypeData("jacaranda", "biomesoplenty:block/stripped_jacaranda_log", new ResourceLocation("biomesoplenty", "stripped_jacaranda_log")),
	            new LogTypeData("palm", "biomesoplenty:block/stripped_palm_log", new ResourceLocation("biomesoplenty", "stripped_palm_log")),
	            new LogTypeData("willow", "biomesoplenty:block/stripped_willow_log", new ResourceLocation("biomesoplenty", "stripped_willow_log")),
	            new LogTypeData("dead", "biomesoplenty:block/stripped_dead_log", new ResourceLocation("biomesoplenty", "stripped_dead_log")),
	            new LogTypeData("magic", "biomesoplenty:block/stripped_magic_log", new ResourceLocation("biomesoplenty", "stripped_magic_log")),
	            new LogTypeData("umbran", "biomesoplenty:block/stripped_umbran_log", new ResourceLocation("biomesoplenty", "stripped_umbran_log")),
	            new LogTypeData("hellbark", "biomesoplenty:block/stripped_hellbark_log", new ResourceLocation("biomesoplenty", "stripped_hellbark_log")),
	            new LogTypeData("empyreal", "biomesoplenty:block/stripped_empyreal_log", new ResourceLocation("biomesoplenty", "stripped_empyreal_log"))
	        ));
	    }
	    
	}


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DecorativeStands.MODID);

    //public static final RegistryObject<Block> SPYGLASS_STAND = registerBlock("spyglass_stand",
            //() -> new SpyglassStandBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final RegistryObject<Block> BINOCULAR_STAND = registerBlock("binocular_stand",
            () -> new BinocularStandBlock(BlockBehaviour.Properties.of()
            	    .strength(1.5F, 6.0F)
            	    .sound(SoundType.STONE)
            	    .noOcclusion()));
    
    public static final RegistryObject<Block> WEAPON_STAND = registerBlock("weapon_stand",
            () -> new WeaponStandBlock(BlockBehaviour.Properties.of()
            	    .strength(2.0F, 3.0F)
            	    .sound(SoundType.WOOD)
            	    .noOcclusion()));

    public static final RegistryObject<Block> DUMMY_STAND = registerBlock("dummy_stand",
            () -> new DummyStandBlock(BlockBehaviour.Properties.of()
            	    .strength(2.0F, 3.0F)
            	    .sound(SoundType.WOOD)
            	    .noOcclusion()));
    
    // Dynamic variants of Spyglass Stand (e.g., for each log type)
    public static final Map<String, RegistryObject<Block>> SPYGLASS_STANDS = new HashMap<>();

    public static void registerSpyglassStandVariant(String name) {
        RegistryObject<Block> block = registerBlock("spyglass_stand_" + name,
                () -> new SpyglassStandBlock(BlockBehaviour.Properties.of()
                	    .strength(2.0F, 3.0F)
                	    .sound(SoundType.WOOD)
                	    .noOcclusion()));
        SPYGLASS_STANDS.put(name, block);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return DSItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);

        for (LogTypeData data : LOG_TYPES) {
            registerSpyglassStandVariant(data.type());
        }
    }
}
