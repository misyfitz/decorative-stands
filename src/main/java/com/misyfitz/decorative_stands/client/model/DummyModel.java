package com.misyfitz.decorative_stands.client.model;

import com.misyfitz.decorative_stands.DecorativeStands;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class DummyModel<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation DUMMY_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(DecorativeStands.MODID, "dummy"), "main");
    public DummyModel(ModelPart root) {
        super(root);
        // super will assign root, head, body, rightArm, leftArm, rightLeg, leftLeg internally
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8)
            .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, new CubeDeformation(0.5F)),
            PartPose.offsetAndRotation(0, 0, 0, -0.1047F, 0.0873F, 0));
        
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create()  // MUST exist!
                .texOffs(32, 0)
                .addBox(-4F, -8F, -4F, 8, 8, 8, new CubeDeformation(0.5F)),
                PartPose.ZERO
            );

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
            .texOffs(16, 16).addBox(-4, 0, -2, 8, 12, 4)
            .texOffs(16, 32).addBox(-4, 0, -2, 8, 12, 4, new CubeDeformation(0.25F)),
            PartPose.offset(0, 0, 0));

        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
            .texOffs(40, 16).addBox(-3, -2, -2, 4, 12, 4)
            .texOffs(40, 32).addBox(-3, -2, -2, 4, 12, 4, new CubeDeformation(0.25F)),
            PartPose.offsetAndRotation(-5, 2, 0, -0.1745F, 0, 0));

        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
            .texOffs(32, 48).addBox(-1, -2, -2, 4, 12, 4)
            .texOffs(48, 48).addBox(-1, -2, -2, 4, 12, 4, new CubeDeformation(0.25F)),
            PartPose.offsetAndRotation(5, 2, 0, 0.2094F, 0, 0));

        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
            .texOffs(0, 16).addBox(-2, 0, -2, 4, 12, 4)
            .texOffs(0, 32).addBox(-2, 0, -2, 4, 12, 4, new CubeDeformation(0.25F)),
            PartPose.offsetAndRotation(-1.9F, 12, 0, 0.192F, 0, 0.0349F));

        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
            .texOffs(16, 48).addBox(-2, 0, -2, 4, 12, 4)
            .texOffs(0, 48).addBox(-2, 0, -2, 4, 12, 4, new CubeDeformation(0.25F)),
            PartPose.offsetAndRotation(1.9F, 12, 0, -0.1745F, 0, -0.0349F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    // Optional: Override setupAnim if you want custom animations or poses
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Default humanoid animation can go here or your own pose logic
        //super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    // The root method already exists in HumanoidModel, no need to override unless you have a reason
}
