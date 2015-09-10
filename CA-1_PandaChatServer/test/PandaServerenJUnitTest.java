
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

    PandaServeren serveren;

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
    public void setUp()
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

    @Test
    public void testOfOneClientTrue() throws IOException
    {
        socket = new Socket(ip, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true); //Set to true, to get auto flush behaviour
        
        socket2 = new Socket(ip, port);
        input2 = new Scanner(socket.getInputStream());
        output2 = new PrintWriter(socket.getOutputStream(), true); //Set to true, to get auto flush behaviour
        
        
        testpanda.send("USER#testpanda");

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
