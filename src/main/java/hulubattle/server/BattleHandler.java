package hulubattle.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Future;

import com.google.gson.Gson;

import hulubattle.game.model.CombatLog;
import hulubattle.game.model.Game;
import hulubattle.game.model.GameDelegate;

public class BattleHandler implements Runnable, GameDelegate {
    public static final int BUFFER_SIZE = 1024;
    public static Gson gson = new Gson();

    private AsynchronousSocketChannel socketA;
    private AsynchronousSocketChannel socketB;
    private ByteBuffer bufferA = ByteBuffer.allocate(BUFFER_SIZE);
    private ByteBuffer bufferB = ByteBuffer.allocate(BUFFER_SIZE);

    private Game game;
    private boolean flag = true;

    public BattleHandler(AsynchronousSocketChannel a, AsynchronousSocketChannel b)
            throws URISyntaxException, IOException {
        socketA = a;
        socketB = b;
        game = new Game();
        game.setDelegate(this);
    }

    @Override
    public void run() {
        game.setUp();
        ByteBuffer a = ByteBuffer.allocate(BUFFER_SIZE);
        socketA.read(a, null, new ReadHandler(socketA, a, game));
        ByteBuffer b = ByteBuffer.allocate(BUFFER_SIZE);
        socketB.read(b, null, new ReadHandler(socketB, b, game));

        while (flag && !Thread.currentThread().isInterrupted()) {
            // waiting
        }

        try {
            socketA.close();
            socketB.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gameDidActFail(CombatLog error) {
        send(error);
    }

    @Override
    public void gameDidActSucceed(CombatLog log) {
        send(log);
    }

    @Override
    public void gameDidEnd(CombatLog msgA, CombatLog msgB) {
        send(msgA, msgB);
        flag = false;
    }

    @Override
    public void gameDidSetUp(List<CombatLog> setup) {
        setup.forEach(this::send);
    }

    @Override
    public void gameDidStart(CombatLog msgA, CombatLog msgB) {
        send(msgA, msgB);
    }

    private void send(CombatLog logA, CombatLog logB) {
        byte[] a = gson.toJson(logA).getBytes(StandardCharsets.UTF_8);
        byte[] b = gson.toJson(logB).getBytes(StandardCharsets.UTF_8);
        send(a, bufferA, socketA);
        send(b, bufferB, socketB);
    }

    private void send(CombatLog log) {
        byte[] bytes = gson.toJson(log).getBytes(StandardCharsets.UTF_8);
        send(bytes, bufferA, socketA);
        send(bytes, bufferB, socketB);
    }

    private void send(byte[] bytes, ByteBuffer buffer, AsynchronousSocketChannel socket) {
        buffer.clear();
        buffer.put(bytes);
        buffer.flip();

        Future<Integer> i = socket.write(buffer);
        try {
            i.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
