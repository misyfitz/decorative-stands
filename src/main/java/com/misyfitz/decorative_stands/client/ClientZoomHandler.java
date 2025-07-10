package com.misyfitz.decorative_stands.client;

import com.misyfitz.decorative_stands.content.block.entity.AbstractStandBlockEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(modid = "decorative_stands", value = Dist.CLIENT)
public class ClientZoomHandler {

    private static boolean isZooming = false;
    private static BlockPos zoomedBlockPos = null;
    private static float customFov = 0.1F;
    @SuppressWarnings("removal")
	private static ResourceLocation scopeTexture = new ResourceLocation("minecraft", "textures/misc/spyglass_scope.png");
    private static float scopeScale = 0.5F;

    private static CameraType previousCameraType = null;

    // --- Zoom control API ---
    @SuppressWarnings("removal")
	public static void startCustomZoom() {
        startCustomZoom(null, 0.1F, new ResourceLocation("minecraft", "textures/misc/spyglass_scope.png"));
    }
    
    @SuppressWarnings("removal")
	public static void startCustomZoom(BlockPos pos) {
        startCustomZoom(pos, 0.1F, new ResourceLocation("minecraft", "textures/misc/spyglass_scope.png"));
    }

    public static void startCustomZoom(BlockPos pos, ResourceLocation texture) {
        startCustomZoom(pos, 0.1F, texture);
    }

    public static void startCustomZoom(BlockPos pos, float fov, ResourceLocation texture) {
        if (isZooming)
            return;

    	zoomedBlockPos = pos;
        isZooming = true;
        customFov = fov;
        scopeTexture = texture;

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
        if (mc.player != null) {
            mc.player.setShiftKeyDown(false);
        }

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
        if (previousCameraType != null) {
            mc.options.setCameraType(previousCameraType);
            previousCameraType = null;
        }

        isZooming = false;
        zoomedBlockPos = null;

        MinecraftForge.EVENT_BUS.unregister(ClientZoomHandler.class);
    }


    // --- Forge Events ---
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (!isZooming || mc.level == null || mc.player == null)
            return;

        // Stop zoom if player exits first-person or presses crouch
        if (mc.options.keyShift.isDown() || mc.options.getCameraType() != CameraType.FIRST_PERSON) {
            stopZoom();
            return;
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!isZooming) return;
        renderCustomScopeOverlay(event.getGuiGraphics(), Minecraft.getInstance().getFrameTime());
        //float partialTick = Minecraft.getInstance().getFrameTime();
        //renderCustomScopeOverlay(event.getGuiGraphics(), partialTick);
    }


    @SubscribeEvent
    public static void onComputeFov(ComputeFovModifierEvent event) {
        if (isZooming) {
            event.setNewFovModifier(customFov);
        }
    }

    // --- Overlay rendering ---
    private static void renderCustomScopeOverlay(GuiGraphics guiGraphics, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        float f = (float)Math.min(screenWidth, screenHeight);
        scopeScale = Mth.lerp(0.5F * partialTick, scopeScale, 1.125F);
        float scaledSize = Math.min((float) screenWidth / f, (float) screenHeight / f) * scopeScale;

        int texWidth = Mth.floor(f * scaledSize);
        int texHeight = Mth.floor(f * scaledSize);
        int x0 = (screenWidth - texWidth) / 2;
        int y0 = (screenHeight - texHeight) / 2;
        int x1 = x0 + texWidth;
        int y1 = y0 + texHeight;

        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager._depthMask(false);
        //GlStateManager.alphaFunc(GL11.GL_LESS, 1.0F);
        GlStateManager._depthMask(true);

        //RenderSystem.setShader(GameRenderer::getPositionTexShader);
        //RenderSystem.setShaderTexture(1, scopeTexture);

        guiGraphics.blit(scopeTexture, x0, y0, -10, 0.0F, 0.0F, texWidth, texHeight, texWidth, texHeight);
        guiGraphics.fill(RenderType.guiOverlay(), 0, y1, screenWidth, screenHeight, -300, 0xFF000000);

        // Blacken outer screen
        //guiGraphics.fill(RenderType.guiOverlay(), 0, y1, screenWidth, screenHeight, -90, 0xFF000000);
        guiGraphics.fill(RenderType.guiOverlay(), 0, 0, screenWidth, y0, -90, 0xFF000000);
        guiGraphics.fill(RenderType.guiOverlay(), 0, y0, x0, y1, -90, 0xFF000000);
        guiGraphics.fill(RenderType.guiOverlay(), x1, y0, screenWidth, y1, -90, 0xFF000000);

        GlStateManager._disableBlend();
        //RenderSystem.enableDepthTest(); // Restore normal depth
        //RenderSystem.disableBlend();
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
