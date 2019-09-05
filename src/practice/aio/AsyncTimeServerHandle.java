package practice.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeServerHandle implements Runnable{
    private int port;
    AsynchronousServerSocketChannel serverSocketChannel;
    CountDownLatch countDownLatch;

    public AsyncTimeServerHandle(int port){
        this.port = port;
        try{
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("Time Server started");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        doAccept();
        try {
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void doAccept(){
        this.serverSocketChannel.accept(this, new AcceptCompletionHandler());
    }
}
