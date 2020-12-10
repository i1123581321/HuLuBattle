package hulubattle.client.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hulubattle.game.model.CombatLog;
import hulubattle.game.model.LogConsumer;
import hulubattle.server.BattleTask;
import hulubattle.server.GameServer;
import hulubattle.server.WriteHelper;

public class GameClient implements AutoCloseable {
    private ExecutorService groupThreadPool = Executors.newCachedThreadPool();
    private AsynchronousChannelGroup group;
    private LogConsumer consumer;
    private ByteBuffer buffer = ByteBuffer.allocate(BattleTask.BUFFER_SIZE);
    private final AsynchronousSocketChannel clientSocket;

    public GameClient(LogConsumer consumer) throws IOException {
        this.consumer = consumer;
        group = AsynchronousChannelGroup.withThreadPool(groupThreadPool);
        clientSocket = AsynchronousSocketChannel.open(group);
    }

    public void connect() {
        clientSocket.connect(GameServer.SERVER_ADDR, clientSocket, new ClientConnectHandler(consumer));
    }

    public void write(CombatLog log){
        byte[] bytes = WriteHelper.format(log);
        buffer.clear();
        buffer.put(bytes);
        buffer.flip();
        Future<Integer> i = clientSocket.write(buffer);
        try{
            i.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        clientSocket.close();
    }
}
