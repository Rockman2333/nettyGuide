package practice.nio;

import practice.bio.TimeServerHandle;

public class TimeServer {
    public static void main(String[] args) {
        int port = 888;
        new Thread(new MultiplexerTimeServer(port)).start();
        new Thread(new TimeClientHandle("127.0.0.1", port)).start();
    }
}
