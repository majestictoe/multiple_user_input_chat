package edu.cse;
import javafx.scene.text.Text;

public class Getter implements Runnable{
    String[] values;
    Queue queue;
    private Text outputBoi;
    Getter(Queue q, Text textYes){
        queue = q;
        outputBoi = textYes;
    }
    public void run(){
        Thread.currentThread().setName("GETTER Thread");

        while(true) {
            values = queue.getValues();
            while(values == null){
                Thread.currentThread().yield();
                values = queue.getValues();
            }
            if (values[0] != null && outputBoi.getText() != null) {
                System.out.println(values[0]);
                outputBoi.setText("You: " + values[0] + "\n" + outputBoi.getText());
                //queue.finishPut();
            } else {
                System.out.println("???");
            }
        }
    }
}