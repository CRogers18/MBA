package monster_battle_arena;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * @author Coleman Rogers
 */
public class Main extends Application {
        
    public static void main(String[] args) {
        System.out.println("Now in main");
        launch(args);
    }
    
    public void init() throws IOException
    {
        // Code that runs before start() method is invoked
        Game game = new Game();
        game.initGame();
        
        Monster[] monsters = game.getMonsterList();
    }
    
    
    @Override
    public void start(Stage primaryStage) throws URISyntaxException 
    {
        GenerateGraphics gameGraphics = new GenerateGraphics();
        
        String videoPath = getClass().getResource("/VideoAssets/mainMenuBg1.mp4").toURI().toString();
//      String audioPath = getClass().getResource("/AudioAssets/mainMenu_Winters Tale.mp3").toURI().toString();
        
        // Code to create a window to display the application in
        Group root = new Group();
        Scene mainScene = new Scene(root, 1920, 1080);
        
        Media mainMenuBg = new Media(videoPath);
//      Media mainMenuAudio = new Media(audioPath);

        MediaPlayer vidPlayer = new MediaPlayer(mainMenuBg);
//      MediaPlayer audioPlayer = new MediaPlayer(mainMenuAudio);
/*                 
        audioPlayer.setVolume(0.45);
        audioPlayer.setStartTime(Duration.seconds(1));
        audioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        
        audioPlayer.setOnEndOfMedia(() -> {
                audioPlayer.seek(Duration.ZERO);
                System.out.println("Audio media ended!");
            });
*/        
//      MediaView audioView = new MediaView(audioPlayer);
        MediaView videoView = new MediaView(vidPlayer);
        
        Text mainTitle = new Text(610, 320, "Monster Battle Arena");
        mainTitle.setFont(Font.font("Endor", FontWeight.BOLD, 72));
        mainTitle.setStroke(Color.BLUE);
        mainTitle.setFill(Color.WHITE);
        
        Button startBtn = new Button("Start");
        startBtn.setOnAction(e -> {
            root.getChildren().remove(startBtn);
            root.getChildren().add(gameGraphics.makeButton("New Game", 830, 600, "-fx-text-fill: #00ffff"));
        });
        startBtn.setOnMouseEntered(e -> {
            startBtn.setStyle("-fx-text-fill: #009999");
        });
        startBtn.setOnMouseExited(e -> {
            startBtn.setStyle("-fx-text-fill: #00ffff");
        });
        startBtn.setFont(Font.font("Endor", FontWeight.BOLD, 55));
        startBtn.setPrefSize(250, 2);
        startBtn.setBackground(Background.EMPTY);
        startBtn.setStyle("-fx-text-fill: #00ffff");
        startBtn.relocate(830, 500);
        
        root.getChildren().addAll(videoView, mainTitle, startBtn);
        
    //    ((Group)mainScene.getRoot()).getChildren().addAll(videoView, audioView, mainTitle);
        
        primaryStage.setTitle("Monster Battle Arena");
        primaryStage.setScene(mainScene);
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
        
        vidPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        vidPlayer.setOnEndOfMedia(() -> {
                System.out.println("Video media ended!");
                vidPlayer.seek(Duration.ZERO);
            });
        vidPlayer.play();
    //    audioPlayer.play();
    }
    
}
