package com.misyfitz.decorative_stands.client;

import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;


public class ScopeOverlay {
    @SuppressWarnings("removal")
	public static final ResourceLocation SPYGLASS_SCOPE = new ResourceLocation("decorative_stands", "textures/misc/spyglass_scope.png");
    @SuppressWarnings("removal")
	public static final ResourceLocation BINOCULAR_SCOPE = new ResourceLocation("decorative_stands", "textures/misc/binocular_scope.png");
    
//    @SuppressWarnings("unused")
//	private static final TransparencyStateShard TRANSLUCENT_TRANSPARENCY = new TransparencyStateShard("translucent_transparency", () -> {
//        RenderSystem.enableBlend();
//        RenderSystem.blendFuncSeparate(
//            GlStateManager.SourceFactor.SRC_ALPHA,
//            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
//            GlStateManager.SourceFactor.ONE,
//            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
//        );
//    }, () -> {
//        RenderSystem.disableBlend();
//        RenderSystem.defaultBlendFunc();
//    });
    @FunctionalInterface
    public interface ScopeOverlayRenderer {
        void render(GuiGraphics guiGraphics, float partialTick, int width, int height);
    }


    public static final ScopeOverlayRenderer CUSTOM_SCOPE_SPYGLASS = (guiGraphics, partialTick, width, height) -> {
        float scopeScale = 1F;
        RenderSystem.enableBlend();
        
        float f = Math.min(width, height);
        float f1 = Math.min((float)width / f, height / f) * scopeScale;
        int i = Mth.floor(f * f1);
        int j = Mth.floor(f * f1);
        int k = (width - i) / 2;
        int l = (height - j) / 2;
        int i1 = k + i;
        int j1 = l + j;

        guiGraphics.blit(SPYGLASS_SCOPE, k, l, 0, 0.0F, 0.0F, i, j, i, j);
        guiGraphics.fill(RenderType.guiOverlay(), 0, j1, width, height, -90, 0xFF000000); // top
        guiGraphics.fill(RenderType.guiOverlay(), 0, 0, width, l, -90, 0xFF000000);       // bottom
        guiGraphics.fill(RenderType.guiOverlay(), 0, l, k, j1, -90, 0xFF000000);          // left
        guiGraphics.fill(RenderType.guiOverlay(), i1, l, width, j1, -90, 0xFF000000);     // right

        RenderSystem.disableBlend();
    };

    public static final ScopeOverlayRenderer CUSTOM_SCOPE_BINOCULAR = (guiGraphics, partialTick, width, height) -> {
        float screenMin = Math.min(width, height);
        float scopeScale = 1.125F;
        
        RenderSystem.enableBlend();
        
        float scaledHeight = screenMin * scopeScale;
        float scaledWidth = scaledHeight * 1.5F;

        int texWidth = Mth.floor(scaledWidth);
        int texHeight = Mth.floor(scaledHeight);

        int x0 = (width - texWidth) / 2;
        int y0 = (height - texHeight) / 2;
        int x1 = x0 + texWidth;
        int y1 = y0 + texHeight;

        guiGraphics.blit(BINOCULAR_SCOPE, x0, y0, -10, 0.0F, 0.0F, texWidth, texHeight, texWidth, texHeight);

        guiGraphics.fill(RenderType.guiOverlay(), 0, 0, width, y0, -90, 0xFF000000);     // top
        guiGraphics.fill(RenderType.guiOverlay(), 0, y1, width, height, -90, 0xFF000000); // bottom
        guiGraphics.fill(RenderType.guiOverlay(), 0, y0, x0, y1, -90, 0xFF000000);       // left
        guiGraphics.fill(RenderType.guiOverlay(), x1, y0, width, y1, -90, 0xFF000000);   // right

        RenderSystem.disableBlend();
    };

}
