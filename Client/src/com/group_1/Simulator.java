
package com.group_1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;


public class Simulator extends Frame implements ActionListener, WindowListener {
    ArrayList<String> response;
    TextField testsTextField;
    Label testsLabel;
    TextArea resultsMessage;
    TextField hostField;
    TextField portField;
    Label hostLabel;
    Label portLabel;
    Button runTestsButton;
    Button startServerButton;
    private Socket socket;
    private PrintWriter writer;
    private OutputStream output;

    public Simulator(){
        setTitle("CLIENT SIMULATOR");
        setSize(400, 800);
        setLayout(null);
        setVisible(true);
        addWindowListener(this);
        setResizable(false);

        this.response = new ArrayList<>();

        this.hostLabel = new Label();
        this.hostLabel.setText("HOSTNAME");
        this.hostLabel.setBounds(15, 50, 100, 30);
        add(this.hostLabel);

        this.hostField = new TextField();
        hostField.setText("localhost");
        this.hostField.setBounds(120, 50, 250, 30);
        add(this.hostField);

        this.portLabel = new Label();
        this.portLabel.setText("PORT");
        this.portLabel.setBounds(15, 100, 100, 30);
        add(this.portLabel);

        this.portField = new TextField();
        portField.setText("9090");
        this.portField.setBounds(120, 100, 250, 30);
        add(this.portField);

        this.testsLabel = new Label();
        this.testsLabel.setText("TESTS NO.");
        this.testsLabel.setBounds(15, 150, 100, 30);
        add(this.testsLabel);

        this.testsTextField = new TextField();
        this.testsTextField.setBounds(120, 150, 250, 30);
        add(this.testsTextField);

        this.runTestsButton = new Button("RUN TESTS");
        this.runTestsButton.setBounds(120, 200, 200, 30);
        this.runTestsButton.addActionListener(this);
        add(this.runTestsButton);

        this.startServerButton = new Button("START SERVER");
        this.startServerButton.setBounds(15, 200, 100, 30);
        this.startServerButton.addActionListener(this);
        add(this.startServerButton);

        this.resultsMessage = new TextArea();
        this.resultsMessage.setText("Results:");
        this.resultsMessage.setBounds(15, 250, 350, 500);
        add(this.resultsMessage);
    }

    public void actionPerformed(ActionEvent e) throws NullPointerException {
        try {
            if (e.getSource() == this.startServerButton) {
                String hostname = this.hostField.getText();
                int port = Integer.parseInt(this.portField.getText());
                this.socket = this.connectToServer(hostname, port);
                new ReadThread(socket, this).start();
            }
            if (e.getSource() == this.runTestsButton) {
                this.sendMessage(this.testsTextField.getText());
            }
        } catch (NullPointerException nullPointerException) {
            System.out.println("Server Error, try again");
        }

    }


    public void updateResults(String message){
        this.response.add(message);
        this.resultsMessage.setText("");
        String msg = "";
        Iterator<String> iter= this.response.iterator();
        while (iter.hasNext()) {
            msg += iter.next()+"\n";
        }
        this.resultsMessage.setText(msg);
    }

    public void sendMessage(String message){
        try {
            this.output = this.socket.getOutputStream();
            this.writer = new PrintWriter(this.output, true);
            this.writer.println(message);
        } catch (IOException ex) {
            System.out.println("COULD NOT REACH THE SERVER. CONNECTION RESET." );
        }
    }

    public Socket connectToServer(String hostname, int port){
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("CONNECTED TO SERVER.");
            return socket;
        } catch (UnknownHostException ex) {
            System.out.println("SERVER NOT FOUND.");
            return null;
        } catch (IOException ex) {
            System.out.println("COULD NOT READ INPUT FROM SERVER. CONNECTION RESET." );
            return null;
        }
    }

    // Listen for actions on the window
    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) { dispose(); }
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
}

