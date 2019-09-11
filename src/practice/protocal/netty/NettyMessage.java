package practice.protocal.netty;

public final class NettyMessage {
    private NettyHeader header;
    private Object body;

    public NettyHeader getHeader() {
        return header;
    }

    public void setHeader(NettyHeader header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
