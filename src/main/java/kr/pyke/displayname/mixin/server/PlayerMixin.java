package kr.pyke.displayname.mixin.server;

import kr.pyke.displayname.client.cache.DisplayNameCache;
import kr.pyke.displayname.data.DisplayNameData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public class PlayerMixin {
    @Redirect(method = "getDisplayName", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getName()Lnet/minecraft/network/chat/Component;"))
    private Component redirectGetName(Player instance) {
        String originalName = instance.getGameProfile().name();
        String displayName = "";

        Level level = instance.level();
        MinecraftServer server = level.getServer();

        if (instance.level().isClientSide()) { displayName = DisplayNameCache.CACHE.get(instance.getUUID()); }
        else if (server != null) {
            DisplayNameData data = DisplayNameData.getServerState(server);
            displayName = data.getDisplayName(instance.getUUID());
        }

        if (null == displayName || displayName.isEmpty()) { return Component.literal(originalName); }

        return Component.literal(displayName);
    }
}
