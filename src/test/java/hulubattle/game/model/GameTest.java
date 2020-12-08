package hulubattle.game.model;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class GameTest {
    @Test(expected = Test.None.class)
    public void testCreate() throws URISyntaxException, IOException {
        Game game = new Game();
        game.hashCode();
    }
}
