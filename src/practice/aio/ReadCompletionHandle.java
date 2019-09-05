package practice.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class ReadCompletionHandle implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel socketChannel;
    public ReadCompletionHandle(AsynchronousSocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        try{
            String body = new String(bytes, "UTF-8");
            System.out.println("Time Server recieve data:" + body);
            doWrite(new Date().toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void doWrite(String currentTime){
        if (currentTime != null){
            byte[] bytes = currentTime.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            socketChannel.write(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if (byteBuffer.hasRemaining()){
                        socketChannel.write(byteBuffer, byteBuffer, this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {

                }
            });
        }
    }
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        exc.printStackTrace();
        try {
            this.socketChannel.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
