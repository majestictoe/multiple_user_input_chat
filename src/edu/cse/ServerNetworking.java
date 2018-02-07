package edu.cse;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javafx.scene.text.Text;

public class ServerNetworking implements Runnable {
    private ArrayList clientOutputStreams;
    private int numOfClients = 2;
    private Queue inputQueue;
    private Queue outputQueue;
    private Text ugh;

    public ServerNetworking(Queue inQueue, Queue outQueue, Text text) {
        clientOutputStreams = new ArrayList();
        inputQueue = inQueue;
        outputQueue = outQueue;
        //ugh = new Text();
        ugh = text;
    }

    public void run() {
        // set up server-side networking
        try {
            ServerSocket serverSock = new ServerSocket(5000);
            System.out.println("Server: networking is ready");
            while (true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);

                // for every new client, run an IncomingDataReceiver on a new thread to receive data from it
                CommunicationHandler handler = new CommunicationHandler(clientSocket, inputQueue, clientOutputStreams, ugh);
                Thread handlerThread = new Thread(handler);
                handlerThread.setName("Server communication thread " + numOfClients);
                handlerThread.start();
                System.out.println("server: accepted client connection");
            }
        } catch(Exception ex){
            ex.printStackTrace();
            System.out.println("networking failed :(");
        }

    }
}