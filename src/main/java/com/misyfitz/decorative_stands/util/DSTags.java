package com.misyfitz.decorative_stands.util;

import com.misyfitz.decorative_stands.DecorativeStands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;

public class DSTags {
	public static class Blocks{
		
		@SuppressWarnings({ "removal", "unused" })
		private static TagKey<Block> tag(String name){
			return BlockTags.create(new ResourceLocation(DecorativeStands.MODID, name));
		}
	}
	
	public static class Items{
		
		public static final TagKey<Item> STANDS = tag("stands");
		
		@SuppressWarnings("removal")
		private static TagKey<Item> tag(String name){
			return ItemTags.create(new ResourceLocation(DecorativeStands.MODID, name));
		}
	}
	
    public static void register(IEventBus eventBus) {

    }
	
    /*public static <T> TagKey<T> optionalTag(IForgeRegistry<T> registry, ResourceLocation id) {
        return registry.tags().createOptionalTagKey(id, Collections.emptySet());
    }

    @SuppressWarnings("removal")
    public static <T> TagKey<T> forgeTag(IForgeRegistry<T> registry, String path) {
        return optionalTag(registry, new ResourceLocation("forge", path));
    }

    public static TagKey<Block> forgeBlockTag(String path) {
        return forgeTag(ForgeRegistries.BLOCKS, path);
    }

    public static TagKey<Item> forgeItemTag(String path) {
        return forgeTag(ForgeRegistries.ITEMS, path);
    }

    public static TagKey<Fluid> forgeFluidTag(String path) {
        return forgeTag(ForgeRegistries.FLUIDS, path);
    }

    public enum NameSpace {
        MOD(DecorativeStands.MODID, false, true),
        FORGE("forge");

        public final String id;
        public final boolean optionalDefault;
        public final boolean alwaysDatagenDefault;

        NameSpace(String id) {
            this(id, true, false);
        }

        NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
            this.id = id;
            this.optionalDefault = optionalDefault;
            this.alwaysDatagenDefault = alwaysDatagenDefault;
        }
    }

    public enum AllEntityTags {
        CHUNK_LOADER_CAPTURABLE;

        public final TagKey<EntityType<?>> tag;
        public final boolean alwaysDatagen;

        AllEntityTags() {
            this(NameSpace.MOD);
        }

        AllEntityTags(NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        AllEntityTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        @SuppressWarnings("removal")
        AllEntityTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            ResourceLocation id = new ResourceLocation(
                namespace.id,
                path == null ? name().toLowerCase(Locale.ROOT) : path
            );

            if (optional) {
                tag = optionalTag(ForgeRegistries.ENTITY_TYPES, id);
            } else {
                tag = null;
            }

            this.alwaysDatagen = alwaysDatagen;
        }


        public boolean matches(EntityType<?> type) {
            return tag != null && type.is(tag);
        }

        public boolean matches(Entity entity) {
            return matches(entity.getType());
        }

        private static void register() {
            // If you want to log or validate tags, do it here.
        }
    }
    
     */
}
