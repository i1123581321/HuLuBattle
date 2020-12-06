package hulubattle.game.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class JsonDataSupplierTest {
    private String characterJson = "[{\"id\":1,\"name\":\"枪兵\",\"hp\":100,\"def\":20,\"mobility\":2,\"skillList\":[1]},{\"id\":2,\"name\":\"骑兵\",\"hp\":100,\"def\":10,\"mobility\":3,\"skillList\":[2]}]";
    private DataSupplier<AbstractCharacterData> characterDataSupplier;

    @Test
    public void testLoadFromString() {
        characterDataSupplier = new JsonDataSupplier<>(SimpleCharacterData.class, characterJson);

        assertTrue(characterDataSupplier.get(1).isPresent());
        assertFalse(characterDataSupplier.get(3).isPresent());

        int[] expected = new int[] { 1 };

        characterDataSupplier.get(1).ifPresent(data -> {
            assertEquals(20, data.getDef());
            assertArrayEquals(expected, data.getSkillList());
        });
    }

    @Test(expected = Test.None.class)
    public void testLoadFromFileNotThrows() throws IOException, URISyntaxException {
        Path characterPath = Paths.get(getClass().getClassLoader().getResource("characters.json").toURI());
        characterDataSupplier = new JsonDataSupplier<>(SimpleCharacterData.class, characterPath);
    }

    @Test(expected = IOException.class)
    public void testLoadFromFileThrows() throws IOException {
        characterDataSupplier = new JsonDataSupplier<>(SimpleCharacterData.class,
                Paths.get("/not_exist_characters.json"));
    }
}
