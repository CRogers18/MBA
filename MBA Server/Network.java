package mba_server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

/**
 *
 * @author Coleman Rogers
 */
public class Network {
    
    public static final int port = 50005;
    
    public static void register (EndPoint endpoint)
    {
        // Register classes that contain objects that will be sent over the network
        Kryo kryo = endpoint.getKryo();
        ObjectSpace.registerClasses(kryo);
        kryo.register(RegisterUser.class);
        kryo.register(FindGame.class);
        kryo.register(QueueResponse.class);
        kryo.register(Player.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(Monster.class);
        kryo.register(String[].class);
        
    }
    
    // TODO (Maybe): Move player class into here?
    
    public static class RegisterUser
    {
        public String username;
    }
    
    public static class FindGame
    {
        public Player player;
        public boolean isJoining;
        public int queuePosition;
    }
    
    public static class QueueResponse
    {
        public int placeInQueue;
    }
    
}
