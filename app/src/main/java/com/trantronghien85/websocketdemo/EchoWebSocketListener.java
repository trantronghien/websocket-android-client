package com.trantronghien85.websocketdemo;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class EchoWebSocketListener extends WebSocketListener {
    private WebsSocketClientListener listener;

    public EchoWebSocketListener(WebsSocketClientListener listener){
        this.listener = listener;
    }
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
//        webSocket.send("Hello, it's SSaurel !");
//        webSocket.send("What's up ?");
//        webSocket.send(ByteString.decodeHex("deadbeef"));
//        webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        listener.onOpenConnect(webSocket);
    }
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        if (listener != null){
            listener.onReceived(text);
        }
//        output("Receiving : " + text);
    }
    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
//        output("Receiving bytes : " + bytes.hex());
        if (listener != null){
            listener.onReceivedByte(bytes);
        }
    }
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        if (listener != null){
            listener.onClosing(code);
        }
//        output("Closing : " + code + " / " + reason);
    }
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//        output("Error : " + t.getMessage());
        if (listener != null){
            listener.onFailure(t.getMessage());
        }
    }
}
