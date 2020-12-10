package hulubattle.server;

import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import hulubattle.game.model.CombatLog;

public class WriteHelper {
    private static Gson gson = new Gson();
    public static final String DELIM = "%";

    public static byte[] format(CombatLog log) {
        return (gson.toJson(log) + DELIM).getBytes(StandardCharsets.UTF_8);
    }

    private WriteHelper() {
        // hide
    }
}
