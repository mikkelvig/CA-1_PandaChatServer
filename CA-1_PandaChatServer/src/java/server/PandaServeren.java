/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;

/**
 *
 * @author steffen
 */
public class PandaServeren {

    private static final Properties properties = Utils.initProperties("pandaProperty.properties");
    private static ServerSocket serverSocket;
    private static boolean KeepRunning = true;
    PandaServeren server = this;
    ConcurrentMap<String, ClientHandler> clientMap = new ConcurrentHashMap();
    PrintWriter out;
    BufferedReader in;
    String usernamestream;

    public static void StopServer() {
        KeepRunning = false;
    }

    public void send(String username, String msg) {

        String[] parts = msg.split("#");
        String part1 = parts[0]; // selve kommandoen
        String part2 = parts[1]; //brugerne
        String part3 = parts[2]; // selve beskeden
        

        if (part1.equals("MSG")) {
            if (part2.equals("*")) {
                // her skal alle i listen have beskeden
                for (ClientHandler value : clientMap.values()) {
                    String messageToUser = "MSG#"+username+"#";
                    value.send(messageToUser + part3);
                }
            } else {
                String[] userString = part2.split(","); // ved flere brugere splittes disse op, og får hver deres index i userString arrayet.
                for (String users : userString) {
                    ClientHandler value = clientMap.get(users);
                    String messageToUser = "MSG#"+username+"#";
                    value.send(messageToUser + part3);
                }
            }

        }

    }

    public void removeHandler(ClientHandler client) {
        clientMap.remove(client.getUsername(), client);
        // denne blok tilføjer users til stringWithUsers og udskriver til alle klienter
        String stringWithUsers = "USERLIST#";
        for (ClientHandler value : clientMap.values()) {
            stringWithUsers += String.valueOf(value.getUsername() + ",");
        }
        for (ClientHandler value : clientMap.values()) {
            value.send(stringWithUsers);
        }

    }

    public void Run() {
        // henter værdierene af serverIp og port fra vores property fil
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");
        // Server startede besked med specifik ip og port tilføjet i log
        Logger.getLogger(PandaServeren.class.getName()).log(Level.INFO, "Sever started. Listening on: " + port + ", bound to: " + ip);
        try {
            // Serversocket instantieres
            serverSocket = new ServerSocket();
            // serverSocket bindes til en specifik adresse, værdien af InetSocketAddress = predefinerede ip og port
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                // serversocket venter på at nogen prøver at oprette forbindelse, og accepterer når det sker, og blockerer indtil da 
                Socket socket = serverSocket.accept();
                // besked om ny klient, tilføjet til log
                Logger.getLogger(PandaServeren.class.getName()).log(Level.INFO, "Connected to a client");

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                usernamestream = in.readLine();
                String[] part = usernamestream.split("#");
                String username = part[1];
                // bruger den accepterede socket til at oprette en ny client 
                ClientHandler client = new ClientHandler(username, socket, server);
                // starter en ny tråd for hver ny client
                client.start();
                // tilføjere den nye client til det trådsikre concurrenthashmap med username som key
                clientMap.put(username, client);
                // denne blok tilføjer users til stringWithUsers og udskriver til alle klienter
                String stringWithUsers = "USERLIST#";
                for (ClientHandler value : clientMap.values()) {
                    stringWithUsers += String.valueOf(value.getUsername() + ",");
                }
                for (ClientHandler value : clientMap.values()) {
                    value.send(stringWithUsers);
                }
                // en boolean som kan sættes til false med stopserver metoden 
            } while (KeepRunning);
        } catch (IOException ex) {
            Logger.getLogger(PandaServeren.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {

        PandaServeren panda = new PandaServeren();
        panda.Run();

        try {
            String logFile = properties.getProperty("logFile");
            Utils.setLogFile(logFile, pandaServer.class.getName());

        } finally {
            Utils.closeLogger(pandaServer.class.getName());
        }
    }

}
