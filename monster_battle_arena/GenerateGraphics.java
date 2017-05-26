package monster_battle_arena;

import java.net.URISyntaxException;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    
    private Group mainMenuGroup;
    private final Player player;
    private final Monster[] monsterList;
    private int cardPacksOpened = 0;
    private boolean isMoving = false;
    
    public GenerateGraphics(Player player, Monster[] monsters)
    {
        this.player = player;
        this.monsterList = monsters;
    }
    
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
        // Paths to video and audio assets
        String videoPath = getClass().getResource("/VideoAssets/mainMenuBg.mp4").toURI().toString();
        String audioPath = getClass().getResource("/AudioAssets/mainMenu_Winters Tale.mp3").toURI().toString();
        
        Group root = new Group();

        Scene mainMenu = new Scene(root, 1920, 1080);
        
        // Create a vertical box to hold menu elements in a single column.
        // makeVerticalBox parameters: spacing between nodes in Y-axis, posX, posY
        VBox vertBox = new VBox(40);
        vertBox.setAlignment(Pos.CENTER);
        vertBox.setPrefWidth(300);
        
        // Insets parameters (padding space in pixels): top, right, bottom, left
        vertBox.setPadding(new Insets(200, 540, 100, 600));
        
        Media mainMenuBg = new Media(videoPath);
        Media mainMenuAudio = new Media(audioPath);
        
        // Media players for both audio and video
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
        
        // Sets handler for what to do on mouse enter and exit, also add drop shadow effect
        playBtn.setOnMouseEntered(e -> playBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        playBtn.setOnMouseExited(e -> playBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        playBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        
        editorBtn.setOnMouseEntered(e -> editorBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        editorBtn.setOnMouseExited(e -> editorBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        editorBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        
        // What to do when the deck editor button is pressed
        editorBtn.setOnAction(e -> {
            
            // Remove the box containing main menu elements and load new assets
            root.getChildren().removeAll(vertBox, video);
            Image editor_bg = new Image("/ImageAssets/deckBuilder_bg.png");
            
            /* Deck editor has 8 spots to place cards, so make 8 
               containers to place the card images in. */
            ImageView[] deckEditorCardView = new ImageView[8];
            Image[] deckEditorCardImages = new Image[monsterList.length];
            ImageView editorBgView = new ImageView(editor_bg);
            
            /* Graphic for deck editor needs to be corrected, minor issues with
               alignment occur when placing cards at equal distance from one another */
            int imageSpacing = 105, graphicErrorSpacing = 2;
            
            // THIS HAS A HEFTY PERFORMANCE PENALTY, NEED TO LOOK INTO OPTIMIZATIONS
            for (int i = 0; i < 8; i++)
            {
                /* TO-DO: Have placeholder graphics for other cards so we can
                   load these images as "card + i + .png" rather than hardcode 39.
                   Card graphic is downsized for now, but in future will be correctly sized */
                try 
                {
                    deckEditorCardImages[i] = new Image("/ImageAssets/Cards/card" + i + ".png", 260, 355, false, false);
                } catch (IllegalArgumentException err)
                {
                    deckEditorCardImages[i] = new Image("/ImageAssets/Cards/missingTexture.png", 260, 355, false, false);
                }
                
                // For the first 8 card images, set the ImageView to display the image
                if (i < 8)
                {
                    ColorAdjust darken = new ColorAdjust();
                    darken.setBrightness(-0.65);
            
                    deckEditorCardView[i] = new ImageView(deckEditorCardImages[i]);
                    deckEditorCardView[i].setEffect(darken);
                    deckEditorCardView[i].setCache(true);
                    deckEditorCardView[i].setCacheHint(CacheHint.SPEED);
  
                    if (i == 4)
                        imageSpacing = 105;
                    
                    // A lot of stupid code to fix a slightly off graphical background
                    if (i < 4)
                    {
                        deckEditorCardView[i].relocate(imageSpacing, 75);
                        
                        if (i == 2)
                            deckEditorCardView[i].relocate(++imageSpacing, 75);
                        
                        if (i == 3)
                            deckEditorCardView[i].relocate(imageSpacing + graphicErrorSpacing, 75);
                    }
                    
                    else if (i >= 4)
                    {
                        deckEditorCardView[i].relocate(imageSpacing, 515);
                        
                        if (i > 5)
                            deckEditorCardView[i].relocate(imageSpacing + graphicErrorSpacing, 515);
                    }

                    imageSpacing += 325;
                }
            }
            
            for (int i = 0; i < player.getPersonalCardDeck().size(); i++)
            {
                    int currentID = player.getPersonalCardDeck().get(i).getMonsterID();
                    System.out.println("Checking monsterID: " + player.getPersonalCardDeck().get(i).getMonsterID());
                    
                    // For the time being limit to first page of deck editor
                    if (currentID > 7)
                        continue;

                    deckEditorCardView[currentID].setEffect(null);
            }
            
            

            /* Commented out at the moment since it was just for testing purposes
            // Set an event handler for mouse clicking on the card
            cardView.setOnMouseClicked(ev -> {
                
                // Invert variable for whether card is moving each time it is clicked
                isMoving = !isMoving;
                    
                    // Event handler for mouse movement
                    cardView.setOnMouseMoved(eve -> {
                        
                        // If the card should be moving, get mouse coords and put the card there
                        if (isMoving)
                        {
                            double mouseX = eve.getSceneX(), mouseY = eve.getSceneY();
                            cardView.relocate(mouseX - card39.getWidth()/2, mouseY - card39.getHeight()/2);
                        }
                    });
                    
                    // Handles rapid mouse movement that can sometimes exit the bounds
                    // of the cardView object before setOnMouseMove event can be fired, 
                    // resulting in the card no longer being moved to the mouse coords 
                    cardView.setOnMouseExited(even -> {
                        
                        if (isMoving)
                        {
                            double mouseX = even.getSceneX(), mouseY = even.getSceneY();
                            cardView.relocate(mouseX - card39.getWidth()/2, mouseY - card39.getHeight()/2);
                        }
                    });
            });
            */
            
            Button backBtn = makeButton("Save and Exit", "#00ffff", "#0d5cdb", 25, 300);
            backBtn.relocate(1540, 950);
            
            backBtn.setOnAction(ev -> {
                root.getChildren().removeAll(editorBgView, backBtn);
                
                for (int i = 0; i < deckEditorCardView.length; i++)
                    root.getChildren().remove(deckEditorCardView[i]);
                
                root.getChildren().addAll(video, vertBox);
            });
            
            // Add new assets
            root.getChildren().addAll(editorBgView, backBtn);
            
            for (int i = 0; i < deckEditorCardView.length; i++)
                    root.getChildren().add(deckEditorCardView[i]);
            
        });
        
        shopBtn.setOnMouseEntered(e -> shopBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        shopBtn.setOnMouseExited(e -> shopBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        shopBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        shopBtn.setOnAction(e -> {
            root.getChildren().remove(vertBox);
            Image shop_bg = new Image("/ImageAssets/shop_bg.png");
            ImageView imageView = new ImageView(shop_bg);
            imageView.setOnMouseClicked(ev -> {
                root.getChildren().remove(imageView);
                root.getChildren().addAll(video, vertBox);
            });
            root.getChildren().remove(video);
            root.getChildren().add(imageView);
            createShopMenu(root);
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

        // Add all of the nodes to the vertical box and then add to the root group
        vertBox.getChildren().add(mainTitle);
        root.getChildren().addAll(audio, video);
        root.getChildren().add(vertBox);
        
        // Private variable holds a copy of the main menu group for quick reloading
        mainMenuGroup = root;
        vertBox.getChildren().add(startBtn);
        
        vidPlayer.play();
        audioPlayer.play();
        
        return mainMenu;
    }
    
    private void createShopMenu(Group root)
    {
        /* NOTE: Assets created by this class are NOT being removed on main
                 menu reload. This inefficiency will need to be addressed. */

        HBox shopBox = new HBox(100);
        Image standard_card_pack = new Image("/ImageAssets/standard_card_pack.png", 220, 300, false, false);
        Image ultra_card_pack = new Image("/ImageAssets/ultra_card_pack.png", 220, 300, false, false);
                
        ImageView scpImage = new ImageView(standard_card_pack);
        ImageView ucpImage = new ImageView(ultra_card_pack);
        
        scpImage.setOnMouseEntered(e -> scpImage.setEffect(makeDropShadow(Color.AQUA, 40)));
        scpImage.setOnMouseExited(e -> scpImage.setEffect(null));
        
        ucpImage.setOnMouseEntered(e -> ucpImage.setEffect(makeDropShadow(Color.AQUA, 40)));
        ucpImage.setOnMouseExited(e -> ucpImage.setEffect(null));
        
        // Transparent rectangle for background for card packs
        Rectangle backdrop = new Rectangle();
        backdrop.relocate(600, 300);
        backdrop.setWidth(700);
        backdrop.setHeight(500);
        backdrop.setOpacity(0.60);
        
        // Transparent recentangle for background for player gem count
        Rectangle playerGems = new Rectangle();
        playerGems.relocate(30, 30);
        playerGems.setWidth(230);
        playerGems.setHeight(75);
        playerGems.setOpacity(0.60);
        
        // Box for player gem count
        HBox playerGemBox = new HBox(10);
        playerGemBox.relocate(50, 50);
        Image gemIcon = new Image("/ImageAssets/gem_currency.png", 40, 40, false, false);
        ImageView gemView1 = new ImageView(gemIcon);
        String gemCount = Integer.toString(player.getGold());
        Text playerGemCount = new Text(gemCount);
        playerGemCount.setFill(Color.WHITE);
        playerGemCount.setFont(Font.font("Serif", FontWeight.BOLD, 30));
        playerGemBox.getChildren().addAll(playerGemCount, gemView1);
        
        // Price box 1 for standard card pack
        HBox priceBox1 = new HBox(10);
        priceBox1.setPadding(new Insets(0, 0, 0, 85));
        ImageView gemView2 = new ImageView(gemIcon);
        ImageView gemView3 = new ImageView(gemIcon);

        Text s_Price = new Text("100");
        s_Price.setFill(Color.WHITE);
        s_Price.setFont(Font.font("Serif", FontWeight.BOLD, 30));

        priceBox1.getChildren().addAll(s_Price, gemView2);
        priceBox1.setAlignment(Pos.CENTER);
        priceBox1.relocate(680, 710);
        
        // Price Box 2 for ultra card pack
        HBox priceBox2 = new HBox(10);
        priceBox2.setPadding(new Insets(0, 0, 0, 85));
        
        Text u_Price = new Text("400");
        u_Price.setFill(Color.WHITE);
        u_Price.setFont(Font.font("Serif", FontWeight.BOLD, 30));
        
        priceBox2.getChildren().addAll(u_Price, gemView3);
        priceBox2.setAlignment(Pos.CENTER);
        priceBox2.relocate(990, 710);
        
        // Standard card pack opening
        scpImage.setOnMouseClicked(e -> {
            int currentGemCount = player.getGold();
            
            if (currentGemCount >= 100)
            {
                player.setGold(currentGemCount-100);

                String newGemCount = Integer.toString(player.getGold());
                playerGemCount.setText(newGemCount);

                RanNumGen openPack = new RanNumGen();
                openPack.packOpening(player, false, monsterList);

                cardPacksOpened += 1;
                System.out.println("Standard Opened. Card packs opened so far: " + cardPacksOpened);
                
                for (int i = 0; i < player.getPersonalCardDeck().size(); i++)
                    System.out.println("Card " + i + " is " + player.getPersonalCardDeck().get(i).getMonsterName());
            }
            
            else
                System.out.println("Insufficient funds to purchase pack!");
            
        });
        
        // Ultra card pack opening
        ucpImage.setOnMouseClicked(e -> {
            int currentGemCount = player.getGold();
            
            if (currentGemCount >= 400)
            {
                player.setGold(currentGemCount-400);

                String newGemCount = Integer.toString(player.getGold());
                playerGemCount.setText(newGemCount);

                RanNumGen openPack = new RanNumGen();
                openPack.packOpening(player, true, monsterList);

                cardPacksOpened += 1;
                System.out.println("Ultra Opened. Card packs opened so far: " + cardPacksOpened);
                
                for (int i = 0; i < player.getPersonalCardDeck().size(); i++)
                    System.out.println("Card " + i + " is " + player.getPersonalCardDeck().get(i).getMonsterName());
            }
            
            else
                // Might play an audio clip here in the future
                System.out.println("Insufficient funds to purchase pack!");
            
        });
        
        shopBox.getChildren().addAll(scpImage, ucpImage);
        shopBox.setAlignment(Pos.CENTER);
        shopBox.relocate(690, 400);
        root.getChildren().addAll(backdrop, shopBox, priceBox1, priceBox2, playerGems, playerGemBox);
    }

}