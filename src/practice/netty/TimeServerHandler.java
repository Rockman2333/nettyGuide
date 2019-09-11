package practice.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.util.Date;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("the time server receive data:" + body);
        ByteBuf res = Unpooled.copiedBuffer(new Date().toString().getBytes());
        ctx.writeAndFlush(res);
    }

}
