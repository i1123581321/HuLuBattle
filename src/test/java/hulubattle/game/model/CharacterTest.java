package hulubattle.game.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.BeforeClass;
import org.junit.Test;

import hulubattle.game.data.AbstractCharacterData;
import hulubattle.game.data.DataSupplier;
import hulubattle.game.data.JsonDataSupplier;
import hulubattle.game.data.SimpleCharacterData;
import hulubattle.game.model.AbstractCharacter.CharacterHurtHandler;

public class CharacterTest {
    private static String characterJson = "[{\"id\":1,\"name\":\"枪兵\",\"hp\":100,\"def\":20,\"mobility\":2,\"skillList\":[1]},{\"id\":2,\"name\":\"骑兵\",\"hp\":100,\"def\":10,\"mobility\":3,\"skillList\":[2]}]";
    private static DataSupplier<AbstractCharacterData> supplier = new JsonDataSupplier<>(SimpleCharacterData.class,
            characterJson);
    private static AbstractCharacter c1, c2;

    @BeforeClass
    public static void setUp() {
        c1 = new SimpleCharacter(supplier.get(1).get(), 1, Camp.A);
        c2 = new SimpleCharacter(supplier.get(2).get(), 2, Camp.B);
    }

    @Test
    public void testDistance() {
        c1.moveTo(1, 2);
        c2.moveTo(7, 9);
        assertEquals(13, c1.distance(c2));
        assertEquals(c1.distance(c2), c2.distance(c1));
    }

    @Test
    public void testIsHarm() {
        assertTrue(c1.isHarm(c2));
    }

    @Test
    public void testHurt() {
        CharacterHurtHandler handler = mock(CharacterHurtHandler.class);
        c1.setHandler(handler);
        c1.hurt(40);
        c1.hurt(-20);
        verify(handler, times(2)).handle(1);
        assertEquals(80, c1.getHp());
    }
}
