package edu.cse;

import javafx.application.Application;
import javafx.stage.Stage;

public class Server extends Application {
    private static Queue inputQueue;
    private static Queue outputQueue;

    @Override
    public void start(final Stage stage) {
        GUI gui = new GUI("Server", inputQueue, outputQueue);
        gui.run(stage);
    }

    public static void main(String[] args) {
        // inputQueue communicates images from Server to GUIUpdater
        inputQueue = new Queue();
        // outputQueue communicates images from GUI to Server
        outputQueue = new Queue();

        // Create a thread that creates a ServerSocket and handles incoming client Sockets
        ServerNetworking serverNetworking = new ServerNetworking(inputQueue, outputQueue);
        Thread serverNetworkingThread = new Thread(serverNetworking);
        serverNetworkingThread.setName("serverNetworkingThread");
        serverNetworkingThread.start();

        // Start the Server's GUI thread
        Application.launch(args);
    }
}

