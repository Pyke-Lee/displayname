package kr.pyke.displayname.network.payload.s2c;

import kr.pyke.displayname.DisplayName;
import kr.pyke.displayname.client.gui.ChangeDisplayNameScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record S2C_OpenChangeDisplayNameScreenPayload() implements CustomPacketPayload {
    public static final Type<S2C_OpenChangeDisplayNameScreenPayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(DisplayName.MOD_ID, "s2c_display_name_change_screen_open"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2C_OpenChangeDisplayNameScreenPayload> STREAM_CODEC = StreamCodec.unit(new S2C_OpenChangeDisplayNameScreenPayload());

    @Override public @NotNull Type<? extends CustomPacketPayload> type() { return ID; }

    public static void handle(S2C_OpenChangeDisplayNameScreenPayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> openScreen(context.client()));
    }

    @Environment(EnvType.CLIENT)
    private static void openScreen(Minecraft client) {
        client.setScreen(ChangeDisplayNameScreen.create());
    }
}
