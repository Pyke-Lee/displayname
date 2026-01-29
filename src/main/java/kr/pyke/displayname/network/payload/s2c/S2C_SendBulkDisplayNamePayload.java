package kr.pyke.displayname.network.payload.s2c;

import kr.pyke.displayname.DisplayName;
import kr.pyke.displayname.client.cache.DisplayNameCache;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record S2C_SendBulkDisplayNamePayload(Map<UUID, String> displayNames) implements CustomPacketPayload {
    public static final Type<S2C_SendBulkDisplayNamePayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(DisplayName.MOD_ID, "c2s_displayname_bulk"));

    @Override public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() { return ID; }

    public static final StreamCodec<RegistryFriendlyByteBuf, S2C_SendBulkDisplayNamePayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.map(HashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.STRING_UTF8), S2C_SendBulkDisplayNamePayload::displayNames,
        S2C_SendBulkDisplayNamePayload::new
    );

    public static void handle(S2C_SendBulkDisplayNamePayload payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> DisplayNameCache.CACHE.putAll(payload.displayNames()));
    }
}
