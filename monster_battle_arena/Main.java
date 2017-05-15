package monster_battle_arena;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * @author Coleman Rogers
 */
public class Main extends Application {
    
    private String version = "0.05";
    Game game = new Game();
    Player player = new Player();
    Monster[] monsterList;
            
    public static void main(String[] args) {
        // Code execution goes here after running init() and start() methods
        launch(args);
    }
    
    public void init() throws IOException
    {
        // Make a new instance of the game class and call the initialization method
        game.initGame(player);
        
        // Capturing monster list data for no real reason tbh...
        monsterList = game.getMonsterList();
        
        /* RAM Usage task, to be used later
        new Timer().schedule(new TimerTask() {
            
            Runtime rt = Runtime.getRuntime();
            long ramUsed = rt.totalMemory() - rt.freeMemory();
            
            @Override
            public void run()
            {
                System.out.println("[INFO] RAM Used: " + (ramUsed/1024L) + " MB");
            }
            
        }, 0, 1000);
        */
    }
    
    
    @Override
    public void start(Stage primaryStage) throws URISyntaxException
    {
        // Create an instance of the graphics generation class
        GenerateGraphics gameGraphics = new GenerateGraphics(player, monsterList);

        // Create main menu scene to display when the game starts
        Scene mainMenu = gameGraphics.createMainMenu(primaryStage);
                
        primaryStage.setTitle("Monster Battle Arena v" + version);
        primaryStage.setScene(mainMenu);
        primaryStage.setOnCloseRequest(e -> {
        //  e.consume();      <--- will be used to add confirm dialog on close
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();        
    }
    
}
