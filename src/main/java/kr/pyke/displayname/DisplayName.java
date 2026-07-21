package kr.pyke.displayname;

import kr.pyke.displayname.command.DisplayNameCommand;
import kr.pyke.displayname.data.DisplayNameData;
import kr.pyke.displayname.network.DisplayNamePacket;
import kr.pyke.displayname.network.payload.s2c.S2C_SendBulkDisplayNamePayload;
import kr.pyke.displayname.type.MESSAGE_TYPE;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DisplayName implements ModInitializer {
	public static final String MOD_ID = "displayname";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static MinecraftServer SERVER_INSTANCE;

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTING.register(server -> SERVER_INSTANCE = server);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> SERVER_INSTANCE = null);

		DisplayNamePacket.registerCodec();
		DisplayNamePacket.registerServer();

		CommandRegistrationCallback.EVENT.register(DisplayNameCommand::register);

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> ServerPlayNetworking.send(handler.getPlayer(), new S2C_SendBulkDisplayNamePayload(DisplayNameData.getServerState(Objects.requireNonNull(server)).getDisplayNames())));
	}

	public static void sendMessage(ServerPlayer player, MESSAGE_TYPE type, String message) {
		Component component = Component.literal("§6[SYSTEM]§r ").append(message);

		switch (type) {
			case MESSAGE_TYPE.PERSONAL -> player.sendSystemMessage(component);
			case MESSAGE_TYPE.SERVER -> {
				MinecraftServer server = player.level().getServer();
				if (server == null) { return; }

				server.getPlayerList().broadcastSystemMessage(component, false);
			}
			case MESSAGE_TYPE.BROADCAST -> {
				MinecraftServer server = player.level().getServer();
				if (server == null) { return; }

				PlayerList playerList = server.getPlayerList();

				playerList.broadcastSystemMessage(Component.empty(), false);
				playerList.broadcastSystemMessage(component, false);
				playerList.broadcastSystemMessage(Component.empty(), false);
			}
		}
	}
}