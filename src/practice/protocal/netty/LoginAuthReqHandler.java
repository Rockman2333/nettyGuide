package practice.protocal.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildNettyMessage());
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        if (nettyMessage.getHeader() != null && nettyMessage.getHeader().getType() == NetttyMessageType.LOGIN_RESP.value()){
            byte loginResult = (byte)nettyMessage.getBody();
            System.out.println("login is ok:" + loginResult);
            ctx.fireChannelRead(msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }
    private NettyMessage buildNettyMessage(){
        NettyMessage message = new NettyMessage();
        NettyHeader header = new NettyHeader();
        header.setType(NetttyMessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }
}
