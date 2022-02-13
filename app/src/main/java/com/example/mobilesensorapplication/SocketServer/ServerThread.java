package com.example.mobilesensorapplication.SocketServer;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/* serverthread method implemented here to activate the server network */
public class ServerThread implements Runnable {
    public static final int SERVER_PORT = 5050;
    private ServerSocket serverSocket;
    Context context;
    Server server;

    public ServerThread(Context context, Server server) {
        this.context = context;
        this.server = server;
    }

    public void run() {
        Socket socket;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);

            //deactivates the visibility of the button
            server.startServerMessaeg();
        } catch (IOException e) {
            e.printStackTrace();
            server.showMessage("Error Starting Server : ");
        }

        //communicates to client and displays error if communication fails
        if (null != serverSocket) {
            while (!Thread.currentThread().isInterrupted()) {
             //   Log.i("Mani", "run: ServerThread");
                try {
                    socket = serverSocket.accept();
                    CommunicationThread commThread = new CommunicationThread(socket, context, server);
                    new Thread(commThread).start();
                } catch (IOException e) {
                    e.printStackTrace();
                    server.showMessage("Error Communicating to Client :");
                }
            }
        }
    }

}
