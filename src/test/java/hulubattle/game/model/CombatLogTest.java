package hulubattle.game.model;

import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;

import org.junit.Test;

public class CombatLogTest {
    private static Gson gson = new Gson();
    private static String logJson = "{\"type\":\"SET\",\"src\":1,\"dest\":-1,\"x\":3,\"y\":4,\"skill\":-1,\"msg\":\"\"}";

    @Test
    public void testSerialize() {
        CombatLog log = CombatLog.set(1, 3, 4);
        String str = gson.toJson(log);
        assertEquals(logJson, str);
    }

    @Test
    public void testDeserialize() {
        CombatLog log = gson.fromJson(logJson, CombatLog.class);
        assertEquals("SET", log.type);
        assertEquals(4, log.y);
    }
}
