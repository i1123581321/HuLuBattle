package hulubattle.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

import hulubattle.game.model.CombatLog;
import hulubattle.game.model.Game;

class ServerReadHandler implements CompletionHandler<Integer, Void> {
    private AsynchronousSocketChannel socket;
    private ByteBuffer buffer;
    private Game game;

    public ServerReadHandler(AsynchronousSocketChannel socket, ByteBuffer buffer, Game game) {
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
        byte[] contexts = new byte[BattleTask.BUFFER_SIZE];
        buffer.get(contexts, 0, result);
        buffer.clear();
        String str = new String(contexts, 0, result, StandardCharsets.UTF_8);
        CombatLog log = BattleTask.gson.fromJson(str, CombatLog.class);
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