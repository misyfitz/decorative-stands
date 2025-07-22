package com.misyfitz.decorative_stands.client.model;

import com.misyfitz.decorative_stands.DecorativeStands;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SpyglassTubeModel extends Model {
    @SuppressWarnings("removal")
	public static final ModelLayerLocation SPYTUBE_LAYER = new ModelLayerLocation(
            new ResourceLocation(DecorativeStands.MODID, "spyglass_tube"), "main");

    private final ModelPart root;
    private final ModelPart tube;

    public SpyglassTubeModel(ModelPart modelPart) {
        super(RenderType::entitySolid);
        this.root = modelPart;
        this.tube = modelPart.getChild("tube");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        // Set tube origin to world Y = 24 - 17 = 7
        PartDefinition tube = root.addOrReplaceChild("tube", CubeListBuilder.create(),
            PartPose.offset(0.0F, 10.0F, 0.0F)); // origin at [8, 17, 8]

        // Cube 2 (bottom part): height 12 → center it vertically around 6
        // But our parent (tube) origin is 17, so offset is (6 - 17) = -11
        tube.addOrReplaceChild("part1",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-2.0F, -12.0F, -2.0F, 4F, 12F, 4F), // from Y=-12 to Y=0
            PartPose.offset(0.0F, 1F, 0.0F)); // lower part is shifted down
        
        // Cube 1 (top part): height 10 → center it vertically around 17
        // Since origin is at 17, offset Y by -(22 - 17) = -5
        tube.addOrReplaceChild("part2",
            CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-1.5F, -10.0F, -1.5F, 3F, 10F, 3F), // from Y=-10 to Y=0
            PartPose.offset(0.0F, -10.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 32, 32);
    }



    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        root.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setupAnim(float time) {
        // Optional animation (remove or tweak if not needed)
        float bob = (float) Math.sin(time * 0.1F) * 0.05F;
        this.tube.y = 24.0F - 9.16131F + bob;
    }
}
