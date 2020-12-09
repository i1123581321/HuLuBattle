package hulubattle.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

import hulubattle.game.model.CombatLog;
import hulubattle.game.model.Game;

class ReadHandler implements CompletionHandler<Integer, Void> {
    private AsynchronousSocketChannel socket;
    private ByteBuffer buffer;
    private Game game;

    public ReadHandler(AsynchronousSocketChannel socket, ByteBuffer buffer, Game game) {
        this.socket = socket;
        this.buffer = buffer;
        this.game = game;
    }

    @Override
    public void completed(Integer result, Void attachment) {
        if (result == -1) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        buffer.flip();
        byte[] contexts = new byte[BattleHandler.BUFFER_SIZE];
        buffer.get(contexts, 0, result);
        buffer.clear();
        String str = new String(contexts, StandardCharsets.UTF_8);
        CombatLog log = BattleHandler.gson.fromJson(str, CombatLog.class);
        game.act(log);
        socket.read(buffer, null, this);
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}