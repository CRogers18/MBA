package monster_battle_arena;

import java.net.URISyntaxException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author Coleman Rogers
 */
public class GenerateGraphics {
    
    // Class for quickly generating graphical interface elements
    
    public Button makeButton(String text, String textColor, String bgColor, int fontSize, double minWidth)
    {
        // Makes a new button with specified text and CSS styling
        Button button = new Button(text);
        button.setStyle("-fx-text-fill: " + textColor + ";" + "-fx-background-color: " + bgColor + ";");
        button.setFont(Font.font("Endor", FontWeight.BOLD, fontSize));
        button.setMinWidth(minWidth);
        
        return button;
    }
    
    public VBox makeVerticalBox(int vSpacing)
    {
        // Creates a new vertical box placed at the specified location, using the specified alignment
        VBox box = new VBox(vSpacing);
        
        return box;
    }
    
    public Label makeLabel(double posX, double posY)
    {
        // Creates a new label at the specified location
        Label label = new Label();
        label.relocate(posX, posY);
        
        return label;
    }
    
    public Text makeText(String textContent, String style, int fontSize)
    {
        // Creates a new text element at the specified location with specified content and CSS styling
        Text text = new Text(textContent);
        text.setStyle(style);
        text.setFont(Font.font("Endor", FontWeight.BOLD, fontSize));
        
        return text;
    }
    
    public Scene createMainMenu() throws URISyntaxException
    {
        String videoPath = getClass().getResource("/VideoAssets/mainMenuBg.mp4").toURI().toString();
        String audioPath = getClass().getResource("/AudioAssets/mainMenu_Winters Tale.mp3").toURI().toString();
        
        Group root = new Group();
        BorderPane window = new BorderPane();

        Scene mainMenu = new Scene(root, 1920, 1080);
        
        // Create a vertical box to hold menu elements in a single column.
        // makeVerticalBox parameters: spacing between nodes in Y-axis, posX, posY
        VBox box = new VBox(40);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(200);
        window.setCenter(box);
        // Insets parameters (padding space in pixels): top, right, bottom, left
        box.setPadding(new Insets(200, 540, 100, 600));
        
        // Create text for main game title
        // makeText parameters: text content, text CSS styling, text font size
        Text mainTitle = makeText("Monster Battle Arena", "", 72);
        mainTitle.setStroke(Color.BLUE);
        mainTitle.setFill(Color.WHITE);
        
        Button startBtn = new Button("Start");
        startBtn.setOnAction(e -> {
            box.getChildren().remove(startBtn);
            box.getChildren().add(makeButton("Play", "#00ffff", "#0d5cdb", 25, box.getPrefWidth()));
            box.getChildren().add(makeButton("Deck Editor", "#00ffff", "#0d5cdb", 25, box.getPrefWidth()));
            box.getChildren().add(makeButton("Shop", "#00ffff", "#0d5cdb", 25, box.getPrefWidth()));
            box.getChildren().add(makeButton("Settings", "#00ffff", "#0d5cdb", 25, box.getPrefWidth()));
            box.getChildren().add(makeButton("Quit", "#00ffff", "#0d5cdb", 25, box.getPrefWidth()));
        });
        startBtn.setOnMouseEntered(e -> {
            startBtn.setStyle("-fx-text-fill: #009999");
        });
        startBtn.setOnMouseExited(e -> {
            startBtn.setStyle("-fx-text-fill: #00ffff");
        });
        startBtn.setFont(Font.font("Endor", FontWeight.BOLD, 55));
        startBtn.setBackground(Background.EMPTY);
        startBtn.setStyle("-fx-text-fill: #00ffff");
                
        Media mainMenuBg = new Media(videoPath);
        Media mainMenuAudio = new Media(audioPath);

        MediaPlayer vidPlayer = new MediaPlayer(mainMenuBg);
        MediaPlayer audioPlayer = new MediaPlayer(mainMenuAudio);
        
        vidPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        vidPlayer.setStopTime(Duration.seconds(9));
        vidPlayer.setOnEndOfMedia(() -> vidPlayer.seek(Duration.seconds(0)));
        
        audioPlayer.setVolume(0.40);
        audioPlayer.setStartTime(Duration.seconds(1));
        audioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        audioPlayer.setOnEndOfMedia(() -> audioPlayer.seek(Duration.seconds(0)));
        
        MediaView audio = new MediaView(audioPlayer);
        MediaView video = new MediaView(vidPlayer);
        
        box.getChildren().addAll(mainTitle, startBtn);
        root.getChildren().addAll(audio, video);
        root.getChildren().add(box);
        
        vidPlayer.play();
        audioPlayer.play();
        
        return mainMenu;
    }
    
}
