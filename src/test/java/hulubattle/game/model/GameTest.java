package hulubattle.game.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import net.bytebuddy.asm.Advice.Argument;

public class GameTest {
    private Game game;

    @Mock
    private GameDelegate delegate;

    @Captor
    private ArgumentCaptor<ArrayList<CombatLog>> captor;

    @Before
    public void setup() throws URISyntaxException, IOException {
        MockitoAnnotations.openMocks(this);
        game = new Game();
        game.setDelegate(delegate);
    }

    @Test(expected = Test.None.class)
    public void testCreate() throws URISyntaxException, IOException {
        new Game();
    }

    @Test
    public void testSetUp() {
        game.setUp();

        verify(delegate, times(1)).gameDidSetUp(captor.capture());

        List<CombatLog> list = captor.getValue();
        assertEquals(4, list.size());
        assertEquals("SET", list.get(0).type);
        assertEquals(3, list.get(0).get("x"));
        assertEquals(6, list.get(1).get("y"));
        assertEquals(0, list.get(2).get("data"));
        assertEquals(1, list.get(3).get("camp"));
    }
}
