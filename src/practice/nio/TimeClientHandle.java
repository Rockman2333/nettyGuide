package practice.nio;

import javax.imageio.IIOException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class TimeClientHandle implements Runnable {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public TimeClientHandle(String host, int port){
        this.host = host;
        this.port = port;
        try {
            this.selector = Selector.open();
            this.socketChannel = SocketChannel.open();
            this.socketChannel.configureBlocking(false);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host, port))){
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }
    private void doWrite(SocketChannel socketChannel)throws IOException{
        byte[] req = "query time order".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        if(!writeBuffer.hasRemaining()){
            System.out.println("send success");
        }
    }
    @Override
    public void run() {
        try {
            doConnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        while (!stop){
            try {
                selector.select(1000);
                Iterator<SelectionKey> iterator = selector.keys().iterator();
                SelectionKey key = null;
                while (iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e){
                        if (key != null){
                            key.cancel();
                            key.channel().close();
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        if (selector != null){
            try {
                selector.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void handleInput(SelectionKey key) throws IOException{
        if (key.isValid()){
            if (key.isConnectable()){
                if (socketChannel.finishConnect()){
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    doWrite(socketChannel);
                } else {
                    System.exit(1);
                }
            }
            if (key.isReadable()){
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(readBuffer);
                if(readBytes > 0){
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("receive server response:" + body);
                    this.stop = true;
                } else if(readBytes < 0){
                    key.cancel();
                    socketChannel.close();
                }
            }
        }
    }
}
