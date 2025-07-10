package com.misyfitz.decorative_stands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DSSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DecorativeStands.MODID);

    @SuppressWarnings("removal")
	public static final RegistryObject<SoundEvent> BINOCULAR_USE =
            SOUND_EVENTS.register("binocular_use",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(DecorativeStands.MODID, "binocular_use")));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
