package kr.pyke.displayname.mixin.plasmo;

import com.google.common.io.ByteArrayDataInput;
import kr.pyke.displayname.client.cache.DisplayNameCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.plo.voice.proto.data.player.VoicePlayerInfo;

import java.util.UUID;

@Pseudo
@Mixin(value = VoicePlayerInfo.class, remap = false)
public abstract class VoicePlayerInfoMixin {
    @Inject(method = "deserialize", at = @At("TAIL"), remap = false)
    private void displayname$applyDisplayName(ByteArrayDataInput in, CallbackInfo ci) {
        UUID playerID = ((VoicePlayerInfo) (Object) this).getPlayerId();
        if (playerID == null) { return; }

        String displayName = DisplayNameCache.CACHE.get(playerID);
        if (displayName != null && !displayName.isEmpty()) {
            ((VoicePlayerInfo) (Object) this).setPlayerNick(displayName);
        }
    }
}