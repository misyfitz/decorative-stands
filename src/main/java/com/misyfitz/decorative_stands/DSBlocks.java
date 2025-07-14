package com.misyfitz.decorative_stands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.misyfitz.decorative_stands.content.block.BinocularStandBlock;
import com.misyfitz.decorative_stands.content.block.SpyglassStandBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DSBlocks {

    //private static final CreateRegistrate REGISTRATE = CreateAcoustics.getRegistrate();
    
    public static final List<String> LOG_TYPES = List.of(
    	    "acacia", "birch", "cherry", "dark_oak", "jungle", 
    	    "mangrove", "oak", "spruce", "crimson", "warped"
    	);


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DecorativeStands.MODID);

    //public static final RegistryObject<Block> SPYGLASS_STAND = registerBlock("spyglass_stand",
            //() -> new SpyglassStandBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final RegistryObject<Block> BINOCULAR_STAND = registerBlock("binocular_stand",
            () -> new BinocularStandBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));

    // Dynamic variants of Spyglass Stand (e.g., for each log type)
    public static final Map<String, RegistryObject<Block>> SPYGLASS_STANDS = new HashMap<>();

    public static void registerSpyglassStandVariant(String name) {
        RegistryObject<Block> block = registerBlock("spyglass_stand_" + name,
                () -> new SpyglassStandBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));
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

        for (String type : LOG_TYPES) {
            registerSpyglassStandVariant(type);
        }
    }
}
