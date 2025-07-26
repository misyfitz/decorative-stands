package com.misyfitz.decorative_stands.util;

import com.misyfitz.decorative_stands.DecorativeStands;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DSCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DecorativeStands.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.decorative_stands.main"))
            .icon(() -> new ItemStack(DSBlocks.SPYGLASS_STANDS.get("dark_oak").get()))
            .displayItems((params, output) -> {
            	//items
                output.accept(DSItems.BINOCULAR.get());
                //blocks
                //output.accept(DSBlocks.SPYGLASS_STAND.get());
                //output.accept(DSBlocks.WEAPON_STAND.get());
                output.accept(DSBlocks.BINOCULAR_STAND.get());
                output.accept(DSBlocks.DUMMY_STAND.get());
                // Add all spyglass stand variants to the creative tab
                DSBlocks.LOG_TYPES.forEach(data ->
                output.accept(DSBlocks.SPYGLASS_STANDS.get(data.type()).get())
            );}).build());

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
