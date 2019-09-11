package practice.protocal.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.util.List;
import java.util.Map;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, NettyMessage nettyMessage, List<Object> list) throws Exception {
        if(nettyMessage == null|nettyMessage.getHeader() == null){
            throw new Exception("the encode message is null");
        }
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(nettyMessage.getHeader().getCrcCoder());
        byteBuf.writeInt(nettyMessage.getHeader().getLength());
        byteBuf.writeLong(nettyMessage.getHeader().getSessionId());
        byteBuf.writeByte(nettyMessage.getHeader().getType());
        byteBuf.writeByte(nettyMessage.getHeader().getPriority());
        byteBuf.writeInt(nettyMessage.getHeader().getAttachment().size());
        if (nettyMessage.getHeader().getAttachment().size() > 0){
            for (Map.Entry<String, Object> item : nettyMessage.getHeader().getAttachment().entrySet()){
                String key = item.getKey();
                byteBuf.writeInt(key.length()).readBytes(key.getBytes("UTF-8"));
                Object value = item.getValue();
                ObjectEncoderOutputStream objos = new ObjectEncoderOutputStream(new ByteBufOutputStream(byteBuf));
                objos.writeObject(value);
            }
        }
        if(nettyMessage.getBody() != null){
            ObjectEncoderOutputStream objos = new ObjectEncoderOutputStream(new ByteBufOutputStream(byteBuf));
            objos.writeObject(nettyMessage.getBody());
        } else {
            byteBuf.writeInt(0);
            byteBuf.setInt(4, byteBuf.readableBytes());
        }
    }
}
