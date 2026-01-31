package kr.pyke.displayname.network.payload.c2s;

import kr.pyke.displayname.DisplayName;
import kr.pyke.displayname.data.DisplayNameData;
import kr.pyke.displayname.network.payload.s2c.S2C_DisplayNameChangeResponsePayload;
import kr.pyke.displayname.util.Utils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record C2S_ChangeDisplayNamePayload(String displayName) implements CustomPacketPayload {
    public static final Type<C2S_ChangeDisplayNamePayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(DisplayName.MOD_ID, "c2s_change_display_name"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2S_ChangeDisplayNamePayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, C2S_ChangeDisplayNamePayload::displayName,
        C2S_ChangeDisplayNamePayload::new
    );

    @Override public @NotNull Type<? extends CustomPacketPayload> type() { return ID; }

    public static void handle(C2S_ChangeDisplayNamePayload payload, ServerPlayNetworking.Context context) {
        ServerPlayer serverPlayer = context.player();
        String newName = payload.displayName();
        MinecraftServer server = context.server();

        context.server().execute(() -> {
            if (newName.length() < 2 || newName.length() > 16) {
                ServerPlayNetworking.send(serverPlayer, new S2C_DisplayNameChangeResponsePayload(false, "닉네임은 2~16자여야 합니다."));
                return;
            }

            DisplayNameData data = DisplayNameData.getServerState(server);
            String myCurrentNick = data.getDisplayName(serverPlayer.getUUID());
            String myEffectiveName = (myCurrentNick != null && !myCurrentNick.isEmpty()) ? myCurrentNick : serverPlayer.getGameProfile().getName();

            if (newName.equalsIgnoreCase(myEffectiveName)) {
                ServerPlayNetworking.send(serverPlayer, new S2C_DisplayNameChangeResponsePayload(false, "현재 사용 중인 이름입니다."));
                return;
            }

            boolean isDuplicate = false;
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                if (player.getUUID().equals(serverPlayer.getUUID())) { continue; }

                String originalName = player.getGameProfile().getName();
                String customName = data.getDisplayName(player.getUUID());

                if (newName.equalsIgnoreCase(originalName) || (customName != null && newName.equalsIgnoreCase(customName))) {
                    isDuplicate = true;
                    break;
                }
            }

            if (isDuplicate) {
                ServerPlayNetworking.send(serverPlayer, new S2C_DisplayNameChangeResponsePayload(false, "이미 사용 중인 닉네임입니다."));
                return;
            }

            if (!serverPlayer.isCreative()) {
                ItemStack heldItem = serverPlayer.getMainHandItem();
                if (!heldItem.isEmpty()) { heldItem.shrink(1); }
                else {
                    ServerPlayNetworking.send(serverPlayer, new S2C_DisplayNameChangeResponsePayload(false, "아이템이 부족합니다."));
                    return;
                }
            }

            Utils.updateDisplayName(serverPlayer, newName);
            ServerPlayNetworking.send(serverPlayer, new S2C_DisplayNameChangeResponsePayload(true, ""));
        });
    }
}