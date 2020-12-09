package hulubattle.server;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private Logger logger = Logger.getLogger("hulubattle.server.AcceptHandler");
    private AsynchronousServerSocketChannel server;
    private Set<AsynchronousSocketChannel> channels = new HashSet<>();
    private ExecutorService battleServicePool;

    public AcceptHandler(AsynchronousServerSocketChannel server, ExecutorService battleServicePool) {
        this.server = server;
        this.battleServicePool = battleServicePool;
    }

    @Override
    public void completed(AsynchronousSocketChannel result, Void attachment) {
        channels.add(result);
        if (channels.size() == 2) {
            Iterator<AsynchronousSocketChannel> it = channels.iterator();
            AsynchronousSocketChannel a = it.next();
            AsynchronousSocketChannel b = it.next();
            try {
                BattleHandler handler = new BattleHandler(a, b);
                battleServicePool.submit(handler);
            } catch (Exception e) {
                logger.info("Create battle handler failed");
                try {
                    a.close();
                    b.close();
                } catch (IOException f) {
                    logger.info("Close channels failed");
                }
            }
            channels.clear();
        }
        server.accept(attachment, this);
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        logger.info("Accept failed");
    }
}
