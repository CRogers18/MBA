package monster_battle_arena;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
 * @author Coleman Rogers
 */
public class Main extends Application {
        
    public static void main(String[] args) {
        // Code execution goes here after running init() and start() methods
        launch(args);
    }
    
    public void init() throws IOException
    {
        // Make a new instance of the game class and call the initialization method
        Game game = new Game();
        game.initGame();
        
        // Capturing monster list data for no real reason tbh...
        Monster[] monsters = game.getMonsterList();
    }
    
    
    @Override
    public void start(Stage primaryStage) throws URISyntaxException
    {
        // Create an instance of the graphics generation class
        GenerateGraphics gameGraphics = new GenerateGraphics();

        // Create main menu scene to display when the game starts
        Scene mainMenu = gameGraphics.createMainMenu();
                
        primaryStage.setTitle("Monster Battle Arena");
        primaryStage.setScene(mainMenu);
        primaryStage.setOnCloseRequest(e -> {
        //  e.consume();      <--- will be used to add confirm dialog on close
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();        
    }
    
}
