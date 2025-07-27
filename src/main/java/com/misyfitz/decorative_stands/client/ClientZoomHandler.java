package com.misyfitz.decorative_stands.client;

import com.misyfitz.decorative_stands.content.block.entity.AbstractStandBlockEntity;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = "decorative_stands", value = Dist.CLIENT)
public class ClientZoomHandler {

    private static boolean isZooming = false;
    private static BlockPos zoomedBlockPos = null;
    private static float customFov = 0.1F;
    private static int isItemUse = 0;
    public static ResourceLocation currentScope = null;
    private static ItemStack zoomItem = ItemStack.EMPTY;
    //private static float scopeScale = 0.5F;
    @SuppressWarnings("unused")
	private static float scopeAspectRatio = 1.0F;

    private static CameraType previousCameraType = null;
    
    public static final IGuiOverlay ZOOM_OVERLAY = (gui, guiGraphics, partialTick, width, height) -> {

    	if (isZooming && currentScope != null) {
            renderCustomScopeOverlay(guiGraphics, partialTick);
        }
    };


    public static void startZoomFromItem(ResourceLocation texture, float ratio) {
        isItemUse = 1;
        zoomItem = Minecraft.getInstance().player.getMainHandItem();
        startCustomZoom(null, 0.1F, ratio, texture);
    }
    
	public static void startCustomZoom(BlockPos pos) {
        startCustomZoom(pos, 0.1F, 1.0F, ScopeOverlay.SPYGLASS_SCOPE);
    }

    public static void startCustomZoom(BlockPos pos, ResourceLocation texture) {
        startCustomZoom(pos, 0.1F, 1.0F, texture);
    }
    
    public static void startCustomZoom(BlockPos pos, float ratio, ResourceLocation texture) {
        startCustomZoom(pos, 0.1F, ratio, texture);
    }

    public static void startCustomZoom(BlockPos pos, float fov, float ratio, ResourceLocation texture) {
        if (isZooming)
            return;

        scopeAspectRatio = ratio;
    	zoomedBlockPos = pos;
        isZooming = true;
        customFov = fov;
        currentScope = texture;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.setShiftKeyDown(true);
        }

        // Store and override camera type
        previousCameraType = mc.options.getCameraType();
        if (previousCameraType != CameraType.FIRST_PERSON) {
            mc.options.setCameraType(CameraType.FIRST_PERSON);
        }

        MinecraftForge.EVENT_BUS.register(ClientZoomHandler.class);
    }

    public static void stopZoom() {
        if (!isZooming)
            return;
        
        Minecraft mc = Minecraft.getInstance();

        // Clear block usage state
        if (zoomedBlockPos != null && mc.level != null) {
            if (mc.level.getBlockEntity(zoomedBlockPos) instanceof  AbstractStandBlockEntity stand) {
                if (mc.player != null && stand.isUser(mc.player)) {
                    stand.setUser(null);  // clear the user
                    stand.setChanged();
                    mc.level.sendBlockUpdated(zoomedBlockPos, mc.level.getBlockState(zoomedBlockPos), mc.level.getBlockState(zoomedBlockPos), 3);
                }
            }
        }

        // Restore previous camera type
//        if (previousCameraType != null) {
//            mc.options.setCameraType(previousCameraType);
//            previousCameraType = null;
//        }

        isZooming = false;
        zoomedBlockPos = null;
        zoomItem = null;
        isItemUse = 0;
        currentScope = null;

        MinecraftForge.EVENT_BUS.unregister(ClientZoomHandler.class);
    }


    // --- Forge Events ---
    @SuppressWarnings("removal")
	@SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;
        Minecraft mc = Minecraft.getInstance();
        if (!isZooming || mc.level == null || mc.player == null)
            return;
        
//        if (mc.options.getCameraType() != CameraType.FIRST_PERSON) {
//        	previousCameraType = null;
//        	return;
//        	}
        switch (isItemUse) {
            case 0 -> {
                if (mc.options.keyShift.isDown()
                	|| mc.options.keyLeft.isDown()
                    || mc.options.keyRight.isDown()
                    || mc.options.keyUp.isDown()
                    || mc.options.keyDown.isDown()) {
                    mc.player.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
                    stopZoom();
                    return;
                }
            }
            case 1 -> {
                boolean changedItem = !ItemStack.isSameItemSameTags(zoomItem, mc.player.getMainHandItem());
                if (mc.options.keyShift.isDown() || changedItem) {
                    mc.player.playSound(SoundEvents.SPYGLASS_STOP_USING, 1.0F, 1.0F);
                    stopZoom();
                    return;
                }
            }
        }
    }

    
    @SubscribeEvent
    public static void onComputeFov(ComputeFovModifierEvent event) {
        if (isZooming) {
            event.setNewFovModifier(customFov);
        }
    }

    private static void renderCustomScopeOverlay(GuiGraphics guiGraphics, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        if (currentScope != null) {
            if (currentScope.equals(ScopeOverlay.SPYGLASS_SCOPE)) {
                ScopeOverlay.CUSTOM_SCOPE_SPYGLASS.render(null, guiGraphics, partialTick, width, height);
            } else {
                ScopeOverlay.CUSTOM_SCOPE_BINOCULAR.render(null, guiGraphics, partialTick, width, height);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (isZooming) {
            event.setCanceled(true); // Prevent hand rendering entirely
        }
    }
    
    // --- Accessors ---
    @Nullable
    public static BlockPos getZoomedBlockPos() {
        return zoomedBlockPos;
    }

    public static boolean isZooming() {
        return isZooming;
    }
}
