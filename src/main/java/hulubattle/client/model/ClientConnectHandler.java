package hulubattle.client.model;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import hulubattle.game.model.LogConsumer;
import hulubattle.server.BattleTask;
import hulubattle.server.ReadHandler;

public class ClientConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {
    private LogConsumer consumer;

    public ClientConnectHandler(LogConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void completed(Void result, AsynchronousSocketChannel attachment) {
        ByteBuffer buffer = ByteBuffer.allocate(BattleTask.BUFFER_SIZE);
        ReadHandler handler = new ReadHandler(attachment, buffer);
        handler.setConsumer(consumer);
        attachment.read(buffer, null, handler);
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
        // do-nothing
    }
}
