package practice.protocal.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {
    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();
    private String whiteCheckList = "127.0.0.1";
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == NetttyMessageType.LOGIN_REQ.value()){
            String nodeIndex = ctx.channel().remoteAddress().toString();
        } else {
            ctx.fireChannelRead(msg);
        }
    }
    private NettyMessage buildResponse(byte result){
        NettyMessage message = new NettyMessage();
        NettyHeader header = new NettyHeader();
        header.setType(NetttyMessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return  message;
    }
}
