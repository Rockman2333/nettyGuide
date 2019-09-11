package practice.protocal.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;

import java.util.HashMap;
import java.util.Map;

public final class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf)super.decode(ctx, in);
        if (frame == null){
            return  null;
        }
        NettyMessage nettyMessage = new NettyMessage();
        NettyHeader header = new NettyHeader();
        header.setCrcCoder(in.readInt());
        header.setLength(in.readInt());
        header.setSessionId(in.readLong());
        header.setType(in.readByte());
        header.setPriority(in.readByte());
        Map<String, Object> attachment = new HashMap<>();
        int attachmentSize = in.readInt();
        if (attachmentSize > 0){
            for (int i = 0 ;i < attachmentSize; i++){
                int keySize = in.readInt();
                byte[] keyAarray = new byte[keySize];
                in.readBytes(keyAarray);
                String key = new String(keyAarray, "UTF-8");
                ObjectDecoderInputStream odis = new ObjectDecoderInputStream(new ByteBufInputStream(in));
                Object value = odis.readObject();
                attachment.put(key, value);
            }
        }
        header.setAttachment(attachment);
        if(in.readableBytes() > 4){
            ObjectDecoderInputStream odis = new ObjectDecoderInputStream(new ByteBufInputStream(in));
            Object body = odis.readObject();
            nettyMessage.setBody(body);
        }
        return nettyMessage;
    }
}
