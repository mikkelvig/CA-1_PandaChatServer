/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import sharedprotocol.PandaProtocol;

/**
 *
 * @author Mikkel
 */
public class pandaClient extends Observable
{

    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;

    public void connect(String address, int port) throws UnknownHostException, IOException
    {
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true); //Set to true, to get auto flush behaviour

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
//                    while(!input.hasNext()){
//                    }
//                    System.out.println(input.hasNext());
                    String msg = input.nextLine();
                    setChanged();
                    notifyObservers(msg);

                    if (msg.equals(PandaProtocol.STOP))
                    {
                        try
                        {
                            socket.close();
                        } catch (IOException ex)
                        {
                            Logger.getLogger(pandaClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }).start();
    }

    public void send(String msg)
    {
        output.println(msg);
    }

    public void stop() throws IOException
    {
        output.println(PandaProtocol.STOP);
    }
}
