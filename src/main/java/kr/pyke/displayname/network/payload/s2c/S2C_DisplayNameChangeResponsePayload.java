package kr.pyke.displayname.network.payload.s2c;

import kr.pyke.displayname.DisplayName;
import kr.pyke.displayname.client.gui.ChangeDisplayNameScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record S2C_DisplayNameChangeResponsePayload(boolean success, String message) implements CustomPacketPayload {
    public static final Type<S2C_DisplayNameChangeResponsePayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(DisplayName.MOD_ID, "s2c_display_name_change_response"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2C_DisplayNameChangeResponsePayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL, S2C_DisplayNameChangeResponsePayload::success,
        ByteBufCodecs.STRING_UTF8, S2C_DisplayNameChangeResponsePayload::message,
        S2C_DisplayNameChangeResponsePayload::new
    );

    @Override public @NotNull Type<? extends CustomPacketPayload> type() { return ID; }

    public static void handle(S2C_DisplayNameChangeResponsePayload payload, ClientPlayNetworking.Context context) {
        boolean success = payload.success();
        String message = payload.message();

        context.client().execute(() -> ChangeDisplayNameScreen.handleResponse(success, message));
    }
}
