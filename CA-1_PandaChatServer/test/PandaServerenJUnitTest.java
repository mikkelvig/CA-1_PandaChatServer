
import client.pandaClient;
import java.io.IOException;
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
public class PandaServerenJUnitTest
{

    static PandaServeren serveren;

    private static final Properties properties = Utils.initProperties("pandaProperty.properties");
    int port = Integer.parseInt(properties.getProperty("port"));
    String ip = properties.getProperty("serverIp");
    private Scanner input;
    private PrintWriter output;
    Socket socket;

    public PandaServerenJUnitTest()
    {
    }

    @BeforeClass
    //husk at stoppe den efter klasse 
    public static void setUp()
    {
        Runnable r = new Runnable()
        {

            @Override
            public void run()
            {
                serveren = new PandaServeren();

                serveren.Run();
            }
        };
        new Thread(r).start();
    }
    
    @AfterClass
    public static void tearDownClass(){
        PandaServeren.StopServer();
    }
    

    @Test
    public void testOfOneClientTrue() throws IOException
    {
        socket = new Socket(ip, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true); //Set to true, to get auto flush behaviour
        output.write("USER#Bubber");
        String serverResponse = "USERLIST#Bubber,";
        System.out.println("gad vide om man kan se det her: "+input.nextLine());
        assertTrue(serverResponse.equals(input.nextLine()));

//        testpanda.send("MSG#*#Hej testpanda");
        // debug
    }

//    @Test
//    public void testAfSendTrue()
//    {
//        PandaServeren panda = new PandaServeren();
//        ConcurrentMap<String, ClientHandler> testclient = new ConcurrentHashMap();
//        try
//        {
//            Socket s = new Socket("localhost", 9090);
//            
//            testclient.put("Andreas", new ClientHandler("Andreas", s, new PandaServeren()));
//        } catch (IOException ex)
//        {
//            Logger.getLogger(PandaServerenJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        panda.clientMap = testclient;
//        panda.send("Bente", "MSG#*#Hej allesammen");
//       // her mangler vi at gribe beskeden og teste om det er den rigtige 
//    }
}
