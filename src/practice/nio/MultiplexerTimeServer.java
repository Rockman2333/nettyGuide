package practice.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;

    public MultiplexerTimeServer(int port){
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("time server is start in port:" + port);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    try{
                        handleInput(key);
                    } catch (Exception e){
                        key.cancel();
                        if (key.channel() != null){
                            key.channel().close();
                        }
                    }
                }
            } catch (Exception e){

            }
        }
        if (selector != null){
            try {
                selector.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    private void handleInput(SelectionKey key) throws IOException{
        if (key.isValid()){
            if (key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.register(this.selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()){
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("timeServer receive data:" + body);
                    doWrite(sc,new Date().toString());
                } else {
                    key.cancel();
                    sc.close();
                }
            }
        }

    }
    private void doWrite(SocketChannel channel, String response) throws IOException{
        if (response != null){
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(writeBuffer);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
