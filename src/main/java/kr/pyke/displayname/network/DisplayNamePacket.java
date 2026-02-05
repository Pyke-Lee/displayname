package kr.pyke.displayname.network;

import kr.pyke.displayname.network.packet.c2s.C2S_ChangeDisplayName;
import kr.pyke.displayname.network.packet.s2c.S2C_DisplayNameChangeResponse;
import kr.pyke.displayname.network.packet.s2c.S2C_OpenChangeDisplayNameScreen;
import kr.pyke.displayname.network.packet.s2c.S2C_SendBulkDisplayName;
import kr.pyke.displayname.network.packet.s2c.S2C_SendSingleDisplayName;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class DisplayNamePacket {
    private DisplayNamePacket() { }

    public static void registerServer() {
        C2S_ChangeDisplayName.register();
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        S2C_SendSingleDisplayName.register();
        S2C_SendBulkDisplayName.register();
        S2C_DisplayNameChangeResponse.register();
        S2C_OpenChangeDisplayNameScreen.register();
    }
}