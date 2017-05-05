package monster_battle_arena;

import java.net.URISyntaxException;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
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
public class GenerateGraphics {
    
    // Class for quickly generating graphical interface elements
    
    public Button makeButton(String text, String textColor, String bgColor, int fontSize, double minWidth)
    {
        // Makes a new button with specified text and CSS styling
        Button button = new Button(text);
        // Example of custom button background: -fx-background-image: url('/ImageAssets/mainBtnBg.png')
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
    
    private Effect makeDropShadow(Color colorOfShadow, int radius)
    {
        DropShadow shadow = new DropShadow();
        shadow.setColor(colorOfShadow);
        shadow.setRadius(radius);
        
        return shadow;
    }
    
    public Scene createMainMenu(Stage mainStage) throws URISyntaxException
    {
        String videoPath = getClass().getResource("/VideoAssets/mainMenuBg.mp4").toURI().toString();
        String audioPath = getClass().getResource("/AudioAssets/mainMenu_Winters Tale.mp3").toURI().toString();
        
        Group root = new Group();
    //  BorderPane window = new BorderPane();

        Scene mainMenu = new Scene(root, 1920, 1080);
        
        // Create a vertical box to hold menu elements in a single column.
        // makeVerticalBox parameters: spacing between nodes in Y-axis, posX, posY
        VBox vertBox = new VBox(40);
        vertBox.setAlignment(Pos.CENTER);
        vertBox.setPrefWidth(300);
    //  window.setCenter(box);
        
        // Insets parameters (padding space in pixels): top, right, bottom, left
        vertBox.setPadding(new Insets(200, 540, 100, 600));
        
        // Media players for both audio and video
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

        // Create text for main game title
        // makeText parameters: text content, text CSS styling, text font size
        Text mainTitle = makeText("Monster Battle Arena", "", 72);
        mainTitle.setStroke(Color.BLUE);
        mainTitle.setFill(Color.WHITE);
        
        // Make buttons to place on the main menu
        // makeButton parameters: button text content, text color, button color, text size, button width
        Button playBtn = makeButton("Play", "#00ffff", "#0d5cdb", 25, vertBox.getPrefWidth());
        Button editorBtn = makeButton("Deck Editor", "#00ffff", "#0d5cdb", 25, vertBox.getPrefWidth());
        Button shopBtn = makeButton("Shop", "#00ffff", "#0d5cdb", 25, vertBox.getPrefWidth());
        Button settingsBtn = makeButton("Settings", "#00ffff", "#0d5cdb", 25, vertBox.getPrefWidth());
        Button quitBtn = makeButton("Quit", "#00ffff", "#0d5cdb", 25, vertBox.getPrefWidth());
        
        playBtn.setOnMouseEntered(e -> playBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        playBtn.setOnMouseExited(e -> playBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        playBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        
        editorBtn.setOnMouseEntered(e -> editorBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        editorBtn.setOnMouseExited(e -> editorBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        editorBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        
        shopBtn.setOnMouseEntered(e -> shopBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        shopBtn.setOnMouseExited(e -> shopBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        shopBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        shopBtn.setOnAction(e -> {
            root.getChildren().remove(vertBox);
            Image shop_bg = new Image("/ImageAssets/shop_bg.png");
            ImageView imageView = new ImageView(shop_bg);
            root.getChildren().remove(video);
            root.getChildren().add(imageView);
        });
        
        settingsBtn.setOnMouseEntered(e -> settingsBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        settingsBtn.setOnMouseExited(e -> settingsBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        settingsBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        
        quitBtn.setOnMouseEntered(e -> quitBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        quitBtn.setOnMouseExited(e -> quitBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        quitBtn.setOnAction(e -> {
            // To-do: confirm exit pop-up and saving of player data
            Platform.exit();
            System.exit(0);
        });
        quitBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        
        Button startBtn = makeButton("Start", "#00ffff", "", 55, vertBox.getPrefWidth());
        startBtn.setOnMouseEntered(e -> startBtn.setStyle("-fx-text-fill: #009999"));
        startBtn.setOnMouseExited(e -> startBtn.setStyle("-fx-text-fill: #00ffff"));
        startBtn.setOnAction(e -> {
            vertBox.getChildren().remove(startBtn);
            vertBox.getChildren().addAll(playBtn, editorBtn, shopBtn, settingsBtn, quitBtn);
        });
        startBtn.setBackground(Background.EMPTY);

        /* Button array is made to hold all main menu buttons in a single
           variable so that they can all be more quickly passed to the 
           prepSubMenu() method */
    //  Button[] mainMenuButtons = {playBtn, editorBtn, shopBtn, settingsBtn};
        
        /* Prepare different scenes to be displayed on the mainStage when the
           action listeners for buttons within the mainMenuButton array are fired */
    //  prepSubMenus(mainMenu, mainStage, root, 0);

        // Add all of the nodes to the vertical box and then add to the root group
        vertBox.getChildren().addAll(mainTitle, startBtn);
        root.getChildren().addAll(audio, video);
        root.getChildren().add(vertBox);
        
        vidPlayer.play();
        audioPlayer.play();
        
        return mainMenu;
    }
    
    /* Method to prepare action listeners to display different sub-menus  
       that are passed in as an array of buttons with a groupID to differentiate
       between the different groups */
    private void prepSubMenus(Scene scene, Stage mainStage, Group root, int buttonGroupID)
    {
        switch (buttonGroupID)
        {
            case 0:
                
                break;
                
            case 1:
                break;
                
            default:
                System.out.println("[ERROR] buttonGroupID is invalid!");
                break;
        }
        
        System.out.println("[INFO] Sub-scenes loaded for groupID: " + buttonGroupID + " successfully");
    }

    
    
}
