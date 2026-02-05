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

public class S2C_DisplayNameChangeResponse {
    public static final ResourceLocation ID = new ResourceLocation(DisplayName.MOD_ID, "s2c_display_name_change_response");

    public static void send(ServerPlayer player, boolean success, String message) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBoolean(success);
        buf.writeUtf(message);
        ServerPlayNetworking.send(player, ID, buf);
    }

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, handler, buf, responseSender) -> {
            boolean success = buf.readBoolean();
            String message = buf.readUtf();

            client.execute(() -> ChangeDisplayNameScreen.handleResponse(success, message));
        });
    }
}