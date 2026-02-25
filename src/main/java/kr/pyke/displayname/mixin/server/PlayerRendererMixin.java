package kr.pyke.displayname.mixin.server;

import kr.pyke.displayname.client.cache.DisplayNameCache;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @ModifyVariable(
        method = "renderNameTag(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At("HEAD"),
        argsOnly = true
    )
    private Component modifyDisplayName(Component name, AbstractClientPlayer player) {
        String displayName = DisplayNameCache.CACHE.get(player.getUUID());

        if (displayName == null || displayName.isEmpty()) { return name; }

        return Component.literal(displayName);
    }
}
