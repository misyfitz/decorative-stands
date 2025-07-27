//package com.misyfitz.decorative_stands.util;
//
//import com.misyfitz.decorative_stands.DecorativeStands;
//import com.misyfitz.decorative_stands.network.OpenDummyMenuPacket;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.network.NetworkRegistry;
//
//public class DSNetworking {
//    private static final String PROTOCOL_VERSION = "1";
//    public static final net.minecraftforge.network.SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
//        new ResourceLocation(DecorativeStands.MODID, "main"),
//        () -> PROTOCOL_VERSION,
//        (version) -> version.equals(PROTOCOL_VERSION),
//        (version) -> version.equals(PROTOCOL_VERSION)
//    );
//
//    private static int packetId = 0;
//
//    public static void register(IEventBus eventBus) {
//        registerMessages();
//    }
//
//    private static void registerMessages() {
//        INSTANCE.registerMessage(
//            packetId++,
//            OpenDummyMenuPacket.class,
//            OpenDummyMenuPacket::toBytes,
//            OpenDummyMenuPacket::new,
//            OpenDummyMenuPacket::handle
//        );
//    }
//}
