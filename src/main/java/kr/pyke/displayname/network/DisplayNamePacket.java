package kr.pyke.displayname.network;

import kr.pyke.displayname.network.payload.c2s.C2S_ChangeDisplayNamePayload;
import kr.pyke.displayname.network.payload.s2c.S2C_DisplayNameChangeResponsePayload;
import kr.pyke.displayname.network.payload.s2c.S2C_OpenChangeDisplayNameScreenPayload;
import kr.pyke.displayname.network.payload.s2c.S2C_SendBulkDisplayNamePayload;
import kr.pyke.displayname.network.payload.s2c.S2C_SendSingleDisplayNamePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class DisplayNamePacket {
    private DisplayNamePacket() { }

    public static void registerCodec() {
        // Server → Client
        PayloadTypeRegistry.playS2C().register(S2C_SendSingleDisplayNamePayload.ID, S2C_SendSingleDisplayNamePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(S2C_SendBulkDisplayNamePayload.ID, S2C_SendBulkDisplayNamePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(S2C_DisplayNameChangeResponsePayload.ID, S2C_DisplayNameChangeResponsePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(S2C_OpenChangeDisplayNameScreenPayload.ID, S2C_OpenChangeDisplayNameScreenPayload.STREAM_CODEC);

        // Client → Server
        PayloadTypeRegistry.playC2S().register(C2S_ChangeDisplayNamePayload.ID, C2S_ChangeDisplayNamePayload.STREAM_CODEC);
    }

    public static void registerServer() {
        // C2S_ChangeDisplayNamePayload
        ServerPlayNetworking.registerGlobalReceiver(C2S_ChangeDisplayNamePayload.ID, C2S_ChangeDisplayNamePayload::handle);
    }

    public static void registerClient() {
        // S2C_SendSingleDisplayNamePayload
        ClientPlayNetworking.registerGlobalReceiver(S2C_SendSingleDisplayNamePayload.ID, S2C_SendSingleDisplayNamePayload::handle);
        // S2C_SendBulkDisplayNamePayload
        ClientPlayNetworking.registerGlobalReceiver(S2C_SendBulkDisplayNamePayload.ID, S2C_SendBulkDisplayNamePayload::handle);
        // S2C_DisplayNameChangeResponsePayload
        ClientPlayNetworking.registerGlobalReceiver(S2C_DisplayNameChangeResponsePayload.ID, S2C_DisplayNameChangeResponsePayload::handle);
        // S2C_OpenChangeDisplayNameScreenPayload
        ClientPlayNetworking.registerGlobalReceiver(S2C_OpenChangeDisplayNameScreenPayload.ID, S2C_OpenChangeDisplayNameScreenPayload::handle);
    }
}
