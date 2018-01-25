package edu.cse;
import java.io.PrintWriter;
import java.io.OutputStream;

public class TextSender implements Runnable {
    private Queue outputQueue;
    private PrintWriter writer;
    private OutputStream out;

    TextSender(Queue queue, PrintWriter w, OutputStream stream) {
        outputQueue = queue;
        writer = w;
        out = stream;
    }

    public void run() {
        while (true) {
            // Ask queue for an image to send
            String textToSend = "nothing yet";
            while (outputQueue.getValues()!=null) {
                while (outputQueue.getValues()[0] == null) {
                    Thread.currentThread().yield();
                    textToSend = outputQueue.getValues()[0];
                }
            }
            try {
//                 HOW TO WRITE SIMPLE TEXT TO SOCKET:
                writer.println(textToSend);
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("unable to send text");
            }
        }
    }

}