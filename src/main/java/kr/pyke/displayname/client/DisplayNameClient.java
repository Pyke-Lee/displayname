package kr.pyke.displayname.client;

import kr.pyke.displayname.client.gui.ChangeDisplayNameScreen;
import kr.pyke.displayname.network.DisplayNamePacket;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;

public class DisplayNameClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DisplayNamePacket.registerClient();
    }

    public static void openChangeDisplayNameScreen() {
        Minecraft.getInstance().setScreen(ChangeDisplayNameScreen.create());
    }
}
