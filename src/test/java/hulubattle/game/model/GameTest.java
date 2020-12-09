package hulubattle.game.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GameTest {
    private Game game;
    private AutoCloseable closeable;
    private Logger logger = Logger.getLogger("hulubattle.game.model.GameTest");

    @Mock
    private GameDelegate delegate;

    @Captor
    private ArgumentCaptor<ArrayList<CombatLog>> listCaptor;

    @Captor
    private ArgumentCaptor<CombatLog> captor;

    @Before
    public void setup() throws URISyntaxException, IOException {
        closeable = MockitoAnnotations.openMocks(this);
        game = new Game();
        game.setDelegate(delegate);
        game.setUp();
    }

    @After
    public void clean() throws Exception {
        closeable.close();
    }

    @Test(expected = Test.None.class)
    public void testCreate() throws URISyntaxException, IOException {
        new Game();
    }

    @Test
    public void testSetUp() {
        verify(delegate, times(1)).gameDidSetUp(listCaptor.capture());
        verify(delegate, times(1)).gameDidStart(captor.capture(), any(CombatLog.class));

        assertEquals(CombatLog.info("A"), captor.getValue());

        List<CombatLog> list = listCaptor.getValue();
        assertEquals(4, list.size());
        assertEquals("SET", list.get(0).type);
        assertEquals(3, list.get(0).get("x"));
        assertEquals(6, list.get(1).get("y"));
        assertEquals(0, list.get(2).get("data"));
        assertEquals(1, list.get(3).get("camp"));
    }

    @Test
    public void testWrongLogType() {
        game.act(CombatLog.destroy(1));
        verify(delegate, times(1)).gameDidActFail(captor.capture());

        assertEquals(CombatLog.error("日志类型非法"), captor.getValue());
    }

    @Test
    public void testIllegalSrc() {
        game.act(CombatLog.move(4, 3, 5));
        game.act(CombatLog.move(2, 6, 5));
        verify(delegate, times(2)).gameDidActFail(captor.capture());

        checkLog(captor.getAllValues(), 2);
    }

    @Test
    public void testIllegalMove() {
        game.act(CombatLog.move(0, -1, 10));
        game.act(CombatLog.move(0, 3, 6));
        game.act(CombatLog.move(0, 3, 7));

        verify(delegate, times(3)).gameDidActFail(captor.capture());

        checkLog(captor.getAllValues(), 3);
    }

    @Test
    public void testIllegalDest() {
        game.act(CombatLog.cast(0, 4, 0));
        game.act(CombatLog.cast(0, 3, 3));

        verify(delegate, times(2)).gameDidActFail(captor.capture());

        checkLog(captor.getAllValues(), 2);
    }

    @Test
    public void testIllegalCast() {
        game.act(CombatLog.cast(0, 3, 1));
        game.act(CombatLog.cast(0, 1, 0));
        game.act(CombatLog.cast(0, 2, 0));

        verify(delegate, times(3)).gameDidActFail(captor.capture());

        checkLog(captor.getAllValues(), 3);
    }

    @Test
    public void testMove() {
        game.act(CombatLog.move(0, 3, 3));
        verify(delegate, times(1)).gameDidActSucceed(captor.capture());

        assertEquals(CombatLog.move(0, 3, 3), captor.getValue());
    }

    @Test
    public void testCast() {
        game.act(CombatLog.cast(0, 3, 0));
        verify(delegate, times(2)).gameDidActSucceed(captor.capture());

        List<CombatLog> logs = captor.getAllValues();
        assertEquals(CombatLog.cast(0, 3, 0), logs.get(0));
        assertEquals(CombatLog.hurt(3, 50), logs.get(1));
    }

    @Test
    public void testDestroy() {
        game.act(CombatLog.cast(0, 3, 0));
        game.act(CombatLog.skip());
        game.act(CombatLog.cast(0, 3, 0));

        verify(delegate, times(6)).gameDidActSucceed(captor.capture());

        assertEquals(CombatLog.destroy(3), captor.getValue());
    }

    @Test
    public void testEnd() {
        game.act(CombatLog.cast(0, 3, 0));
        game.act(CombatLog.skip());
        game.act(CombatLog.cast(0, 3, 0));
        game.act(CombatLog.skip());
        game.act(CombatLog.move(1, 5, 4));
        game.act(CombatLog.cast(1, 2, 1));
        game.act(CombatLog.skip());
        game.act(CombatLog.cast(1, 2, 1));
        game.act(CombatLog.skip());
        game.act(CombatLog.cast(1, 2, 1));
        game.act(CombatLog.skip());
        game.act(CombatLog.cast(1, 2, 1));
        game.act(CombatLog.skip());

        verify(delegate, times(1)).gameDidEnd(captor.capture(), any(CombatLog.class));

        assertEquals(CombatLog.info("游戏胜利"), captor.getValue());
    }

    private void checkLog(List<CombatLog> logs, int expectedSize) {
        assertEquals(expectedSize, logs.size());
        logs.forEach(l -> assertEquals("ERROR", l.type));
        logs.forEach(l -> logger.info(l.msg));
    }
}
