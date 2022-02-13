package com.example.mobilesensorapplication.SocketServer;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Server {
    Context context;
    ServerMessageListener serverMessageListener;

    public Socket tempClientSocket;
    String returnString;

    //here it sets the Thread initially to null
    Thread serverThread = null;

    private Handler handler = new Handler();

    public Server(Context context, ServerMessageListener serverMessageListener) {
        this.context = context;
        this.serverMessageListener = serverMessageListener;
    }


    //showMessage method to handle posting of mesage to the textView
    public void showMessage(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                serverMessageListener.message(message);
            }
        });
    }

    public void startServer() {

        //this initiates the serverthread defined later and starts the thread
        this.serverThread = new Thread(new ServerThread(context, this));
        this.serverThread.start();
    }

    public void startServerMessaeg() {
        showMessage("Server Started.");
    }

    public void sendData(String message) {

        if (message.length() > 0) {
            sendMessage(message);
        }
    }


    //method implemented to send message to the client
    private void sendMessage(final String message) {
        try {
            if (null != tempClientSocket) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PrintWriter out = null;
                        try {
                            Log.i("Test", "run: "+message);
                            out = new PrintWriter(new BufferedWriter(
                                    new OutputStreamWriter(tempClientSocket.getOutputStream())),
                                    true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        out.println(message);
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        if (null != serverThread) {
            sendMessage("Disconnect");
            serverThread.interrupt();
            serverThread = null;
        }
    }
    public String Decrypt(String text){
        returnString = text;

        try {
            String key = "Bar12345Bar12345"; // 128 bit key
            // Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(text.getBytes()));
            returnString = decrypted;
            System.out.println("Decrypted Message: "+ decrypted);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return returnString;
    }

}
