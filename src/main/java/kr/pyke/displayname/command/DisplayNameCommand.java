package kr.pyke.displayname.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kr.pyke.displayname.network.packet.s2c.S2C_OpenChangeDisplayNameScreen;
import kr.pyke.displayname.util.Utils;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class DisplayNameCommand {
    private DisplayNameCommand() { }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ctx, Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal("이름변경")
            .requires(source -> source.hasPermission(2))
            .then(Commands.argument("target", EntityArgument.player())
                .executes(DisplayNameCommand::openScreenChangeDisplayName)

                .then(Commands.argument("displayName", StringArgumentType.greedyString())
                    .executes(DisplayNameCommand::changeDisplayName)
                )
            )
        );
    }

    private static int changeDisplayName(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer serverPlayer = context.getSource().getPlayer();
        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        String displayName = StringArgumentType.getString(context, "displayName");

        Utils.updateDisplayName(target, displayName, serverPlayer);

        return 1;
    }

    private static int openScreenChangeDisplayName(CommandContext<CommandSourceStack> context) {
        ServerPlayer serverPlayer = context.getSource().getPlayer();

        S2C_OpenChangeDisplayNameScreen.send(Objects.requireNonNull(serverPlayer));

        return 1;
    }
}
