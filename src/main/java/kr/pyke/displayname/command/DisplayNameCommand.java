package kr.pyke.displayname.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kr.pyke.PykeLib;
import kr.pyke.displayname.data.DisplayNameData;
import kr.pyke.displayname.network.payload.s2c.S2C_SendSingleDisplayNamePayload;
import kr.pyke.displayname.util.Utils;
import kr.pyke.util.constants.COLOR;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Objects;

public class DisplayNameCommand {
    private DisplayNameCommand() { }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ctx, Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal("이름변경")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("target", EntityArgument.player())
                .then(Commands.argument("displayName", StringArgumentType.greedyString())
                    .executes(DisplayNameCommand::changeDisplayName)
                )
            )
        );
    }

    private static int changeDisplayName(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        MinecraftServer server = context.getSource().getServer();
        ServerPlayer serverPlayer = context.getSource().getPlayer();
        DisplayNameData data = DisplayNameData.getServerState(server);

        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        String displayName = StringArgumentType.getString(context, "displayName");

        data.setDisplayName(target.getUUID(), displayName);
        PykeLib.sendSystemMessage(Objects.requireNonNull(serverPlayer), COLOR.LIME.getColor(), String.format("&7%s&f님의 이름을 &7%s&f(으)로 변경하였습니다.", target.getName().getString(), displayName));

        List<ServerPlayer> serverPlayers = server.getPlayerList().getPlayers();
        S2C_SendSingleDisplayNamePayload packet = new S2C_SendSingleDisplayNamePayload(target.getUUID(), displayName);
        serverPlayers.forEach(player -> ServerPlayNetworking.send(player, packet));
        Utils.refreshTabList(target);

        return 1;
    }
}
