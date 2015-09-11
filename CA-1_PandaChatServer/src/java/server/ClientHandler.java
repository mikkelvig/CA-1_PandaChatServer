/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import sharedprotocol.PandaProtocol;

/**
 *
 * @author Bente
 */
public class ClientHandler extends Thread {

    Scanner input;
    PrintWriter writer;
    Socket socket;
    PandaServeren server;
    String username;

    public ClientHandler(String username, Socket socket, PandaServeren server) throws IOException {

        this.username = username;
        this.socket = socket;
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
        this.server = server;
    }

    public void send(String msg) {
        writer.println(msg);
    }

    public String getUsername() {
        return username;
    }    
        @Override
        public void run(){
        
        String message = input.nextLine(); //IMPORTANT blocking call
        Logger.getLogger(PandaServeren.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
        while (!message.equals(PandaProtocol.STOP)) {
//            writer.println(message.toUpperCase());
            server.send(username,message);
            Logger.getLogger(PandaServeren.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
            message = input.nextLine(); //IMPORTANT blocking call
        }
        writer.println(PandaProtocol.STOP);//Echo the stop message back to the client for a nice closedown
        try {
            socket.close();
            input.close();
            writer.close();
            server.removeHandler(this);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(PandaServeren.class.getName()).log(Level.INFO, "Closed a Connection");
    }
        
    
}
