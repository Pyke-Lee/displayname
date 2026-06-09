package kr.pyke.displayname.network;

import kr.pyke.displayname.network.payload.s2c.S2C_SendBulkDisplayNamePayload;
import kr.pyke.displayname.network.payload.s2c.S2C_SendSingleDisplayNamePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class DisplayNamePacket {
    private DisplayNamePacket() { }

    public static void registerCodec() {
        // Server → Client
        PayloadTypeRegistry.clientboundPlay().register(S2C_SendSingleDisplayNamePayload.ID, S2C_SendSingleDisplayNamePayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(S2C_SendBulkDisplayNamePayload.ID, S2C_SendBulkDisplayNamePayload.STREAM_CODEC);

        // Client → Server
    }

    public static void registerServer() {
    }

    public static void registerClient() {
        // S2C_SendSingleDisplayNamePayload
        ClientPlayNetworking.registerGlobalReceiver(S2C_SendSingleDisplayNamePayload.ID, S2C_SendSingleDisplayNamePayload::handle);
        // S2C_SendBulkDisplayNamePayload
        ClientPlayNetworking.registerGlobalReceiver(S2C_SendBulkDisplayNamePayload.ID, S2C_SendBulkDisplayNamePayload::handle);
    }
}
