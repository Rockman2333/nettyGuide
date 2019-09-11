package practice.protocal.netty;

import java.util.HashMap;
import java.util.Map;

public final class NettyHeader {
    private int crcCoder = 0xabef0101;
    private int length;//消息长度
    private long sessionId;
    private byte type;
    private byte priority;
    private Map<String ,Object> attachment = new HashMap<>();

    public int getCrcCoder() {
        return crcCoder;
    }

    public void setCrcCoder(int crcCoder) {
        this.crcCoder = crcCoder;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "NettyHeader{" +
                "crcCoder=" + crcCoder +
                ", length=" + length +
                ", type=" + type +
                ", sessionId=" + sessionId +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}
