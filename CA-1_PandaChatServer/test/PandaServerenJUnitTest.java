
import client.pandaClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Proxy;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import server.ClientHandler;
import server.PandaServeren;
import utils.Utils;

/**
 *
 * @author Bente
 */
public class PandaServerenJUnitTest {

//    static PandaServeren serveren;
    private static final Properties properties = Utils.initProperties("pandaProperty.properties");
    int port = Integer.parseInt(properties.getProperty("port"));
    String ip = properties.getProperty("serverIp");
    PrintWriter output;
    Socket socket;
    BufferedReader in;
    PrintWriter output2;
    Socket socket2;
    BufferedReader in2;
    PrintWriter output3;
    Socket socket3;
    BufferedReader in3;
    PrintWriter output4;
    Socket socket4;
    BufferedReader in4;
    
    public PandaServerenJUnitTest() {
    }

    @BeforeClass
    public static void setUp() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                PandaServeren serveren = new PandaServeren();
                serveren.Run();
            }
        };
        new Thread(r).start();
    }

    @AfterClass
    public static void tearDownClass() {
        PandaServeren.StopServer();
    }

    @Test
    public void testConnectionOfOneClientTrue() throws IOException {

        socket = new Socket(ip, port);
        output = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        output.println("USER#Bubber");
        output.println("STOP#");
        String msg = in.readLine();

        String serverResponse = "USERLIST#Bubber,";
        assertTrue(serverResponse.equals(msg));

    }

    @Test
    public void testConnectionOfTwoClientsTrue() throws IOException {

        socket = new Socket(ip, port);
        output = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output.println("USER#Bubber");

        socket2 = new Socket(ip, port);
        output2 = new PrintWriter(socket2.getOutputStream(), true);
        in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        output2.println("USER#Panda");

        String userlist2 = in2.readLine();
        output.println("STOP#");
        output2.println("STOP#");

        String serverResponse = "USERLIST#Bubber,Panda,";
        assertTrue(serverResponse.equals(userlist2));
    }
    
    @Test
    public void testStopCommand() throws IOException {

        socket = new Socket(ip, port);
        output = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output.println("USER#Mikkel");

        socket2 = new Socket(ip, port);
        output2 = new PrintWriter(socket2.getOutputStream(), true);
        in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        output2.println("USER#Torben");
        
        String userlist1 = in2.readLine();
        output.println("STOP#");
                                  System.out.println("Første gang:"+userlist1);
        String userlist2 = in2.readLine();
                                  System.out.println("Anden gang: "+userlist2);
        output2.println("STOP#");

        String serverResponse = "USERLIST#Torben,";
        assertTrue(serverResponse.equals(userlist2));
    }
    
    @Test
    public void testOfMSGCommandToAll() throws IOException {
        socket = new Socket(ip, port);
        output = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output.println("USER#Bente");

        socket2 = new Socket(ip, port);
        output2 = new PrintWriter(socket2.getOutputStream(), true);
        in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        output2.println("USER#Steffen");
        
        output.println("MSG#*#Hej alle");
        String bentesOutput = in.readLine();
        String steffensOutput = in2.readLine();
        assertTrue(bentesOutput.equals("MSG#Bente#Hej alle"));
        assertTrue(steffensOutput.equals("MSG#Bente#Hej alle"));
        output.println("STOP#");
        output2.println("STOP#");
    }
    
    @Test
    public void testOfMSGCommandToSpecificUser() throws IOException {
        socket = new Socket(ip, port);
        output = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output.println("USER#RainbowDash");

        socket2 = new Socket(ip, port);
        output2 = new PrintWriter(socket2.getOutputStream(), true);
        in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        output2.println("USER#PinkiePie");
        
        output.println("MSG#PinkiePie#Hold da helt hest!!");
        String pinkiePiesOutput = in2.readLine();
        assertTrue(pinkiePiesOutput.equals("MSG#RainbowDash#Hold da helt hest!!"));
        output.println("STOP#");
        output2.println("STOP#");
    }
    
    @Test
    public void testOfMSGCommandToTwoOutOfThreeUsers() throws IOException {
        socket = new Socket(ip, port);
        output = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output.println("USER#Tom Brady");

        socket2 = new Socket(ip, port);
        output2 = new PrintWriter(socket2.getOutputStream(), true);
        in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        output2.println("USER#Matthew Slater");
        
        socket3 = new Socket(ip, port);
        output3 = new PrintWriter(socket3.getOutputStream(), true);
        in3 = new BufferedReader(new InputStreamReader(socket3.getInputStream()));
        output3.println("USER#Bill Belichick");
        
        socket4 = new Socket(ip, port);
        output4 = new PrintWriter(socket4.getOutputStream(), true);
        in4 = new BufferedReader(new InputStreamReader(socket4.getInputStream()));
        output4.println("USER#Rob Gronkowski");
        
        output.println("MSG#Matthew Slater, Rob Gronkowski#Hi guys");
        String MatthewsOutput = in2.readLine();
        String BillsOutput = in3.readLine();
        String RobsOutput = in4.readLine();
        
        assertTrue(MatthewsOutput.equals("MSG#Tom Brady#Hi guys"));
        assertTrue(RobsOutput.equals("MSG#Tom Brady#Hi guys"));
        assertFalse(BillsOutput.equals("MSG#Tom Brady#Hi guys"));
        
        output.println("STOP#");
        output2.println("STOP#");
        output3.println("STOP#");
        output4.println("STOP#");
    }
    
}
