package com.misyfitz.decorative_stands;

import com.misyfitz.decorative_stands.compat.Mods;
import com.misyfitz.decorative_stands.client.ClientZoomHandler;
import com.misyfitz.decorative_stands.client.model.SpyglassTubeModel;
import com.misyfitz.decorative_stands.content.block.entity.renderer.BinocularStandBlockEntityRenderer;
import com.misyfitz.decorative_stands.content.block.entity.renderer.SpyglassStandBlockEntityRenderer;
import com.misyfitz.decorative_stands.util.DSItemProperties;
import com.misyfitz.decorative_stands.util.DSTags;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.client.gui.layouts.AbstractLayout;
import org.slf4j.Logger;


@Mod(DecorativeStands.MODID)
public class DecorativeStands {

    public static final String MODID = "decorative_stands";

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogUtils.getLogger();

    public static IEventBus modEventBus;
    //private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    @SuppressWarnings("removal")
	public DecorativeStands() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        
    	//ModLoadingContext modLoadingContext = ModLoadingContext.get();
        
        //REGISTRATE.registerEventListeners(modEventBus);
        //REGISTRATE.setCreativeTab(DSCreativeTabs.MAIN);
        
        DSTags.register(modEventBus);
        DSBlocks.register(modEventBus);
        DSItems.register(modEventBus);
        DSBlockEntities.register(modEventBus);
        DSCreativeTabs.register(modEventBus);
        DSSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        forgeEventBus.addListener(this::registerCommands);

        MinecraftForge.EVENT_BUS.register(this);
        
        //ModLoadingContext.get().registerConfig(Config.Type.COMMON), null);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Mods.JEI.executeIfInstalled(() -> DSRecipes::register);
        });
    }

    private void registerCommands(RegisterCommandsEvent event) {
        DSCommands.register(event.getDispatcher());
    }

    @SuppressWarnings("removal")
	public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
    
    @Mod.EventBusSubscriber(
    		modid = MODID, 
    		bus = Mod.EventBusSubscriber.Bus.MOD, 
    		value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(DSBlockEntities.BINOCULAR_STAND_BE.get(), BinocularStandBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(DSBlockEntities.SPYGLASS_STAND_BE.get(), SpyglassStandBlockEntityRenderer::new);

        }
        
        
        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(SpyglassTubeModel.LAYER_LOCATION, SpyglassTubeModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerBelow(VanillaGuiOverlay.HOTBAR.id(), 
                "scope", 
                ClientZoomHandler.ZOOM_OVERLAY);
        }


        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        	DSItemProperties.addCustomItemProperties();
        	
        }


    }
}
