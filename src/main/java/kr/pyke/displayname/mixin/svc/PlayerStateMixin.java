package kr.pyke.displayname.mixin.svc;

import de.maxhenkel.voicechat.voice.common.PlayerState;
import kr.pyke.displayname.client.cache.DisplayNameCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Pseudo
@Mixin(PlayerState.class)
public abstract class PlayerStateMixin {
    @Inject(method = "getName", at = @At("RETURN"), cancellable = true, remap = false)
    private void displayname$applyDisplayName(CallbackInfoReturnable<String> cir) {
        UUID uuid = ((PlayerState) (Object) this).getUuid();
        if (uuid == null) { return; }

        String displayName = DisplayNameCache.CACHE.get(uuid);
        if (displayName != null && !displayName.isEmpty()) {
            cir.setReturnValue(displayName);
        }
    }
}