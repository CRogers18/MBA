package mba_server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Queue;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mba_server.Network.FindGame;
import mba_server.Network.QueueResponse;
import mba_server.Network.RegisterUser;

/**
 * @author Coleman Rogers
 */
public class MBA_Server extends Application {
    
    private final String version = "0.01", consoleText = "Starting MBA Server v" + version;
    private final TextArea console = new TextArea();
    private final TextArea connectedClientList = new TextArea();
    int i = 0;
    boolean isLive = false;
    private LinkedList <Player> matchmaking;
    
    Server server;
    
    // application initialization phase, does NOT run on JavaFX application thread
    @Override
    public void init()
    {
        server = new Server() 
        {
            protected Connection newConnection () {
                return new PlayerConnection();
            }
        };
        
        server.start();
        Network.register(server);
        
        // initialize matchmaking queue
        matchmaking = new LinkedList<>();
        
        server.addListener(new Listener() {
            
            // listener for receiving data
            public void received(Connection con, Object obj)
            {
                // Handles new user connections
                if (obj instanceof RegisterUser)
                {
                    PlayerConnection connect = (PlayerConnection) con;
                    
                    // If user already is registered, no need to store
                    if (connect.username != null) return;
                    
                    // If not, store the name
                    String username = ((RegisterUser) obj).username;
                    
                    // If given name is invalid, ignore
                    if (username == null) return;
                    
                    connect.username = username;
                    
                    console.appendText(pTime() + connect.username + " has connected to the server.");
                }
                
                // Handles search for a game
                if (obj instanceof FindGame)
                {
                    PlayerConnection connect = (PlayerConnection) con;
                    
                    // TODO: look into different kinds of connections (?)
                    connect.player = ((FindGame) obj).player;
                    connect.isJoining = ((FindGame) obj).isJoining;
                    connect.queuePosition = ((FindGame) obj).queuePosition;
                                        
                    // If the player is not already in the queue and wants to join it, add them
                    if (connect.queuePosition == -1 && connect.isJoining)
                    {
                        matchmaking.add(connect.player);
                        
                        // server needs to send back the position in the queue to the client
                        QueueResponse response = new QueueResponse();
                        response.placeInQueue = matchmaking.size();
                        connect.sendTCP(response);
                        
                        console.appendText(pTime() + connect.username + " has entered the matchmaking queue. Queue load: " + matchmaking.size());
                    }
                    
                    // If the player is requesting to leave the queue and is in it, remove them
                    if (connect.queuePosition != -1 && !connect.isJoining)
                    {
                        Player playerToRemove = matchmaking.get(connect.queuePosition - 1);
                        matchmaking.remove(playerToRemove);
                        console.appendText(pTime() + connect.username + " has exited the matchmaking queue. Queue load: " + matchmaking.size());
                    }    
                }    
                
            }
            
            // What to do if a disconnection occurs
            public void disconnected (Connection con)
            {
                PlayerConnection connect = (PlayerConnection) con;
                
                if (connect.username != null)
                {
                    console.appendText(pTime() + connect.username + " has disconnected from the server.");
                }
            }
        });
        
        try
        {
            server.bind(50005);
            isLive = true;
        } catch (IOException err)
        {
            System.out.println("Server failed to start on port 50005!");
        }
        
    }
    
    // Class for player connection
    static class PlayerConnection extends Connection
    {
        public String username;
        public Player player;
        public boolean isJoining;
        public int queuePosition;
    }
    
    @Override
    public void start(Stage primaryStage)
    {
        StackPane root = new StackPane();
        
        connectedClientList.setEditable(false);
        connectedClientList.setPadding(new Insets(10, 10, 50, 10));
        connectedClientList.relocate(10, 10);
        connectedClientList.setText("Connected clients: ");
        console.setPadding(new Insets(200, 10, 10, 10));
        console.setEditable(false);
        root.getChildren().addAll(connectedClientList, console);
        
        console.setText(consoleText);
        
        Scene scene = new Scene(root, 500, 450);
        
        primaryStage.setTitle("MBA Server v" + version);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        if (isLive)
            console.appendText(pTime() + "Server started on port 50005");
        else
            console.appendText(pTime() + "[FATAL ERROR] " + "Server failed to bind to port 50005!");
    }
    
    public String pTime()
    {
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        String formatted = "\n[" + timeStamp + "] ";
        return formatted;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
    
}