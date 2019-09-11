package practice.protocal.netty;

public enum NetttyMessageType {
    LOGIN_REQ(0),
    LOGIN_RESP(1);
    private byte type;

    NetttyMessageType(int type){
        this.type = (byte) type;
    }
    public byte value(){
        return this.type;
    }
}
