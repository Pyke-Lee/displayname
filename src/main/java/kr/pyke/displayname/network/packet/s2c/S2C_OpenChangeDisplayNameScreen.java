package kr.pyke.displayname.network.packet.s2c;

import io.netty.buffer.Unpooled;
import kr.pyke.displayname.DisplayName;
import kr.pyke.displayname.client.gui.ChangeDisplayNameScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class S2C_OpenChangeDisplayNameScreen {
    public static final ResourceLocation ID = new ResourceLocation(DisplayName.MOD_ID, "s2c_display_name_change_screen_open");

    public static void send(ServerPlayer player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        ServerPlayNetworking.send(player, ID, buf);
    }

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, handler, buf, responseSender) -> {
            client.execute(() -> client.setScreen(ChangeDisplayNameScreen.create()));
        });
    }
}
