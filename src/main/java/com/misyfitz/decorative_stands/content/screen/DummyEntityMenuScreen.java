package com.misyfitz.decorative_stands.content.screen;

import com.misyfitz.decorative_stands.DecorativeStands;
import com.misyfitz.decorative_stands.content.entity.DummyEntity;
import com.misyfitz.decorative_stands.util.DSEntities;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DummyEntityMenuScreen extends AbstractContainerScreen<DummyEntityMenu> {

    @SuppressWarnings("removal")
	private static final ResourceLocation DUMMY_GUI = new ResourceLocation(DecorativeStands.MODID, "textures/gui/dummy/inventory.png");
    @SuppressWarnings("unused")
	private float renderRotation = 180f;
    @SuppressWarnings("unused")
	private float originalRotation;

    public DummyEntityMenuScreen(DummyEntityMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

//    @Override
//    protected void init() {
//        super.init();
//
//        DummyEntity dummy = menu.getDummy();
//        if (dummy != null) {
//            this.originalRotation = dummy.getYRot();
//            this.renderRotation = 0f - originalRotation; // Face the viewer
//        }
//
//        int centerX = (this.width - this.imageWidth) / 2;
//        int centerY = (this.height - this.imageHeight) / 2;
//
//        ForgeSlider rotationSlider = new ForgeSlider(
//        	    centerX + 30, centerY - 10,
//        	    120, 20, // 20 for better click target
//        	    Component.literal("Rotation: "),
//        	    Component.literal("°"),
//        	    0.0D, 360.0D,
//        	    renderRotation,
//        	    false
//        	) {
//        	    @Override
//        	    protected void applyValue() {
//        	        renderRotation = (float) this.getValue();
//        	        this.setMessage(Component.literal("Rotation: " + (int) renderRotation + "°"));
//        	    }
//        	};
//
//        this.addRenderableWidget(rotationSlider);
//        // Optional: set initial message explicitly
//        rotationSlider.setMessage(Component.literal("Rotation: " + (int) renderRotation + "°"));
//
//        this.addRenderableWidget(rotationSlider);
//
//        // Reset button
//        this.addRenderableWidget(Button.builder(
//        	    Component.literal("🔄"), // "🔄"/"↺"
//        	    btn -> {
//        	        renderRotation = 180f - originalRotation;
//        	        rotationSlider.setValue(renderRotation); // Sync slider
//        	        rotationSlider.setMessage(Component.literal("Rotation: " + (int) renderRotation + "°"));
//        	    }
//        	).bounds(centerX + 115, centerY + 60, 20, 20).build());
//    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
    	
        this.renderTransparentBackground(guiGraphics); // Updated for 1.20.4
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTick, int xMouse, int yMouse) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, DUMMY_GUI);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        gui.blit(DUMMY_GUI, x, y, 0, 0, imageWidth, imageHeight);

        // Render Dummy Entity
//        DummyEntity dummy = menu.getDummy(); // implement getDummy() in your DummyEntityMenu
        DummyEntity dummy = new DummyEntity(DSEntities.DUMMY.get(), Minecraft.getInstance().level);
        menu.getDummy();
		DummyEntity.copyItemStackHandler(menu.getDummy().getInventory(), dummy.getInventory());


        if (dummy != null) {
        	int entityX = this.width / 2;
            int entityY = y + 50;
            int scale = 30;

            // Slow rotation using system time (or keep constant for static pose)
            float rotation = (System.currentTimeMillis() % 10000L) / 10000f * 360f;

            renderDummyEntityInInventory(gui, entityX, entityY, scale, rotation, dummy);
        }
    }

    public static void renderDummyEntityInInventory(GuiGraphics gui, int x, int y, int scale, float yRot, DummyEntity dummy) {
    	
    	RenderSystem.enableDepthTest();
        
    	dummy.setYRot(yRot);
        dummy.setYHeadRot(yRot);
        dummy.setYBodyRot(yRot);
        dummy.setXRot(0f);

        dummy.yRotO = yRot;
        dummy.yHeadRotO = yRot;
        dummy.yBodyRotO = yRot;


        gui.pose().pushPose();
        gui.pose().translate(x, y, 50);
        gui.pose().scale(scale, scale, scale); // flip horizontally for correct face
        gui.pose().mulPose(Axis.ZP.rotationDegrees(180f));

        PoseStack poseStack = gui.pose();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super DummyEntity> renderer = dispatcher.getRenderer(dummy);

        
//        int light = Minecraft.getInstance().level.getLightEngine().getRawBrightness(dummy.blockPosition(), 0);
//        renderer.render(dummy, 0.0f, 0.0f, poseStack, gui.bufferSource(), light);
        
        Lighting.setupForEntityInInventory();
        dispatcher.setRenderShadow(false);

        renderer.render(dummy, 0f, 0f, poseStack, gui.bufferSource(), 15728880);

        gui.flush();
        dispatcher.setRenderShadow(true);
        Lighting.setupFor3DItems();

        gui.pose().popPose();
    }
    
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Skip rendering title and player inventory title
    }

}
