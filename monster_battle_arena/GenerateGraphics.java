package monster_battle_arena;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

/*
 * @author Coleman Rogers
 */
public class GenerateGraphics {
    
    // Class for quickly generating graphical interface elements
    Timer timer = new Timer();
    
    private Group root, deckEditGroup, arenaGroup, mpSelectGroup;
    private MediaPlayer vidPlayer, mainMenuAudioPlayer, mpMenuAudioPlayer;
    private final Image[] cardImages, cardBanners;
    private ImageView[] deckEditorCardView, playBtns;
    private final Stage mainStage;
    private Scene mainMenu, shopMenu, deckEditorMenu, playMenu, arenaUI, mpMenu;
    private VBox cardBannerBox;

    private final Player player;
    private final File playerData;
    private final Path playerDataPath;
    private final Monster[] monsterList;
    private int cardPacksOpened = 0, pageNumber = 1, selectedDeck = 1, clickedBy, deckBeingUsed = 0;
    private boolean isMoving = false, bannerIsLoaded = false, inText = false, playBtnisClicked = false, isBeginner;
    private Text playerGemCount;
    private final String gameVersion = "v0.150";
    
    public GenerateGraphics(Player player, Monster[] monsters, Image[] cardImages, Image[] cardBanners, Stage mainStage, File playerData, boolean isBeginner)
    {
        this.player = player;
        this.monsterList = monsters;
        this.cardImages = cardImages;
        this.cardBanners = cardBanners;
        this.mainStage = mainStage;
        this.playerData = playerData;
        this.isBeginner = isBeginner;
        playerDataPath = Paths.get(playerData.getPath());
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
    
    public Scene createMainMenu() throws URISyntaxException
    {
        // Paths to video and audio assets
        String videoPath = getClass().getResource("/VideoAssets/mainMenuBg.mp4").toURI().toString();
        String mainMenuAudioPath = getClass().getResource("/AudioAssets/mainMenu_Winters Tale.mp3").toURI().toString();
        String mpMenuAudioPath = getClass().getResource("/AudioAssets/mpMenu_AngelofWar.mp3").toURI().toString();
        
        root = new Group();
        mainMenu = new Scene(root, 1920, 1080);
        
        // Create a vertical box to hold menu elements in a single column.
        // makeVerticalBox parameters: spacing between nodes in Y-axis, posX, posY
        VBox vertBox = new VBox(40);
        vertBox.setAlignment(Pos.CENTER);
        vertBox.setPrefWidth(300);
        
        // Insets parameters (padding space in pixels): top, right, bottom, left
        vertBox.setPadding(new Insets(100, 540, 100, 600));
        
        Media mainMenuBg = new Media(videoPath);
        Media mainMenuAudio = new Media(mainMenuAudioPath);
        Media mpMenuAudio = new Media(mpMenuAudioPath);
        
        // Media players for both audio and video
        vidPlayer = new MediaPlayer(mainMenuBg);
        mainMenuAudioPlayer = new MediaPlayer(mainMenuAudio);
        mpMenuAudioPlayer = new MediaPlayer(mpMenuAudio);
        
        /* Leak seems for now to be fixed, according to new RAM usage meter, 
           will still keep an eye on this section in the future */
        vidPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        vidPlayer.setStopTime(Duration.seconds(9));
                
        mainMenuAudioPlayer.setVolume(0.40);
        mainMenuAudioPlayer.setStartTime(Duration.seconds(1));
        mainMenuAudioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mainMenuAudioPlayer.setOnEndOfMedia(() -> mainMenuAudioPlayer.seek(Duration.seconds(0)));
        
        mpMenuAudioPlayer.setVolume(0.40);
        mpMenuAudioPlayer.setStartTime(Duration.seconds(1));
        mpMenuAudioPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mpMenuAudioPlayer.setOnEndOfMedia(() -> mpMenuAudioPlayer.seek(Duration.seconds(0)));
        
        MediaView audio = new MediaView(mainMenuAudioPlayer);
        MediaView video = new MediaView(vidPlayer);

        // Create text for main game title
        // makeText parameters: text content, text CSS styling, text font size
        Text mainTitle = makeText("Monster Battle Arena", "", 72);
        mainTitle.setStroke(Color.BLUE);
        mainTitle.setFill(Color.WHITE);
        
        Text version = new Text(gameVersion);
        version.setFont(Font.font("Times", FontWeight.BOLD, 20));
        version.setFill(Color.WHITE);
        version.relocate(1280, 200);
        
        // Make buttons to place on the main menu
        // makeButton parameters: button text content, text color, button color, text size, button width
        Button sPlayBtn = makeButton("Singleplayer", "#00ffff", "#0d5cdb", 25, vertBox.getPrefWidth());
        Button mPlayBtn = makeButton("Multiplayer", "#00ffff", "#0d5cdb", 25, vertBox.getPrefWidth());
        Button editorBtn = makeButton("Deck Editor", "#00ffff", "#0d5cdb", 25, vertBox.getPrefWidth());
        Button shopBtn = makeButton("Shop", "#00ffff", "#0d5cdb", 25, vertBox.getPrefWidth());
        Button settingsBtn = makeButton("Settings", "#00ffff", "#0d5cdb", 25, vertBox.getPrefWidth());
        Button quitBtn = makeButton("Quit", "#00ffff", "#0d5cdb", 25, vertBox.getPrefWidth());
        
        // Sets handler for what to do on mouse enter and exit, also add drop shadow effect
        sPlayBtn.setOnMouseEntered(e -> sPlayBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        sPlayBtn.setOnMouseExited(e -> sPlayBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        sPlayBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        
        mPlayBtn.setOnMouseEntered(e -> mPlayBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        mPlayBtn.setOnMouseExited(e -> mPlayBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        mPlayBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        
        editorBtn.setOnMouseEntered(e -> editorBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        editorBtn.setOnMouseExited(e -> editorBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        editorBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        
        shopBtn.setOnMouseEntered(e -> shopBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        shopBtn.setOnMouseExited(e -> shopBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        shopBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        
        final Scene arenaInit = createArenaUI();
        arenaUI = arenaInit;
        
        final Scene playMenuInit = createPlayMenu();
        playMenu = playMenuInit;
        
        sPlayBtn.setOnAction(e -> {
            mainStage.setScene(playMenu);
            vidPlayer.stop();
        });
        
        final Scene mpMenuInit = createMpMenu();
        mpMenu = mpMenuInit;
        
        mPlayBtn.setOnAction(e -> {
            mainStage.setScene(mpMenu);
            vidPlayer.stop();
            mainMenuAudioPlayer.pause();
            mpMenuAudioPlayer.play();
        });
        
        final Scene deckEditorMenuInit = createDeckEditorMenu();
        deckEditorMenu = deckEditorMenuInit;
        
        // What to do when the deck editor button is pressed
        editorBtn.setOnAction(e -> {
            updateDeckEditorCards();
            mainStage.setScene(deckEditorMenu);
            vidPlayer.stop();
        });
        
        final Scene shopMenuInit = createShopMenu();
        shopMenu = shopMenuInit;
        
        shopBtn.setOnAction(e -> {
            playerGemCount.setText(Integer.toString(player.getGemBalance()));
            mainStage.setScene(shopMenu);
            vidPlayer.stop();
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
            vertBox.getChildren().addAll(sPlayBtn, mPlayBtn, editorBtn, shopBtn, settingsBtn, quitBtn);
        });
        startBtn.setBackground(Background.EMPTY);

        // Add all of the nodes to the vertical box and then add to the root group
        vertBox.getChildren().addAll(mainTitle, startBtn);
        root.getChildren().addAll(audio, video, vertBox, version);
        
        // If launching direct to main menu, begin playback of music and video
        if (!isBeginner)
        {
            vidPlayer.play();
            mainMenuAudioPlayer.play();
        }
        
        return mainMenu;
    }
    
    public Scene createBeginnerScene()
    {
        Group bGroup = new Group();
        Scene bScene = new Scene(bGroup, 1920, 1080);
                
        ImageView bg = new ImageView(new Image("/ImageAssets/play_bg.jpg", 1920, 1080, false, false));
        
        HBox cardContainer = new HBox(20);
        
        VBox nodeContainer = new VBox(25);
        VBox firstPackContainer = new VBox(100);

        nodeContainer.relocate(470, 200);
        nodeContainer.setAlignment(Pos.CENTER);
        
        Text nameText = new Text("Enter a name for your player: ");
        nameText.setFont(Font.font("Arial", FontWeight.BOLD, 64));
        nameText.setFill(Color.WHITE);
        
        TextField playerName = new TextField();
        playerName.setTooltip(new Tooltip("Names are limited to 16 characters"));
        playerName.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
        playerName.setMaxWidth(450);
        playerName.setAlignment(Pos.CENTER);
        
        Text errorText = new Text("Error creating character!");
        errorText.setFont(Font.font("Arial", FontWeight.BOLD, 64));
        errorText.setFill(Color.RED);
        errorText.setStroke(Color.BLACK);
        errorText.setVisible(false);
        playerName.setOnMouseClicked(e -> {
            errorText.setVisible(false);
            playerName.clear();
        });
        
        Rectangle packOpenBg = new Rectangle();
        packOpenBg.relocate(0, 0);
        packOpenBg.setWidth(1920);
        packOpenBg.setHeight(1080);
        packOpenBg.setOpacity(0.95);
        packOpenBg.setVisible(false);
        
        Button submitBtn = makeButton("Continue", "#00ffff", "#0d5cdb", 25, 150);
        submitBtn.setOnMouseEntered(e -> submitBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        submitBtn.setEffect(makeDropShadow(Color.AQUA, 40));
        submitBtn.setOnAction(e -> {
            
            String playerText = playerName.getText().toLowerCase();
            int nameLength = playerText.length();
            
            // BAD WORDS! AVERT YOUR EYES!
            String[] bannedNames = new String[]{"admin", "mod", "fuck", "shit", "dick", 
                                                "develop", "owner", "hitle", "stalin", "hoe",
                                                "trump", "nigg", "nlgg", "holocau", "cunt", "bitch",
                                                "isis", "jesus", "hammed", "pussy", "jew", "racis", "kkk",
                                                "prophet", "fag", "gay", "homo", "porn", "anal", "slave"};
            boolean isBannedName = false;
            
            // If name is longer than 16 characters or empty, don't allow it
            if (nameLength > 17 || nameLength == 0)
            {
                isBannedName = true;
                errorText.setVisible(true);
            }
            
            if (!isBannedName)
            {
                // If it passes size constraints, check for banned words
                for (int i = 0; i < bannedNames.length; i++)
                {
                    if (playerText.contains(bannedNames[i]))
                    {
                            errorText.setVisible(true);
                            isBannedName = true;
                            break;
                    }
                }
            }
            
            if (!isBannedName)
            {
                if (errorText.isVisible())
                    errorText.setVisible(false);
                
                player.setName(playerName.getText());
                nodeContainer.getChildren().clear();
                
                Text welcomeText = new Text("Welcome to Monster Battle Arena: ");
                Text pNameText = new Text(player.getName());
                Text welcome2 = new Text("Get started by opening your first card pack!");
                Text flipTip = new Text("Click on each card to see what you get!");
                welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 64));
                pNameText.setFont(Font.font("Arial", FontWeight.BOLD, 64));
                welcome2.setFont(Font.font("Arial", FontWeight.BOLD, 64));
                flipTip.setFont(Font.font("Arial", FontWeight.BOLD, 64));
                welcomeText.setFill(Color.WHITE);
                pNameText.setFill(Color.WHITE);
                welcome2.setFill(Color.WHITE);
                flipTip.setFill(Color.WHITE);
                                
                int[] starterDeck = new int[]{0, 1, 2, 3, 4, 5, 6};
                ImageView starterPack = new ImageView(new Image("/ImageAssets/first_card_pack.png", 220, 300, false, false));
                starterPack.setEffect(makeDropShadow(Color.BLACK, 40));
                
                nodeContainer.setSpacing(100);
                nodeContainer.relocate(300, 200);
                nodeContainer.getChildren().addAll(welcomeText, pNameText, welcome2, starterPack);
                
                ImageView[] cardsOpened = new ImageView[starterDeck.length];

                for (int i = 0; i < starterDeck.length; i++)
                {
                    final int index = i;

                    cardsOpened[i] = new ImageView(new Image("/ImageAssets/Cards/card_back.png", 220, 300, false, false));
                    cardsOpened[i].setOnMouseClicked(eve -> {
                        
                        // Add the new monster to the player card pool
                        player.getCardPool().add(monsterList[starterDeck[index]]);
                        
                        // Update image graphic to show new monster
                        try 
                        {
                            cardsOpened[index].setImage(new Image("/ImageAssets/Cards/card_" + starterDeck[index] + ".png", 220, 300, false, false));
                        } catch (IllegalArgumentException err)
                        {
                            cardsOpened[index].setImage(new Image("/ImageAssets/Cards/missingTexture.png", 220, 300, false, false));
                        }
                    });
                }
                
                cardContainer.getChildren().addAll(cardsOpened);
                firstPackContainer.getChildren().addAll(cardContainer, submitBtn, flipTip);
                firstPackContainer.setAlignment(Pos.CENTER);
                firstPackContainer.relocate(100, 200);
                firstPackContainer.setDisable(true);
                firstPackContainer.setVisible(false);
                packOpenBg.setDisable(true);
                
                starterPack.setOnMouseClicked(ev -> {
                                        
                    firstPackContainer.setDisable(false);
                    firstPackContainer.setVisible(true);
                                        
                    packOpenBg.setVisible(true);
                    packOpenBg.setDisable(false);
                    
                    submitBtn.setOnAction(eve -> {
                        
                        // Write changes to playerData file
                        try
                        {
                            List <String> fileContents = new ArrayList<>(Files.readAllLines(playerDataPath, StandardCharsets.UTF_8));

                            String updatedStatus = "isNewPlayer: ", newStatus = Integer.toString(0);
                            updatedStatus += newStatus;
                            fileContents.set(0, updatedStatus);
                            
                            String updatedName = "name: ", newName = player.getName();
                            updatedName += newName;
                            fileContents.set(1, updatedName);

                            String updatedPool = "cardPool: ";

                            for (int i = 0; i < player.getCardPool().size(); i++)
                                updatedPool += Integer.toString(player.getCardPool().get(i).getMonsterID()) + " ";

                            fileContents.set(6, updatedPool);

                            Files.write(playerDataPath, fileContents, StandardCharsets.UTF_8);

                        } catch (IOException err)
                        {
                            System.out.println("{ERROR] Failed to write updated player status to player file");
                        }

                        vidPlayer.play();
                        mainMenuAudioPlayer.play();
                        mainStage.setScene(mainMenu);
                        
                        // If this is a player's first time playing, only make
                        // server connection attempt AFTER they pass the tutorial
                        GameClient gc = new GameClient(player);
                    });
                }); 
            }
        });
        
        nodeContainer.getChildren().addAll(nameText, playerName, submitBtn, errorText);
        bGroup.getChildren().addAll(bg, nodeContainer, packOpenBg, firstPackContainer);
        
        return bScene;
    }
    
    private Scene createPlayMenu()
    {
        Group playMenuGroup = new Group();
        playMenu = new Scene(playMenuGroup, 1920, 1080);
        
        Image play_bg = new Image("/ImageAssets/play_bg.jpg", 1920, 1080, false, false);
        ImageView play_menu_bg = new ImageView(play_bg);
        
        Text playText = makeText("Play", "", 60);
        playText.setFill(Color.CYAN);
        playText.setStroke(Color.BLUE);
        playText.setVisible(false);
        
        playBtns = new ImageView[3];
        
        DropShadow ds = new DropShadow();
        ds.setColor(Color.AQUA);
        ds.setRadius(40);
        
        Text descriptionBox = makeText("Select a game mode to learn more!", "", 32);
        descriptionBox.setFill(Color.CYAN);
        descriptionBox.relocate(60, 550);
        // creates the backdrop                      
        Rectangle backdrop = new Rectangle();
        backdrop.relocate(60, 550);
        backdrop.setWidth(1800);
        backdrop.setHeight(325);
        backdrop.setOpacity(0.65);
        
        for (int i = 0; i < 3; i++)
        {
            final int index = i;
            Image btn = new Image("/ImageAssets/PlayMenu/play_" + i + ".jpg", 400, 400, false, false);
            playBtns[i] = new ImageView(btn);
            // TODO: Look further into chaining effects with setInput() method
            playBtns[i].setEffect(makeDropShadow(Color.AQUA, 40));
            playBtns[i].setOnMouseEntered(e -> {
                
                // If the button has been clicked and the mouse enters a button
                // that wasn't the one selected, un-apply the fade effect
                if (playBtnisClicked && clickedBy != index)
                {
                    playBtnisClicked = false;
                    playBtns[clickedBy].setEffect(makeDropShadow(Color.CYAN, 40));
                    playText.setVisible(false);
                }
                
                // Create a new timer task to run
                TimerTask fadeEffect = new TimerTask()
                {
                    
                    // This task will apply a small fade effect to the image
                    // within the ImageView, so make a variable to track darkness
                    double brightnessVal = 0;
                    boolean textSet = false;
                    
                    @Override
                    public void run()
                    {
                        // If below a certain threshold, continue to darken image
                        if (brightnessVal > -0.65)
                        {
                            brightnessVal -= 0.05;
                            ColorAdjust darken = new ColorAdjust();
                            darken.setBrightness(brightnessVal);
                            darken.setInput(ds);
                            playBtns[index].setEffect(darken);
                        }

                        // textSet flag only allows a single call to relocate
                        // and setText methods rather than many as was before
                        else
                        {
                            if (!textSet)
                            {
                                switch (index)
                                {
                                    case 0:
                                        playText.relocate(290, 150);
                                        playText.setText("   Play\nCampaign");
                                        break;
                                    case 1:
                                        playText.relocate(750, 150);
                                        playText.setText("     Play\nTournament");
                                        break;
                                    case 2:
                                        playText.relocate(1310, 150);
                                        playText.setText("  Play\nCustom");
                                        break;
                                }
                                textSet = true;
                                playText.setDisable(true);
                                playText.setVisible(true);
                            }
                        }    
                    }
                };

                // Schedule task to run every 15 ms if no button has been clicked
                if (!playBtnisClicked)
                {
                    descriptionBox.setText("Select a game mode to learn more!");
                    timer.schedule(fadeEffect, 15, 15);
                }
                
                // If a button is active and it isn't the one the player selected
                // then go ahead and cancel the task so that the fade effect can
                // be applied to a new button
                if (playBtnisClicked && clickedBy != index)
                    fadeEffect.cancel();

                // When the mouse exits the image, set effect back to normal and
                // cancel the fadeEffect timer task
                playBtns[index].setOnMouseExited(ev -> {
                //    System.out.println("pbic: " + playBtnisClicked);
                    if (playBtnisClicked == false)
                    {
                        playBtns[index].setEffect(makeDropShadow(Color.CYAN, 40));
                        playText.setVisible(false);
                        fadeEffect.cancel();
                    }
                });
           
                // WIP listener for different images being clicked
                playBtns[index].setOnMouseClicked(ev -> {
                    
                    playBtnisClicked = true;
                    
                    switch (index)
                    {
                        // Campaign mode
                        case 0:
                            clickedBy = 0;
                        //    ds.setColor(Color.YELLOW);
                            descriptionBox.setText("Enter Campaign description here");
                            break;
                        
                        // Tournament mode
                        case 1:
                            clickedBy = 1;
                        //    ds.setColor(Color.YELLOW);
                            descriptionBox.setText("Enter Tournament description here");
                            break;
                        
                        // Custom mode
                        case 2:
                            // Make buttons to select custom deck, add new nodes
                            // to playMenuGroup, remove them if another mode is
                            // selected
                            clickedBy = 2;
                            
                            // Scene swap should happen here
                            
                            
                            // playMenu should be swapped with scene for arena
                            mainStage.setScene(arenaUI);
                            Arena gameArena = new Arena(player, monsterList, arenaGroup);
                        //  ds.setColor(Color.YELLOW);
                            descriptionBox.setText("Enter Custom description here");
                            break;
                            
                        default:
                            System.out.println("[ERROR] index: " + index + " not found!");
                            break;
                    }
                    
                });
                
            });
        }
                
        HBox box = new HBox(100);
        box.getChildren().addAll(playBtns);
        box.setPadding(new Insets(100, 200, 100, 200));
        
        Button backBtn = makeButton("Back", "#00ffff", "#0d5cdb", 25, 200);
        backBtn.relocate(760, 900);
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        backBtn.setOnAction(ev -> {
            mainStage.setScene(mainMenu);
            vidPlayer.play();
        });
        
        playMenuGroup.getChildren().addAll(play_menu_bg, box, backBtn, playText, backdrop, descriptionBox);
        
        return playMenu;
    }
    
    private Scene createShopMenu()
    {
        /* NOTE: Assets created by this class are NOT being removed on main
                 menu reload. This inefficiency will need to be addressed. */
        
        Group shopGroup = new Group();
        shopMenu = new Scene(shopGroup, 1920, 1080);
        
        HBox shopBox = new HBox(100);   
        Image standard_card_pack = new Image("/ImageAssets/standard_card_pack.png", 220, 300, false, false);
        Image ultra_card_pack = new Image("/ImageAssets/ultra_card_pack.png", 220, 300, false, false);
                
        ImageView scpImage = new ImageView(standard_card_pack);
        ImageView ucpImage = new ImageView(ultra_card_pack);
        HBox cardsOpened = new HBox(100);
        cardsOpened.relocate(500, 400);
        
        scpImage.setOnMouseEntered(e -> scpImage.setEffect(makeDropShadow(Color.AQUA, 40)));
        scpImage.setOnMouseExited(e -> scpImage.setEffect(null));
        
        ucpImage.setOnMouseEntered(e -> ucpImage.setEffect(makeDropShadow(Color.YELLOW, 40)));
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
        String gemCount = Integer.toString(player.getGemBalance());
        playerGemCount = new Text(gemCount);
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
            
            // Transparent recentangle for background for displaying pack contents
            Rectangle packOpenBg = new Rectangle();
            packOpenBg.relocate(0, 0);
            packOpenBg.setWidth(1920);
            packOpenBg.setHeight(1080);
            packOpenBg.setOpacity(0.95);
            
            // Add the pack opening background to the group when the pack is clicked
            shopGroup.getChildren().add(packOpenBg);
            
            int currentGemCount = player.getGemBalance();
            
            if (currentGemCount >= 100)
            {
                player.setGemBalance(currentGemCount-100);
                
                String newGemCount = Integer.toString(player.getGemBalance());
                playerGemCount.setText(newGemCount);

                RanNumGen openPack = new RanNumGen();
                int[] idsOpened = openPack.packOpening(player, false, monsterList);
                
                // Add cards opened face down first
                for (int i = 0; i < idsOpened.length; i++)
                {
                    ImageView cardDown = new ImageView(new Image("/ImageAssets/Cards/card_back.png", 220, 300, false, false));
                    final int index = i;
                    cardDown.setId(Integer.toString(idsOpened[i]));
                    
                    switch (monsterList[idsOpened[i]].getRarity())
                    {
                        case 3:
                            cardDown.setEffect(makeDropShadow(Color.GREEN, 40));
                            break;
                            
                        case 4:
                            cardDown.setEffect(makeDropShadow(Color.BLUE, 40));
                            break;
                            
                        case 5:
                            cardDown.setEffect(makeDropShadow(Color.GOLD, 40));
                            break;
                    }
                    
                    cardDown.setOnMouseClicked(ev -> {
                        try
                        {
                            cardDown.setImage(new Image("/ImageAssets/Cards/card_" + cardDown.getId() + ".png", 220, 300, false, false));
                        } catch (IllegalArgumentException err)
                        {
                            cardDown.setImage(new Image("/ImageAssets/Cards/missingTexture.png", 220, 300, false, false));
                        }
                    });
                    
                    cardsOpened.getChildren().add(cardDown);
                }
                
                // Add the new HBox to the group
                shopGroup.getChildren().add(cardsOpened);
                
                // Make an okay button to close the box displaying the cards obtained
                Button okayBtn = makeButton("Continue", "#424141", "#b2b2b2", 25, 300);
                okayBtn.relocate(750, 900);
                okayBtn.setOnAction(ev -> {
                    
                    // Empty out the previous cards displayed when the window is closed 
                    cardsOpened.getChildren().clear();
                    shopGroup.getChildren().removeAll(packOpenBg, cardsOpened, okayBtn);
                });
                shopGroup.getChildren().add(okayBtn);
                
                // Sort cards in ascending order, player card pool, by monster ID
                player.sortCardPool(player.getCardPool(), monsterList);

                cardPacksOpened += 1;
                System.out.println("Standard Opened. Card packs opened so far: " + cardPacksOpened);
            }
            
            else
                System.out.println("Insufficient funds to purchase pack!");
        });
        
        // Ultra card pack opening
        ucpImage.setOnMouseClicked(e -> {
            
            Rectangle packOpenBg = new Rectangle();
            packOpenBg.relocate(0, 0);
            packOpenBg.setWidth(1920);
            packOpenBg.setHeight(1080);
            packOpenBg.setOpacity(0.95);
            
            shopGroup.getChildren().add(packOpenBg);
            
            int currentGemCount = player.getGemBalance();
            
            if (currentGemCount >= 400)
            {
                player.setGemBalance(currentGemCount-400);

                String newGemCount = Integer.toString(player.getGemBalance());
                playerGemCount.setText(newGemCount);

                RanNumGen openPack = new RanNumGen();                
                int[] idsOpened = openPack.packOpening(player, false, monsterList);
                
                // Add cards opened face down first
                for (int i = 0; i < idsOpened.length; i++)
                {
                    ImageView cardDown = new ImageView(new Image("/ImageAssets/Cards/card_back.png", 220, 300, false, false));
                    final int index = i;
                    cardDown.setId(Integer.toString(idsOpened[i]));
                    
                    switch (monsterList[idsOpened[i]].getRarity())
                    {
                        case 3:
                            cardDown.setEffect(makeDropShadow(Color.GREEN, 40));
                            break;
                            
                        case 4:
                            cardDown.setEffect(makeDropShadow(Color.BLUE, 40));
                            break;
                            
                        case 5:
                            cardDown.setEffect(makeDropShadow(Color.GOLD, 40));
                            break;
                    }
                    
                    cardDown.setOnMouseClicked(ev -> {
                        try
                        {
                            cardDown.setImage(new Image("/ImageAssets/Cards/card_" + cardDown.getId() + ".png", 220, 300, false, false));
                        } catch (IllegalArgumentException err)
                        {
                            cardDown.setImage(new Image("/ImageAssets/Cards/missingTexture.png", 220, 300, false, false));
                        }
                    });
                    
                    cardsOpened.getChildren().add(cardDown);
                }
                
                // Add the new HBox to the group
                shopGroup.getChildren().add(cardsOpened);
                
                // Make an okay button to close the box displaying the cards obtained
                Button okayBtn = makeButton("Continue", "#424141", "#b2b2b2", 25, 300);
                okayBtn.relocate(750, 900);
                okayBtn.setOnAction(ev -> {
                    
                    // Empty out the previous cards displayed when the window is closed 
                    cardsOpened.getChildren().clear();
                    shopGroup.getChildren().removeAll(packOpenBg, cardsOpened, okayBtn);
                });
                shopGroup.getChildren().add(okayBtn);
                
                // Sort cards in ascending order, player card pool, by monster ID
                player.sortCardPool(player.getCardPool(), monsterList);

                cardPacksOpened += 1;
                System.out.println("Ultra Opened. Card packs opened so far: " + cardPacksOpened);
            }
            
            else
                // Might play an audio clip here in the future
                System.out.println("Insufficient funds to purchase pack!");
        });
        
        Button previous = makeButton("Back", "#00ffff", "#0d5cdb", 25, 200);
        previous.relocate(75, 900);
        previous.setOnMouseEntered(e -> previous.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        previous.setOnMouseExited(e -> previous.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        previous.setOnAction(ev -> {
            
            // Write changes to the player's updated gem balance when the shop is closed.
            // TODO: Move this code to be in the save method when the whole application is closed.
            try
            {
                List <String> fileContents = new ArrayList<>(Files.readAllLines(playerDataPath, StandardCharsets.UTF_8));
                
                String updatedBalance = "gemBalance: ", newBalance = Integer.toString(player.getGemBalance());
                updatedBalance += newBalance;
                fileContents.set(2, updatedBalance);
                
                String updatedPool = "cardPool: ";
                
                for (int i = 0; i < player.getCardPool().size(); i++)
                    updatedPool += Integer.toString(player.getCardPool().get(i).getMonsterID()) + " ";
                
                fileContents.set(6, updatedPool);
                
                Files.write(playerDataPath, fileContents, StandardCharsets.UTF_8);
                
            } catch (IOException err)
            {
                System.out.println("{ERROR] Failed to write updated gem count to player file");
            }
                        
            mainStage.setScene(mainMenu);
            vidPlayer.play();
        });
        
        Image shop_bg = new Image("/ImageAssets/shop_bg.jpg");
        ImageView shopView = new ImageView(shop_bg);

        shopBox.getChildren().addAll(scpImage, ucpImage);
        shopBox.setAlignment(Pos.CENTER);
        shopBox.relocate(690, 400);
        shopGroup.getChildren().addAll(shopView, backdrop, shopBox, priceBox1, priceBox2, playerGems, playerGemBox, previous);
        
        return shopMenu;
    }
    
    private Scene createDeckEditorMenu()
    {
        deckEditGroup = new Group();
        Scene deckEditorScene = new Scene(deckEditGroup, 1920, 1080);
        
        // Using a different BG for the time being, might switch back
        Image editor_bg = new Image("/ImageAssets/deckBuilderBG2.jpg");
            
        /* Deck editor has 8 spots to place cards, so make 8 
           containers to place the card images in. */
        deckEditorCardView = new ImageView[8];
        ImageView editorBgView = new ImageView(editor_bg);
        editorBgView.setId("Background_Image");
        
        // Container to hold card banners on right side of screen, uses  
        // 5 px of spacing between nodes in the vbox
        cardBannerBox = makeVerticalBox(5);
        
        // Insets parameters (padding space in pixels): top, right, bottom, left
        cardBannerBox.setPadding(new Insets(5, 0, 400, 1460));
        
        // Load custom deck1 in by default when the menu is opened
        for (Monster card : player.getCustomDeck1())
        {
            ImageView bannerImage = new ImageView(cardBanners[card.getMonsterID()]);
            bannerImage.setId(Integer.toString(card.getMonsterID()));
            bannerImage.setOnMouseClicked(ev -> cardBannerBox.getChildren().remove(bannerImage));
            cardBannerBox.getChildren().add(bannerImage);
        }

        /* Graphic for deck editor needs to be corrected, minor issues with
           alignment occur when placing cards at equal distance from one another */
        int imageSpacing = 105, graphicErrorSpacing = 2;

        for (int i = 0; i < 8; i++)
        {
            // Initialize each index of the ImageView array and apply darken effect
            if (i < 8)
            {
                ColorAdjust darken = new ColorAdjust();
                darken.setBrightness(-0.65);

                deckEditorCardView[i] = new ImageView(cardImages[i]);
                deckEditorCardView[i].setEffect(darken);
                deckEditorCardView[i].setCache(true);
                deckEditorCardView[i].setSmooth(true);
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
        
        // Custom deck selection circles (coord x, coord y, radius)
        Circle circle1 = new Circle(900, 960, 40);
        Circle circle2 = new Circle(1050, 960, 40);
        Circle circle3 = new Circle(1200, 960, 40);
        circle1.setFill(Color.BLUE);
        circle2.setFill(Color.BLUE);
        circle3.setFill(Color.BLUE);
        circle1.setEffect(makeDropShadow(Color.CYAN, 30));
        
        circle1.setOnMouseClicked(e -> {
            
            saveCurrentDeck(selectedDeck);
            
            selectedDeck = 1;
            circle1.setEffect(makeDropShadow(Color.CYAN, 30));
            circle2.setEffect(null);
            circle3.setEffect(null);
            
            // When button is pressed, clear current cardBanner and re-build it
            cardBannerBox.getChildren().clear();
            for (Monster card : player.getCustomDeck1())
            {
                ImageView bannerImage = new ImageView(cardBanners[card.getMonsterID()]);
                bannerImage.setId(Integer.toString(card.getMonsterID()));
                bannerImage.setOnMouseClicked(ev -> cardBannerBox.getChildren().remove(bannerImage));
                cardBannerBox.getChildren().add(bannerImage);
            }    
        });
        
        circle2.setOnMouseClicked(e -> {
            
            saveCurrentDeck(selectedDeck);
            
            selectedDeck = 2;
            circle2.setEffect(makeDropShadow(Color.CYAN, 30));
            circle1.setEffect(null);
            circle3.setEffect(null);
            
            cardBannerBox.getChildren().clear();
            for (Monster card : player.getCustomDeck2())
            {
                ImageView bannerImage = new ImageView(cardBanners[card.getMonsterID()]);
                bannerImage.setId(Integer.toString(card.getMonsterID()));
                bannerImage.setOnMouseClicked(ev -> cardBannerBox.getChildren().remove(bannerImage));
                cardBannerBox.getChildren().add(bannerImage);
            }  
        });
        
        circle3.setOnMouseClicked(e -> {
            
            // get current selected deck and save it first
            saveCurrentDeck(selectedDeck);
            
            // then modify the selected deck and clear the cardBannerBox
            selectedDeck = 3;
            circle3.setEffect(makeDropShadow(Color.CYAN, 30));
            circle1.setEffect(null);
            circle2.setEffect(null);
            
            cardBannerBox.getChildren().clear();
            for (Monster card : player.getCustomDeck3())
            {
                ImageView bannerImage = new ImageView(cardBanners[card.getMonsterID()]);
                bannerImage.setId(Integer.toString(card.getMonsterID()));
                bannerImage.setOnMouseClicked(ev -> cardBannerBox.getChildren().remove(bannerImage));
                cardBannerBox.getChildren().add(bannerImage);
            }
        });
        
        Button pageForward = makeButton("Page " + (pageNumber + 1), "#00ffff", "#0d5cdb", 25, 200);
        pageForward.setOnMouseEntered(e -> pageForward.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        pageForward.setOnMouseExited(e -> pageForward.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        pageForward.setEffect(makeDropShadow(Color.BLUE, 40));
        pageForward.relocate(500, 920);
        
        Button pageBack = makeButton("Page " + (pageNumber - 1), "#00ffff", "#0d5cdb", 25, 200);
        pageBack.setOnMouseEntered(e -> pageBack.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        pageBack.setOnMouseExited(e -> pageBack.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        pageBack.setEffect(makeDropShadow(Color.BLUE, 40));
        pageBack.relocate(100, 920);
        pageBack.setDisable(true);
        pageBack.setText("");

        pageBack.setOnAction(ev -> {     
            pageForward.setText("Page " + pageNumber);
            pageNumber -= 1;
            
            if (pageNumber == 1)
            {
                pageBack.setDisable(true);
                pageBack.setText("");
            }
            
            if (pageNumber > 1)
            {
                pageBack.setText("Page " + (pageNumber - 1));
                pageBack.setDisable(false);
            }
            
            // NOTE: Hard-coded value will need to be updated for deck expansions
            if (pageNumber == 7 && pageForward.isDisabled())
                pageForward.setDisable(false);
                
            updateDeckEditorCards();
        });
        
        pageForward.setOnAction(ev -> { 
            pageBack.setText("Page " + pageNumber);
            pageNumber += 1;
            
            // NOTE: Hard-coded value will need to be updated for deck expansions
            if (pageNumber == 8)
            {
                pageForward.setText("");
                pageForward.setDisable(true);
            }
            
            else
            {
                pageForward.setText("Page " + (pageNumber + 1));
                pageForward.setDisable(false);
            }
            
            if (pageNumber > 1)
                pageBack.setDisable(false);
            
            updateDeckEditorCards();
        });

        Button backBtn = makeButton("Save and Exit", "#00ffff", "#0d5cdb", 25, 300);
        backBtn.relocate(1540, 950);
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #07347c;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-text-fill: #00ffff; -fx-background-color: #0d5cbd;"));
        backBtn.setEffect(makeDropShadow(Color.BLUE, 40));

        backBtn.setOnAction(ev -> {
            
            saveCurrentDeck(selectedDeck);
            
            // Write custom deck changes to playerData.txt
            try
            {
                // Copy contents of playerData.txt into an array list
                List <String> deckContents = new ArrayList<>(Files.readAllLines(playerDataPath, StandardCharsets.UTF_8));

                // Go through each line for deck contents
                for (int i = 0; i < 3; i++)
                {
                    switch (i)
                    {
                        // Re-construct each custom deck line from the saved player changes
                        case 0:
                        {
                            String deckChanges = "deck1: ";

                            for (Monster card : player.getCustomDeck1())
                                deckChanges += Integer.toString(card.getMonsterID()) + " ";

                            deckContents.set(3, deckChanges);
                            break;
                        }

                        case 1:
                        {
                            String deckChanges = "deck2: ";

                            for (Monster card : player.getCustomDeck2())
                                deckChanges += Integer.toString(card.getMonsterID()) + " ";

                            deckContents.set(4, deckChanges);
                            break;
                        }

                        case 2:
                        {
                            String deckChanges = "deck3: ";

                            for (Monster card : player.getCustomDeck3())
                                deckChanges += Integer.toString(card.getMonsterID()) + " ";

                            deckContents.set(5, deckChanges);
                            break;
                        }
                    }
                }

                // Write deck changes to the playerData file
                Files.write(playerDataPath, deckContents, StandardCharsets.UTF_8);

            } catch (IOException err)
            {
                System.out.println("[ERROR] Failed to write changes to playerData.txt");
            }
            
            pageNumber = 1;
            pageForward.setText("Page " + (pageNumber + 1));
            pageForward.setDisable(false);
            pageBack.setText("");
            pageBack.setDisable(true);
            
            mainStage.setScene(mainMenu);
            vidPlayer.play();
        });

        // Add new assets to the scene's group and return the finished scene
        deckEditGroup.getChildren().addAll(editorBgView, cardBannerBox, backBtn, pageForward, pageBack, circle1, circle2, circle3);
        deckEditGroup.getChildren().addAll(deckEditorCardView);
        
        return deckEditorScene;
    }
    
    // FURTHER OPTIMIZATIONS MAY EXIST IN THIS METHOD //
    private void updateDeckEditorCards()
    {
        // Page-dependent card ID calculations
        int lower = ((pageNumber - 1) * 8);
        int upper = lower + 7;
        
        // Since we don't have a page 0, set it to 1 if it hits 0 or lower
        if (pageNumber <= 0)
            pageNumber = 1;
        
        // cardCheck is the ID of the top-left card on the deck editor page
        int imageSpacing = 105, graphicErrorSpacing = 2, cardCheck = lower;
        
        for (int i = 0; i < 8; i++)
        {
            ColorAdjust darken = new ColorAdjust();
            darken.setBrightness(-0.65);

            deckEditorCardView[i].setImage(cardImages[cardCheck]);
            
            // Clear the mouse entered, exited, and clicked event handlers
            deckEditorCardView[i].setOnMouseEntered(e -> {});
            deckEditorCardView[i].setOnMouseExited(e -> {});
            deckEditorCardView[i].setOnMouseClicked(e -> {});
            
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
            cardCheck++;
        }
        
        // Loop to check player's card pool against the current page of the deck editor
        int currentIDChecked = 0;
        for (int i = 0; i < player.getCardPool().size(); i++)
        {
            int currentID = player.getCardPool().get(i).getMonsterID();
            
            // Optimization for skipping previously checked cards and cards outside
            // the range of cards on the current page of the deck editor
            if (currentID < lower)
                continue;
            
            if (currentID > upper)
                break;
            
            if (currentIDChecked == currentID && currentID > 0)
                continue;
            
            /*  Un-comment if debugging this loop
            System.out.println("[INFO] Checking for monsterID: " + currentID + " on page " + pageNumber
                   + " between range " + lower + " -> " + upper);
            */
            
            if (currentID >= lower && currentID <= upper)
            {
                deckEditorCardView[currentID % 8].setEffect(null);
                deckEditorCardView[currentID % 8].setOnMouseEntered(e -> deckEditorCardView[currentID % 8].setEffect(makeDropShadow(Color.BLUE, 60)));
                deckEditorCardView[currentID % 8].setOnMouseExited(e -> deckEditorCardView[currentID % 8].setEffect(null));
                deckEditorCardView[currentID % 8].setOnMouseClicked(e -> {
                    
                    int count = 0;

                    for (Node image : cardBannerBox.getChildren())
                        if (image instanceof ImageView)
                            count++;
                    
                    // TO-DO: needed to catch and handle duplicate cards in side menu
                    // if (currentID == Integer.parseInt(image.getId()))
                                
                    if (count < 12)
                    {
                        ImageView banner = new ImageView(cardBanners[currentID]);
                        banner.setOnMouseClicked(ev -> cardBannerBox.getChildren().remove(banner));
                        banner.setId(Integer.toString(currentID));
                    //  Un-comment to debug card banner creation
                    //  System.out.println("Banner created with ID " + banner.getId());
                        cardBannerBox.getChildren().add(banner);
                    }
                });

            currentIDChecked = currentID;
            }
        }
    }

    private void saveCurrentDeck(int selectedDeck)
    {        
        switch (selectedDeck)
        {
            case 1:
                
                ArrayList <Monster> customDeck1 = new ArrayList <>();
                for (Node card : cardBannerBox.getChildren())
                    if (card instanceof ImageView)
                        customDeck1.add(monsterList[Integer.parseInt(card.getId())]);
                
                player.setCustomDeck1(customDeck1);
                
                break;
            
            case 2:
                
                ArrayList <Monster> customDeck2 = new ArrayList <>();
                for (Node card : cardBannerBox.getChildren())
                    if (card instanceof ImageView)
                        customDeck2.add(monsterList[Integer.parseInt(card.getId())]);
                
                player.setCustomDeck2(customDeck2);
                
                break;
                
            case 3:
                
                ArrayList <Monster> customDeck3 = new ArrayList <>();
                for (Node card : cardBannerBox.getChildren())
                    if (card instanceof ImageView)
                        customDeck3.add(monsterList[Integer.parseInt(card.getId())]);
                
                player.setCustomDeck3(customDeck3);
                
                break;
                
            default:
                System.out.println("[ERROR] Selected deck not valid!");
                break;
        }
    }

    private Scene createArenaUI()
    {
        arenaGroup = new Group();
        Scene arenaUI = new Scene(arenaGroup, 1920, 1080, false, SceneAntialiasing.BALANCED);
        
        Image arena_bg = new Image("/ImageAssets/mba_arena.png");
        ImageView arenaBgView = new ImageView(arena_bg);
        
        // NOTE: new Insets (top, right, bottom, left) spacing is in pixels
        
        // Make 2 containers for card images to be placed in
        HBox playerField = new HBox(20);
        playerField.setPadding(new Insets(50, 300, 10, 300));
        playerField.relocate(226, 350);
        
        HBox botField = new HBox(50);
        botField.setPadding(new Insets(50, 300, 30, 300));
        botField.relocate(226, 220);
        botField.setOnMouseClicked(e -> { mainStage.setScene(mainMenu); vidPlayer.play(); });
        
        // Box to display player cards in hand
        HBox playerHand = new HBox(0);
        playerHand.setPadding(new Insets(0, 300, 0, 200));
        
        // TODO: will need to change variably with respect to # of cards in hand
        playerHand.relocate(464, 876);
        
        HBox botHand = new HBox(10);
        botHand.setPadding(new Insets(5, 300, 0, 300));
        botHand.relocate(300, -100);
        arenaGroup.getChildren().addAll(arenaBgView, playerField, botField, playerHand, botHand);
        
        return arenaUI;
    }

    private Scene createMpMenu()
    {
        mpSelectGroup = new Group();
        Scene mp = new Scene(mpSelectGroup, 1920, 1080);
        
        MediaView mpAudio = new MediaView(mpMenuAudioPlayer);
        
        ImageView mp_bg = new ImageView(new Image("/ImageAssets/mp_bg_1.jpg"));
        
        VBox deckSelect = new VBox(20);
        deckSelect.relocate(1450, 775);
        deckSelect.setMaxHeight(10);
        
        for (int i = 0; i < 3; i++)
        {
            final int index = i;
            Button btn = makeButton("Use Deck " + (i + 1), "#00ffff", "#0d5cdb", 20, 300);
            btn.setId(Integer.toString(index));
            btn.setOnAction(e -> {
                deckSelect.getChildren().get(deckBeingUsed).setEffect(null);
                btn.setEffect(makeDropShadow(Color.CYAN, 40));
                deckBeingUsed = Integer.parseInt(btn.getId());
            });
            deckSelect.getChildren().add(btn);
        }
        // Auto-selects deck 1, when sending deckBeingUsed to the server, make sure
        // to add 1 to the value to avoid confusion as decks start at 1 but group indices start at 0
        deckSelect.getChildren().get(0).setEffect(makeDropShadow(Color.CYAN, 40));
        
        // TODO: Move server connection code to here instead of making it automatic
        
        // Get ping to report beforehand then post it here
        String ping = "57ms";
        
        Text serverInfo = new Text("MBA Server 1\nPing: " + ping);
        serverInfo.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        serverInfo.setFill(Color.WHITE);
        serverInfo.setVisible(false);
        
        Rectangle infoBox = new Rectangle();
        infoBox.setFill(Color.BLACK);
        infoBox.setOpacity(0.65);
        infoBox.setWidth(200);
        infoBox.setHeight(75);
        infoBox.setVisible(false);
        
        Ellipse[] portals = new Ellipse[3];
        for (int i = 0; i < 3; i++)
        {
            Ellipse portal = null;
            
            if (i == 0)
            {
                portal = new Ellipse(815, 400, 229, 350);
                portal.setRotate(-10.0);
            }
            
            if (i == 1)
            {
                portal = new Ellipse(1536, 338, 52, 105);
                portal.setRotate(-3.0);
            }
            
            if (i == 2)
                portal = new Ellipse(297, 254, 24, 51);
            
            portal.setFill(Color.TRANSPARENT);
            // no worries, portal will be initialized before the pointer is 
            // de-referenced at this line
            portal.setOnMouseEntered(e -> {
                serverInfo.setText("MBA Server 1\nPing: " + ping);
                infoBox.setVisible(true);
                serverInfo.setVisible(true);
            });
            
            portal.setOnMouseMoved(e -> {
                double mouseX = e.getSceneX(), mouseY = e.getSceneY();
                infoBox.relocate(mouseX + 25, mouseY - 75);
                serverInfo.relocate(infoBox.getLayoutX() + 10, infoBox.getLayoutY() + 10);
            });
            
            portal.setOnMouseExited(e -> {
                infoBox.setVisible(false);
                serverInfo.setVisible(false);
            });
            
            portal.setOnMouseClicked(e -> {
                vidPlayer.play();
                mpMenuAudioPlayer.stop();
                mainMenuAudioPlayer.play();
                mainStage.setScene(mainMenu);
            });
            
            portals[i] = portal;
        }
        
        mpSelectGroup.getChildren().addAll(mp_bg, deckSelect);
        mpSelectGroup.getChildren().addAll(portals);
        mpSelectGroup.getChildren().addAll(infoBox, serverInfo, mpAudio);
        
        return mp;
    }
}