package kr.pyke.displayname.network.packet.c2s;

import io.netty.buffer.Unpooled;
import kr.pyke.displayname.DisplayName;
import kr.pyke.displayname.data.DisplayNameData;
import kr.pyke.displayname.network.packet.s2c.S2C_DisplayNameChangeResponse;
import kr.pyke.displayname.util.Utils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class C2S_ChangeDisplayName {
    public static final ResourceLocation ID = new ResourceLocation(DisplayName.MOD_ID, "c2s_change_display_name");

    public static void send(String displayName) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(displayName);
        ClientPlayNetworking.send(ID, buf);
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            String newName = buf.readUtf();

            server.execute(() -> {
                if (newName.length() < 2 || newName.length() > 16) {
                    S2C_DisplayNameChangeResponse.send(player, false, "닉네임은 2~16자여야 합니다.");
                    return;
                }

                DisplayNameData data = DisplayNameData.getServerState(server);
                String myCurrentNick = data.getDisplayName(player.getUUID());
                String myEffectiveName = (myCurrentNick != null && !myCurrentNick.isEmpty()) ? myCurrentNick : player.getGameProfile().getName();

                if (newName.equalsIgnoreCase(myEffectiveName)) {
                    S2C_DisplayNameChangeResponse.send(player, false, "현재 사용 중인 이름입니다.");
                    return;
                }

                boolean isDuplicate = false;
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    if (p.getUUID().equals(player.getUUID())) { continue; }

                    String originalName = p.getGameProfile().getName();
                    String customName = data.getDisplayName(p.getUUID());

                    if (newName.equalsIgnoreCase(originalName) || (customName != null && newName.equalsIgnoreCase(customName))) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (isDuplicate) {
                    S2C_DisplayNameChangeResponse.send(player, false, "이미 사용 중인 닉네임입니다.");
                    return;
                }

                if (!player.isCreative()) {
                    ItemStack heldItem = player.getMainHandItem();
                    if (!heldItem.isEmpty()) { heldItem.shrink(1); }
                    else {
                        S2C_DisplayNameChangeResponse.send(player, false, "아이템이 부족합니다.");
                        return;
                    }
                }

                Utils.updateDisplayName(player, newName);
                S2C_DisplayNameChangeResponse.send(player, true, "");
            });
        });
    }
}