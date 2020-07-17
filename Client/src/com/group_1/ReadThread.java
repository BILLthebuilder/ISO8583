
package com.group_1;

import java.io.*;
import java.net.*;

/**
 * This thread is responsible for reading server's input and printing it
 * to the console.
 * It runs in an infinite loop until the client disconnects from the server.
 */
public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private Simulator simulator;

    public ReadThread(Socket socket, Simulator simulator) {
        this.socket = socket;
        this.simulator = simulator;
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("COULD NOT READ INPUT FROM SERVER. CONNECTION RESET.");
        }
    }

    public void run() {
        while (true) {
            try {
                String response = this.readMessage();
                simulator.updateResults(response);
            } catch (IOException ex) {
                System.out.println("COULD NOT READ INPUT FROM SERVER. CONNECTION RESET." );
                break;
            }
        }
    }


    /**
     * Read a message from the server
     * @return message
     * @throws IOException if clients resets the connection
     */
    String readMessage() throws IOException {
        String clientMessage;
        clientMessage = this.reader.readLine();
        return  clientMessage;
    }
}
