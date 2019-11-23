package com.trantronghien85.websocketdemo;

import okhttp3.WebSocket;
import okio.ByteString;

public interface WebsSocketClientListener {

    void onOpenConnect(WebSocket clientConnect);

    void onReceived(String text);

    void onReceivedByte(ByteString bytes);

    void onClosing(int code);

    void onFailure(String message);
}
