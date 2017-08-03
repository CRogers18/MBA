package monster_battle_arena;

import java.io.IOException;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.util.logging.Level;
import java.util.logging.Logger;
import monster_battle_arena.Network.FindGame;
import monster_battle_arena.Network.QueueResponse;
import monster_battle_arena.Network.RegisterUser;

/**
 *
 * @author Coleman Rogers
 */
public class GameClient {
        
    /*** EXPERIMENTAL MBA CLIENT-SERVER CODE ***/
    
    private Player player;
    public Client client;
    private int queuePosition;
    
    public GameClient(Player p)
    {
        this.player = p;
        startClient();
    }
    
    public void startClient()
    {
        // Starts a new client and registers it
        client = new Client();
        client.start();
        Network.register(client);
        
        FindGame search = new FindGame();

        // Testing sending player data to the main server
        search.player = player;
        search.isJoining = true;
        search.queuePosition = -1;

        // Listener to pass info along to the server on connection
        client.addListener(new Listener() {

            // Will send the connection info to the server
            public void connected (Connection con)
            {
                RegisterUser register = new RegisterUser();
                register.username = player.getName();
                client.sendTCP(register);
            }

            // If data is received by the client
            public void received(Connection con, Object obj)
            {
                if (obj instanceof Network.QueueResponse)
                {
                    QueueResponse serverResponse = (QueueResponse) obj;
                    queuePosition = serverResponse.placeInQueue;
                    System.out.println("Player is now in queue, position: " + queuePosition);
                }
            }

        });

        // Attempts to connect to the MBA main server
        try
        {
            // Server is running off localhost until a static IP can be set
            client.connect(2000, "localhost", 50005);
            System.out.println("[INFO] Client successfully connected to MBA server");
        } catch (IOException err)
        {
            System.out.println("[ERROR] Failed to connect to MBA server!");
        }
        
        // Tested sending a queue request, 2 second delay, then exit request
        client.sendTCP(search);
        
        try {
            Thread.sleep(2000);
            search.isJoining = false;
            search.queuePosition = queuePosition;
            client.sendTCP(search);
        } catch (InterruptedException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public int getQueuePosition()
    {
        return queuePosition;
    }

    public Client getClient()
    {
        return client;
    }
    
}
