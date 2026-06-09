package kr.pyke.displayname.mixin.client;

import kr.pyke.displayname.client.cache.DisplayNameCache;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @Inject(method = "extractRenderState*", at = @At("TAIL"))
    private void displayname$applyDisplayName(Avatar entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        if (state.nameTag == null) { return; }

        String displayName = DisplayNameCache.CACHE.get(entity.getUUID());
        if (displayName == null || displayName.isEmpty()) { return; }

        state.nameTag = Component.literal(displayName);
    }
}