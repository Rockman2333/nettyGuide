package practice.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf firstMessage;
    public TimeClientHandler(){
        byte[] req = "query time order".getBytes();
        this.firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(firstMessage);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        String body = new String(bytes, "UTF-8");
        System.out.println(body);

    }
}
