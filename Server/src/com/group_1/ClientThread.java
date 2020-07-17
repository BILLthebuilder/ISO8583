
package com.group_1;

import java.io.*;
import java.util.*;
import java.net.*;



public class ClientThread extends Thread{
    private final Socket socket;
    private final Main server;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientThread(Socket socket, Main server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            /** Read and send message from the connected user */
            try {
                do {
                    this.readMessage();
                }while (true);
            } catch (NullPointerException nullPointerException){
                System.out.println("An error has occurred, try again");
            } catch (IOException ioException){
                System.out.println("COULD NOT READ INPUT FROM CLIENT. CONNECTION RESET.");
            }
        } catch (IOException ex) {
            System.out.println("Error in UserThread");
        }
    }


    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }


    /**
     * Read a message from the client
     * @return message
     * @throws IOException if clients resets the connection
     */
    void readMessage() throws IOException {
        String clientMessage;
        int random_int = 0;
        int min = 0;
        int max = 9999;
        clientMessage = this.reader.readLine();
        System.out.println(clientMessage);
        String tests = clientMessage;
        int numberOfTests = Integer.parseInt(tests);
//        Generate random int value from 1000 to 9999
        for (int i = 0; i < numberOfTests; i++) {
            random_int = (int) (Math.random() * (max - min + 1) + min);
            System.out.println(random_int);
            clientMessage = String.valueOf(random_int);
            try {
                this.isIso8583(clientMessage);
            } catch (NotISO8583Exception notISO8583Exception) {
                server.broadcast(notISO8583Exception.getMessage(), this);
            }
        }
    }

    /**
     * Check for version number in the MTI
     * @param clientMessage
     * @return TRUE if version no. found. Else FALSE
     */
    boolean checkForVersion(String clientMessage){
        char one = clientMessage.charAt(0);
        //System.out.println(one);
        if(one == '0' || one == '1' || one == '2'){
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * Check for message class in the MTI
     * @param clientMessage
     * @return TRUE if class found. Else FALSE
     */
    boolean checkForClass(String clientMessage){
        char two = clientMessage.charAt(1);
        if (two == '0' || two == '1' || two == '2' || two == '3' || two == '4'
            || two == '5' || two == '6' || two == '7' || two == '8' || two == '9'){
            return  true;
        }
        else {
            return false;
        }
    }


    /**
     * Check for message function in the MTI
     * @param clientMessage
     * @return TRUE if function found. Else FALSE
     */
    boolean checkForFunction(String clientMessage){
        char three = clientMessage.charAt(2);
        if (three == '0' || three == '1' || three == '2' || three == '3' || three == '4'
                || three == '5' || three == '6' || three == '7' || three == '8' || three == '9'){
            return  true;
        }
        else {
            return false;
        }
    }


    /**
     * Check for message origin in the MTI
     * @param clientMessage
     * @return TRUE if origin found. Else FALSE
     */
    boolean checkForOrigin(String clientMessage){
        char four = clientMessage.charAt(3);
        if (four == '0' || four == '1' || four == '2' || four == '3' || four == '4'
                || four == '5' || four == '6' || four == '7' || four == '8' || four == '9'){
            return  true;
        }
        else {
            return false;
        }
    }


    /**
     * Check if the message is ISO8583 using the MTI
     * @param clientMessage
     * @return TRUE if ISO8583. Else FALSE.
     * @throws StringIndexOutOfBoundsException
     */
    boolean formatIso8583(String clientMessage) throws StringIndexOutOfBoundsException, NullPointerException{
        if(this.checkForVersion(clientMessage) &&
                this.checkForClass(clientMessage) &&
                this.checkForFunction(clientMessage) &&
                this.checkForOrigin(clientMessage)){
            return true;
        }
        else{
            return false;
        }
    }


    boolean isIso8583(String clientMessage) throws NotISO8583Exception{
        try{
            if (formatIso8583(clientMessage)) {
                server.broadcast(clientMessage +": This message is valid ISO8583", this);
                System.out.println("This message is valid ISO8583");
            }else if(clientMessage.length() < 4){
                System.out.println("This message is NOT valid ISO8583");
                throw new NotISO8583Exception(clientMessage +": The input is not valid ISO8583");
            }
            else {
                System.out.println("This message is NOT valid ISO8583");
                throw new NotISO8583Exception(clientMessage +": The input is not valid ISO8583");
            }
        }catch(StringIndexOutOfBoundsException stringIndexOutOfBoundsException){
            server.broadcast("Input Error", this);
        }
     return true;
    }

}
