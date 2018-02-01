package edu.cse;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class GUI {
    Queue _inputQueue;
    Queue _outputQueue;
    private String _name;
    TextField _input;
    Text _text;
    ScrollPane _scroll;
    private BufferedReader reader;
    private PrintWriter writer;
    private OutputStream out;

    GUI(String s, Queue in, Queue out, Text text) {
        _inputQueue = in;
        _outputQueue = out;
        _name = s;
        _text = text;
    }
    public void run(final Stage stage){
        Thread.currentThread().setName("GUI Thread");
        Label question = new Label("Type something.");
        HBox prompt = new HBox();
        Button submit = new Button("Submit");
        VBox layout = new VBox();
        //_text = new Text();
        _input = new TextField();
        prompt.setSpacing(10);
        layout.setSpacing(10);
        submit.setOnAction((e) -> {
            if (_input.getText() != "") {
                boolean didPut = _inputQueue.put(_input.getText());
                while (!didPut) {
                    Thread.currentThread().yield();
                    _inputQueue.put(_input.getText());
                    _input.clear();
                }
            }
        });

    _scroll = new ScrollPane();
        _scroll.setContent(_text);
        _scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        _scroll.setPrefHeight(100);
        _scroll.fitToWidthProperty();
    Getter myGetter = new Getter(_outputQueue,_text);
    Thread getterThread = new Thread(myGetter);
        getterThread.start();
        TextSender sender = new TextSender(_inputQueue, writer, out);
        Thread senderThread = new Thread(sender);
        senderThread.start();
        prompt.getChildren().addAll(question, _input);
        layout.getChildren().addAll(prompt,submit,_scroll);
        Group s1 = new Group();
        s1.getChildren().addAll(layout);
        Scene scene1 = new Scene(s1,310,200);
        stage.setScene(scene1);
        stage.setTitle("fvhvbkbd");
        stage.show();
    }
    public void setClientNetworking(BufferedReader r, PrintWriter w, OutputStream stream) {
        reader = r;
        writer = w;
        out = stream;
    }
}
