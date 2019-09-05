package practice.aio;

public class TimeServer {
    public static void main(String[] args) {
        int port = 888;
        AsyncTimeServerHandle timeserver = new AsyncTimeServerHandle(port);
        AsyncTimeClientHandle timeClient = new AsyncTimeClientHandle("127.0.0.1", port);
        new Thread(timeserver, "aio-timeserver").start();
        new Thread(timeClient, "aio-timeclient").start();
    }
}
