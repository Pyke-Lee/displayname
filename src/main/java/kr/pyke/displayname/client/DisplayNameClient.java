package kr.pyke.displayname.client;

import kr.pyke.displayname.network.DisplayNamePacket;
import net.fabricmc.api.ClientModInitializer;

public class DisplayNameClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DisplayNamePacket.registerClient();
    }
}
