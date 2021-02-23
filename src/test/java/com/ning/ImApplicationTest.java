package com.ning;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

class ImApplicationTest {

    public static void main(String[] args) {
        String url = "ws://app-dev-cstest.fzzqxf.com/sloth/ws/c";
        Map<String, String> m = new HashMap<>();
        m.put("id", "123123");
        m.put("token", "456456");

        MyWebSocketClient ws = new MyWebSocketClient(URI.create(url), m);
        ws.connect();
    }
}

class MyWebSocketClient extends WebSocketClient {

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public MyWebSocketClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("onOpen");
    }

    @Override
    public void onMessage(String s) {
        System.out.println("onMessage receive msg=" + s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("onClose");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("onError");
    }
}
