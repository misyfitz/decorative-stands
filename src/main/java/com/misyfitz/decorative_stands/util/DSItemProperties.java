package com.misyfitz.decorative_stands.util;

import com.misyfitz.decorative_stands.DecorativeStands;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class DSItemProperties {

    public static void addCustomItemProperties() {
        ItemProperties.register(DSItems.BINOCULAR.get(), ResourceLocation.fromNamespaceAndPath(DecorativeStands.MODID, "zooming"),
        		(itemStack, clientLevel, entity, seed) -> {
        		    if (entity != null && entity.isUsingItem() && entity.getUseItem() == itemStack) {
        		        return 1;
        		    }
        		    	return 0;
        			});
    }
}

