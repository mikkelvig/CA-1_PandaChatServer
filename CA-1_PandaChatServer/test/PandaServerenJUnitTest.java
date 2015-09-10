
import java.io.IOException;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import server.ClientHandler;
import server.PandaServeren;

/**
 *
 * @author Bente
 */
public class PandaServerenJUnitTest
{

    public PandaServerenJUnitTest()
    {
    }

    @Test
    public void testAfSendTrue()
    {
        PandaServeren panda = new PandaServeren();
        ConcurrentMap<String, ClientHandler> testclient = new ConcurrentHashMap();
        try
        {
            Socket s = new Socket("localhost", 9090);
            
            testclient.put("Andreas", new ClientHandler("Andreas", s, new PandaServeren()));
        } catch (IOException ex)
        {
            Logger.getLogger(PandaServerenJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        panda.clientMap = testclient;
        panda.send("Bente", "MSG#*#Hej allesammen");
       // her mangler vi at gribe beskeden og teste om det er den rigtige 

    }

}
