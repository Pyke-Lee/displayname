package kr.pyke.displayname.util;

import kr.pyke.displayname.data.DisplayNameData;
import kr.pyke.displayname.network.payload.s2c.S2C_SendSingleDisplayNamePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.EnumSet;
import java.util.List;

public class Utils {
    private Utils() { }

    public static void refreshTabList(ServerPlayer player) {
        EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions = EnumSet.of(
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
            ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED
        );
        ClientboundPlayerInfoUpdatePacket packet = new ClientboundPlayerInfoUpdatePacket(actions, List.of(player));
        player.level().getServer().getPlayerList().broadcastAll(packet);
    }

    public static String stripColor(String displayName) {
        return displayName.replaceAll("(?i)[&§][0-9A-FK-OR]", "");
    }

    public static void updateDisplayName(ServerPlayer target, String displayName, ServerPlayer sender) {
        updateDisplayName(target, displayName);

        String targetName = target.getGameProfile().name();
        sender.level().getServer().getPlayerList().broadcastSystemMessage(Component.literal(String.format("§6[SYSTEM]§r §7%s§r님의 이름을 §7%s§r(으)로 변경하였습니다.", targetName, displayName)), false);
    }

    public static void updateDisplayName(ServerPlayer target, String displayName) {
        MinecraftServer server = target.level().getServer();

        DisplayNameData data = DisplayNameData.getServerState(server);
        data.setDisplayName(target.getUUID(), displayName);

        S2C_SendSingleDisplayNamePayload packet = new S2C_SendSingleDisplayNamePayload(target.getUUID(), displayName);
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(p, packet);
        }

        refreshTabList(target);

        target.level().getServer().getPlayerList().broadcastSystemMessage(Component.literal(String.format("§6[SYSTEM]§r 이름이 §7%s§r(으)로 변경되었습니다.", displayName)), false);
    }
}
