package kr.pyke.displayname;

import kr.pyke.displayname.command.DisplayNameCommand;
import kr.pyke.displayname.data.DisplayNameData;
import kr.pyke.displayname.network.DisplayNamePacket;
import kr.pyke.displayname.network.packet.s2c.S2C_SendBulkDisplayName;
import kr.pyke.displayname.registry.ModRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisplayName implements ModInitializer {
	public static final String MOD_ID = "displayname";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static MinecraftServer SERVER_INSTANCE;

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTING.register(server -> SERVER_INSTANCE = server);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> SERVER_INSTANCE = null);

		DisplayNamePacket.registerServer();

		CommandRegistrationCallback.EVENT.register(DisplayNameCommand::register);

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> S2C_SendBulkDisplayName.send(handler.getPlayer(), DisplayNameData.getServerState(server).getDisplayNames()));

		ModRegistry.register();
	}
}