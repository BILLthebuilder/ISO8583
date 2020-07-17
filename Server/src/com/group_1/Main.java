
package com.group_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Main {
    private int port;
    private Set<String> clients = new HashSet<>();
    private Set<ClientThread> clientThreads = new HashSet<>();


    public Main(int port) {
        this.port = port;
    }


    /**
     * Listen for incoming connections.
     * Create a thread for every new connection received.
     */
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("SERVER LISTENING ON PORT: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("NEW CLIENT CONNECTED.");
                ClientThread newClient = new ClientThread (socket, this);
                clientThreads.add(newClient);
                newClient.start();
            }
        } catch (IOException ex) {
            System.out.println("ERROR IN THE SERVER");
        }
    }
    /**
     * Delivers a message from server to client
     */
    void broadcast(String message, ClientThread clientThread) {
        clientThread.sendMessage(message);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("PROVIDE THE PORT NUMBER.");
            System.exit(0);
        }
        int port = Integer.parseInt(args[0]);
        Main server = new Main(port);
        server.execute();
    }


    /**
     * Adds the newly connected client to the set
     * @param id identifies a client
     */
    //void addConnectedClient(String id) {
        //clients.add(id);
    //}


    /**
     * Removes the connected client and it's thread.
     * @param id identifies a client
     * @param clientThread identifies the thread to this client
     */
//    void removeConnectedClient(String id, ClientThread clientThread){
//        boolean removed = clients.remove(id);
//        if (removed) {
//            clientThreads.remove(clientThread);
//            System.out.println("THE CLIENT [" + id + "] IS DISCONNECTED!");
//        }
//    }

}
