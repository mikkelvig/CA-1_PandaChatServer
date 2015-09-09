/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
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
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author Mikkel
 */
public class pandaServer {

    private static final Properties properties = Utils.initProperties("pandaProperty.properties");
    private static ServerSocket serverSocket;
    private static boolean KeepRunning = true;
    pandaServer server = this;
    ConcurrentMap<String, ClientHandler> clientMap = new ConcurrentHashMap();
//    List<ClientHandler> clientList = new ArrayList ();
    PrintWriter out;
    BufferedReader in;
    String usernamestream;

    public static void StopServer() {
        KeepRunning = false;
    }

    public void send(String msg) {
//        for (ClientHandler clientMap : clientMap){
//            clientMap.send(msg);
//        }

    }

    public void removeHandler(ClientHandler client) {
        clientMap.remove(client);
    }

    public void RunServer() {

        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");

        Logger.getLogger(pandaServer.class.getName()).log(Level.INFO, "Sever started. Listening on: " + port + ", bound to: " + ip);
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                Logger.getLogger(pandaServer.class.getName()).log(Level.INFO, "Connected to a client");
//                out = new PrintWriter(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                usernamestream = in.readLine();

                String[] part = usernamestream.split("#");
                String username = part[1];

                ClientHandler newclient = new ClientHandler(username, socket, server);
                newclient.start();
                clientMap.put(username, newclient);

            } while (KeepRunning);
        } catch (IOException ex) {
            Logger.getLogger(pandaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new pandaServer().RunServer();
        try {
            String logFile = properties.getProperty("logFile");
            Utils.setLogFile(logFile, pandaServer.class.getName());

        } finally {
            Utils.closeLogger(pandaServer.class.getName());
        }
    }
}
