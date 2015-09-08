/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;


/**
 *
 * @author Mikkel
 */
public class PandaServer {

    
    private static final Properties properties = Utils.initProperties("pandaProperty.properties");
    private static ServerSocket serverSocket;
    private static boolean KeepRunning = true;
    PandaServer server = this;
    List<ClientHandler> clientList = new ArrayList ();

    

    public static void StopServer() {
        KeepRunning = false;
    }
    
     public void send(String msg){
        for (ClientHandler clientList : clientList){
            clientList.send(msg);
        }
    } 
     
     public void removeHandler(ClientHandler client){
         clientList.remove(client);
 } 

    public void RunServer() {
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");

        Logger.getLogger(PandaServer.class.getName()).log(Level.INFO, "Sever started. Listening on: " + port + ", bound to: " + ip);
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                Logger.getLogger(PandaServer.class.getName()).log(Level.INFO, "Connected to a client");
                ClientHandler newclient = new ClientHandler(socket, server);
                newclient.start();
                clientList.add(newclient);
                
            } while (KeepRunning);
        } catch (IOException ex) {
            Logger.getLogger(PandaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new PandaServer().RunServer();
        try {
            String logFile = properties.getProperty("logFile");
            Utils.setLogFile(logFile, PandaServer.class.getName());

        } finally {
            Utils.closeLogger(PandaServer.class.getName());
        }
    }
}
