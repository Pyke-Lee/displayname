package kr.pyke.displayname.network.packet.s2c;

import io.netty.buffer.Unpooled;
import kr.pyke.displayname.DisplayName;
import kr.pyke.displayname.client.cache.DisplayNameCache;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class S2C_SendBulkDisplayName {
    public static final ResourceLocation ID = new ResourceLocation(DisplayName.MOD_ID, "c2s_displayname_bulk");

    public static void send(ServerPlayer player, Map<UUID, String> displayNames) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeMap(displayNames, FriendlyByteBuf::writeUUID, FriendlyByteBuf::writeUtf);
        ServerPlayNetworking.send(player, ID, buf);
    }

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, handler, buf, responseSender) -> {
            Map<UUID, String> displayNames = buf.readMap(HashMap::new, FriendlyByteBuf::readUUID, FriendlyByteBuf::readUtf);
            client.execute(() -> DisplayNameCache.CACHE.putAll(displayNames));
        });
    }
}
