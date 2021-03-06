package edu.cse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.Random;

// CommunicationHandler is a handler thread that reads input Images from a Socket and:
//   1. Updates put the Image on the Chat app's input queue
//   2. If it is the Chat server, send the Image to all Clients.

public class CommunicationHandler implements Runnable {
    private InputStream in;
    private ArrayList clientOutputStreams;
    private BufferedReader reader;
    private boolean isServer;
    private Queue inputQueue;
    private Text outputBoi;
    private boolean colorChanged;
    private boolean nextMessageCommand;
    private boolean nextMessageName;
    private boolean isClient;
    private String name;

    public CommunicationHandler(Socket sock, Queue inQueue, ArrayList streams, Text output) {
        //outputBoi = new Text();
        inputQueue = inQueue;
        isServer = true;
        outputBoi = output;
        try {
            in = sock.getInputStream();
            InputStreamReader incomingDataReader = new InputStreamReader(in);
            reader = new BufferedReader(incomingDataReader);
            clientOutputStreams = streams;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("PictureChat CommunicationHandler: Server creation failed");
        }
        colorChanged = false;
        nextMessageCommand = false;
        nextMessageName = false;
        name = "Client";
        isClient = false;
    }

    public CommunicationHandler(Socket sock, Queue inQueue, BufferedReader r, Text output) {
        isServer = false;
        reader = r;
        inputQueue = inQueue;
        outputBoi = output;
        try {
            in = sock.getInputStream();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("PictureChat CommunicationHandler: Client creation failed");
        }
        colorChanged = false;
        nextMessageCommand = false;
        nextMessageName = false;
        name = "Client";
        isClient = true;
    }

    public void run() {
        System.out.println("running -I");
        while (true) {
            System.out.println("running 0");
        try {
// HOW TO READ SIMPLE TEXT FROM SOCKET:
            System.out.println("running I");
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("running II");
                System.out.println("chat CommunicationHandler: read " + message + ".");
                if(message.equals("+//command//+")){
                    System.out.println("next message command");
                    nextMessageCommand = true;
                }
                if(nextMessageCommand) {
                    if (message.equals("color")) {
                        System.out.println("message = color");
                        if (!colorChanged) {
                            System.out.println("change color");
                            outputBoi.setFill(Color.FUCHSIA);
                        } else {
                            System.out.println("change color back");
                            outputBoi.setFill(Color.BLACK);
                        }
                        colorChanged = !colorChanged;
                        nextMessageCommand = false;
                    }
                    if (nextMessageName){
                        name = message;
                        System.out.println("name:"+name);
                        nextMessageCommand = false;
                        nextMessageName = false;
                    }
                    if(message.equals("name")){
                        System.out.println("change name");
                        nextMessageName = true;
                    }
                }else{
                    outputBoi.setText(name + ": " + message + "\n" + outputBoi.getText());
                }
                tellAllClients(message);
            }
            System.out.println("chat CommunicationHandler: read HI.");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("could not read");
        }
    }
    }

    public void tellAllClients(String message) {
        Iterator allClients = clientOutputStreams.iterator();
        while (allClients.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) allClients.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("could not inform clients");
            }
        }
    }
}