//package com.misyfitz.decorative_stands.network;
//
//import java.util.function.Supplier;
//
//import net.minecraft.network.FriendlyByteBuf;
//
//
//
//public class OpenDummyEntityMenuPacket {
//    private final int entityId;
//
//    public OpenDummyEntityMenuPacket(int entityId) {
//        this.entityId = entityId;
//    }
//
//    public OpenDummyEntityMenuPacket(FriendlyByteBuf buf) {
//        this.entityId = buf.readVarInt();
//    }
//
//    public void toBytes(FriendlyByteBuf buf) {
//        buf.writeVarInt(entityId);
//    }
//
//    public void handle(Supplier<NetworkEvent.Context> context) {
//        context.get().enqueueWork(() -> {
//            LocalPlayer player = Minecraft.getInstance().player;
//            if (player == null) return;
//            Entity entity = player.level().getEntity(entityId);
//            if (entity instanceof DummyEntity dummy) {
//                Minecraft.getInstance().setScreen(new DummyEntityMenuScreen(
//                    new DummyEntityMenu(0, player.getInventory(), dummy),
//                    player.getInventory(),
//                    Component.literal("Dummy")
//                ));
//            }
//        });
//        context.get().setPacketHandled(true);
//    }
//}
