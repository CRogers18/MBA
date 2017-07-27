package monster_battle_arena;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/*
 * @author Coleman Rogers
 */
public class Main extends Application {
    
    private final String version = "0.140";
    private final Game game = new Game();
    private final Player player = new Player();
    private File playerData;
    private Monster[] monsterList;
    private Image[] cardImages, cardBanners;
    private boolean showRamStats = true, isBeginner = true;
            
    public static void main(String[] args) {
        // Code execution goes here after running init() and start() methods
        launch(args);
    }
    
    public void init() throws IOException
    {
        System.out.println("[INFO] Starting Monster Battle Arena v" + version);
        
        if (showRamStats)
        {
            // RAM usage task
            new Timer().schedule(new TimerTask() {

                long startTime = System.nanoTime();

                @Override
                public void run()
                {
                    long stopTime = System.nanoTime();
                    long delta_t = (stopTime - startTime)/1000000000;
                    System.gc();
                    Runtime rt = Runtime.getRuntime();
                    System.out.println("[" + delta_t + "s] Heap Space Used: " + ((rt.totalMemory() - rt.freeMemory())/1048576) + " MB" + 
                                       "\t Total Heap Size: " + rt.totalMemory()/1048576 + " MB");
                }

            }, 0, 1000);
        }        
    }
    
    @Override
    public void start(Stage primaryStage) throws URISyntaxException, IOException
    {
        // Pre-load splash screen
        Group splashGroup = new Group();
        Scene splashScene = new Scene(splashGroup, 1920, 1080);
        ImageView splashImage = new ImageView(new Image("/ImageAssets/loadScreen.png", 1920, 1080, false, false));
        splashGroup.getChildren().add(splashImage);
        primaryStage.setWidth(1920);
        primaryStage.setHeight(1080);
        primaryStage.setResizable(false);
        primaryStage.setScene(splashScene);
        primaryStage.show();
                
        // Make a new instance of the game class and call the initialization method
        game.initGame(player);
        isBeginner = game.isBeginner();
        
        monsterList = game.getMonsterList();
        cardImages = game.loadCardImages();
        cardBanners = game.loadCardBanners();
        playerData = game.getPlayerData();
                
        // Create an instance of the graphics generation class
        GenerateGraphics gameGraphics = new GenerateGraphics(player, monsterList, cardImages, cardBanners, primaryStage, playerData, isBeginner);

        // Create main menu scene to display when the game starts
        Scene mainMenu = gameGraphics.createMainMenu();
        
        if (isBeginner)
        {
            Scene beginnerScene = gameGraphics.createBeginnerScene();
            primaryStage.setScene(beginnerScene);
        }
        
        else
            primaryStage.setScene(mainMenu);
                
        
//TODO: primaryStage.setFullScreen(true);
        primaryStage.setOnCloseRequest(e -> {
        //  e.consume();      <--- will be used to add confirm dialog on close
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }
    
}
