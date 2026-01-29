package kr.pyke.displayname.mixin.server;

import kr.pyke.displayname.data.DisplayNameData;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;

    @Redirect(method = "broadcastChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/ChatType;bind(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/network/chat/ChatType$Bound;"))
    private ChatType.Bound injectDisplayNameToChat(ResourceKey<ChatType> resourceKey, Entity entity) {
        DisplayNameData data = DisplayNameData.getServerState(Objects.requireNonNull(this.player.getServer()));
        String displayName = data.getDisplayName(this.player.getUUID());

        if (displayName != null && !displayName.isEmpty()) {
            return ChatType.bind(resourceKey, this.player.registryAccess(), Component.literal(displayName));
        }

        return ChatType.bind(resourceKey, entity);
    }
}
