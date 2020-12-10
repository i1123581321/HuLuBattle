package hulubattle.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameServer implements AutoCloseable {
    private ExecutorService groupThreadPool = Executors.newCachedThreadPool();
    private ExecutorService battleServicePool = Executors.newCachedThreadPool();
    private AsynchronousChannelGroup group;
    private final AsynchronousServerSocketChannel serverSocket;

    public GameServer() throws IOException {
        group = AsynchronousChannelGroup.withThreadPool(groupThreadPool);
        serverSocket = AsynchronousServerSocketChannel.open(group);
        serverSocket.bind(new InetSocketAddress("127.0.0.1", 10080));
    }

    public void accept() {
        serverSocket.accept(null, new ServerAcceptHandler(serverSocket, battleServicePool));
    }

    @Override
    public void close() throws Exception {
        battleServicePool.shutdown();
        battleServicePool.awaitTermination(2, TimeUnit.SECONDS);
        serverSocket.close();
    }
}
