package kr.pyke.displayname.data;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import kr.pyke.displayname.DisplayName;
import kr.pyke.displayname.util.Utils;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.*;

public class DisplayNameData extends SavedData {
    private static final String FILE_NAME = "displayname";

    public static final Codec<DisplayNameData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.STRING)
            .fieldOf("displayNames")
            .forGetter(data -> data.displayNames)
    ).apply(instance, DisplayNameData::new));

    public static final SavedDataType<DisplayNameData> TYPE = new SavedDataType<>(
        Identifier.withDefaultNamespace(FILE_NAME),
        DisplayNameData::new,
        CODEC,
        DataFixTypes.SAVED_DATA_MAP_DATA
    );

    private final Map<UUID, String> displayNames = new HashMap<>();
    private final Map<String, UUID> displayNamesReverse = new HashMap<>();

    public DisplayNameData() {
    }

    private DisplayNameData(Map<UUID, String> loaded) {
        loaded.forEach((uuid, displayName) -> {
            displayNames.put(uuid, displayName);
            displayNamesReverse.put(displayName, uuid);
        });
    }

    public static DisplayNameData getServerState(MinecraftServer server) {
        ServerLevel serverLevel = server.overworld();
        return serverLevel.getDataStorage().computeIfAbsent(TYPE);
    }

    public String getDisplayName(UUID uuid) { return displayNames.get(uuid); }

    public Map<UUID, String> getDisplayNames() { return this.displayNames; }

    public String getRealName(String displayName) {
        UUID uuid = displayNamesReverse.get(displayName);
        if (null == uuid) { return displayName; }

        MinecraftServer server = DisplayName.SERVER_INSTANCE;
        if (null == server) { return displayName; }

        ServerPlayer player = server.getPlayerList().getPlayer(uuid);
        if (null != player) { return player.getGameProfile().name(); }

        return displayName;
    }

    public void setDisplayName(UUID uuid, String displayName) {
        String stripDisplayName = Utils.stripColor(displayName);

        if (displayNames.containsKey(uuid)) {
            String oldDisplayName = displayNames.get(uuid);
            displayNamesReverse.remove(oldDisplayName);
        }

        displayNames.put(uuid, stripDisplayName);
        displayNamesReverse.put(stripDisplayName, uuid);

        this.setDirty();
    }
}