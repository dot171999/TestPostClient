package com.dot.myapplication;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;


public class Client implements Runnable{

    Activity activity;

    public Client(Activity activity) {
        this.activity = activity;
    }

    static WebSocketClient webSocketClient;

    @Override
    public void run()  {
        try {
            gg();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void gg() throws Exception{
        URI uri=new URI("ws://192.168.178.29:3000");
        webSocketClient=new WebSocketClient(uri,new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println(handshakedata  +"  CONNECTED #############################################");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,"CONNECTED",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onMessage(String message) {
                Log.e("  MESSAGE ",message +"#############################################");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.e("  CLOSE ",reason +"#############################################"+code);
            }

            @Override
            public void onError(Exception ex) {
                Log.e("  EXCEPTION ",ex +"#############################################");
            }
        };
        webSocketClient.connect();
    }
}