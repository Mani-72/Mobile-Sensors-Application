package com.example.mobilesensorapplication.SocketServer;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/* communicationThread class that implements the runnable class to communicate with the client */
public class CommunicationThread implements Runnable {

    private Socket clientSocket;

    private BufferedReader input;

    Context context;
    Server server;

    public CommunicationThread(Socket clientSocket, Context context, Server server) {
        this.context = context;
        this.server = server;
        this.clientSocket = clientSocket;
        server.tempClientSocket = clientSocket;
        try {
            this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            server.showMessage("Error Connecting to Client!!");
        }
        server.showMessage("Connected to Client!!");
    }

    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
        //    Log.i("Mani", "run: CommunicationThread");
            try {

                //checks to see if the client is still connected and displays disconnected if disconnected
                String read = input.readLine();
                if (null == read || "Disconnect".contentEquals(read)) {
                    Log.i("Test", "read: "+read);
                    Thread.interrupted();
                    read = "Offline....";
                    server.showMessage("Client : " + read);
                    break;
                }
                server.showMessage("Client : " + read);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
