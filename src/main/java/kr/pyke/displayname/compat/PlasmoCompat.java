package kr.pyke.displayname.compat;

import su.plo.voice.client.ModVoiceClient;

import java.util.UUID;

public class PlasmoCompat {
    private PlasmoCompat() { }

    public static void updateNick(UUID playerID, String displayName) {
        ModVoiceClient client = ModVoiceClient.INSTANCE;
        if (client == null) { return; }

        client.getServerConnection().flatMap(connection -> connection.getPlayerById(playerID)).ifPresent(info -> info.setPlayerNick(displayName));
    }
}
