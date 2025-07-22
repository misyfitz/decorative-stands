package com.misyfitz.decorative_stands.util;

import com.misyfitz.decorative_stands.DecorativeStands;
import com.misyfitz.decorative_stands.content.screen.DummyEntityMenu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DSMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, DecorativeStands.MODID);
    
    public static final RegistryObject<MenuType<DummyEntityMenu>> DUMMY_ENTITY_MENU =
        MENUS.register("dummy_entity_menu", 
            () -> IForgeMenuType.create(DummyEntityMenu::new));


    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
