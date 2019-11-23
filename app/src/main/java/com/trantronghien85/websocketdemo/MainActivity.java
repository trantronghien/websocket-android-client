package com.trantronghien85.websocketdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;

public class MainActivity extends AppCompatActivity implements WebsSocketClientListener {

    private Button btnSend;
    private Button btnReconnect;
    private Button btnClearMessage;
    private Button btnDisconnect;
    private EditText edtMessage;
    private TextView txtStatus;
    private TextView txtContent;
    private OkHttpClient client;
    private static final String URL = "ws://websocketdemo.herokuapp.com";

    private WebSocket ws;
    private final String TAG = "hien-test";
    private StringBuilder  message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = new StringBuilder();
        btnSend = findViewById(R.id.btnSend);
        btnReconnect = findViewById(R.id.btnReconnect);
        btnClearMessage = findViewById(R.id.btnClearMessage);
        btnDisconnect = findViewById(R.id.btnDisconnect);
        edtMessage = findViewById(R.id.txtMessage);
        txtContent = findViewById(R.id.txtContent);
        txtStatus = findViewById(R.id.txtStatus);
        client = new OkHttpClient.Builder().build();
        btnReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectClient();

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        btnClearMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setLength(0);
                txtContent.setText(message.toString());
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shutdown();
                changeStatus(false);
            }
        });

        connectClient();
    }

    private void connectClient() {
        Request request = new Request.Builder()
                .url(URL)
                .build();
        EchoWebSocketListener listener = new EchoWebSocketListener(this);
        ws = client.newWebSocket(request, listener);
    }

    private void sendMessage() {
        String textClientSend = edtMessage.getText().toString();
        message.append(textClientSend);
        message.append("\n\n");
        ws.send(textClientSend);
        txtContent.setText(message.toString());
        edtMessage.setText("");
    }

    private void shutdown() {
        client.dispatcher().executorService().shutdown();
        ws.close(1000 , "close android client");
    }

    @Override
    public void onOpenConnect(WebSocket client) {
        ws = client;
        changeStatus(true);
        Log.d(TAG, "onOpenConnect: ");
    }

    @Override
    public void onReceived(String text) {
        message.append(text);
        message.append("\n\n");
        setTextMessage();
    }

    private void setTextMessage() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtContent.setText(message.toString());
            }
        });
    }

    @Override
    public void onReceivedByte(ByteString bytes) {
        Log.d(TAG, "onReceivedByte: " + bytes.utf8());
    }

    @Override
    public void onClosing(int code) {
        Log.d(TAG, "onClosing: " + code);
        changeStatus(false);
        showErrorMessage("Close connect");
    }

    @Override
    public void onFailure(String message) {
        Log.d(TAG, "onFailure: " + message);
        showErrorMessage(message);
        changeStatus(false);
    }

    private void showErrorMessage(final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeStatus(final boolean connenct) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!connenct) {
                    txtStatus.setText("Chưa kết nối");
                    txtStatus.setTextColor(Color.RED);
                } else {
                    txtStatus.setText("Đã kết nối");
                    txtStatus.setTextColor(Color.BLUE);
                }
            }
        });
    }
}
