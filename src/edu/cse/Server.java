package edu.cse;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class Server extends Application {
    private static Queue inputQueue;
    private static Queue outputQueue;
    private static Text imUpset;

    @Override
    public void start(final Stage stage) {
        GUI gui = new GUI("Server", inputQueue, outputQueue,imUpset);
        gui.run(stage);
    }

    public static void main(String[] args) {
        imUpset = new Text();
        imUpset.setText("test 2");
        // inputQueue communicates images from Server to GUIUpdater
        inputQueue = new Queue();
        // outputQueue communicates images from GUI to Server
        outputQueue = new Queue();

        // Create a thread that creates a ServerSocket and handles incoming client Sockets
        ServerNetworking serverNetworking = new ServerNetworking(inputQueue, outputQueue,imUpset);
        Thread serverNetworkingThread = new Thread(serverNetworking);
        serverNetworkingThread.setName("serverNetworkingThread");
        serverNetworkingThread.start();
        System.out.println("server networking thread started");

        // Start the Server's GUI thread
        Application.launch(args);
    }
}

