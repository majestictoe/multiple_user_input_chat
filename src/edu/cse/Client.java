package edu.cse;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public final class Client extends Application {
    static BufferedReader reader;
    static PrintWriter writer;
    static OutputStream out;
    static Queue inputQueue;
    static Queue outputQueue;
    static Text yeah;

    @Override
    public void start(final Stage stage) {
        GUI gui = new GUI("Client", inputQueue, outputQueue, yeah);
        gui.setClientNetworking(reader, writer, out);
        gui.run(stage);
    }

    public static void main(String[] args) {
        try {
            // Set up client-side networking
            Socket sock = new Socket("127.0.0.1", 5000);
            // reader will be receive images from Server and put them into the SynchronizedQueue
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            // writer will send images to Server as soon as they are gotten from SynchronizedQueue
            out = sock.getOutputStream();
            writer = new PrintWriter(out);

            // Create the queues that will be used for communication
            // inputQueue communicates images from Server to GUIUpdater
            inputQueue = new Queue();
            // outputQueue communicates images from GUI to Server
            outputQueue = new Queue();
            yeah = new Text();
            yeah.setText("test");

            // for every new client, run an IncomingDataReceiver on a new thread to receive data from it
            CommunicationHandler handler = new CommunicationHandler(sock, inputQueue, reader, yeah);
            Thread handlerThread = new Thread(handler);
            handlerThread.start();
            System.out.println("client: created input communication handler");

            // Start the GUI thread
            System.out.println("client: networking is ready... starting the GUI");
            Application.launch(args);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("client: no server available.  Exiting....");
        }

    }
}

