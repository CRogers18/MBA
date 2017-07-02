package monster_battle_arena;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * @author Coleman Rogers
 */
class Arena {
    
    private Player realPlayer, bot;
    private ArrayList <Monster> playerShuffle, botShuffle, playerHand, botHand, playerField, botField;
    private Monster[] monsterData;
    private Stack <Monster> playerPull = new Stack<>();
    private Stack <Monster> botPull = new Stack<>();
    
    // Get the contents of the arenaUI scene
    private Group arenaUI;
    private HBox playerFieldUI, botFieldUI, playerHandUI;
    
    private int currentTurn;
    private boolean hasWon = false;
    private final boolean willShuffle = true;
    
    // NOTE: Will likely need to pass whole scene for arena in here as well
    public Arena(Player p, Monster[] monsterList, Group sceneData)
    {
        // Import custom player data to this class and monsterList
        // to build the bot deck with
        this.realPlayer = p;
        monsterData = monsterList;
        this.arenaUI = sceneData;
        initBot();
        initDecks();
        unpackScene();
        playArenaGame();
        processWinner();
    //  cleanUp();
    }

    private void initBot()
    {
        bot = new Player();
        
        try
        {
            // Get bot data from aiData file
            InputStream is = getClass().getResourceAsStream("aiData.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            // Scan it in (first line only for now)
            String input = br.readLine();
            String[] data = input.split(" ");
            
            // Take first piece of data as name
            bot.setName(data[0]);
            
            ArrayList <Monster> botDeck = new ArrayList<>();
            
            // Use the rest of the data to build the bot's card deck
            for (int i = 1; i < data.length; i++)
                botDeck.add(monsterData[Integer.parseInt(data[i])]);
            
            // Set bot's custom deck to use as the deck built from the parsed data
            bot.setCustomDeck1(botDeck);
            
        } catch (FileNotFoundException err)
        {
            System.out.println("[ERROR] aiData.txt was not found!");
        } catch (IOException ex) {
            System.out.println("[ERROR] Reader failed to parse aiData.txt!");
        }
        
        System.out.println("[INFO] " + bot.getName() + " bot initialized successfuly!");
    }

    private void initDecks()
    {
        playerShuffle = realPlayer.getCustomDeck1();
        botShuffle = bot.getCustomDeck1();
        
        if (willShuffle)
        {
            int deckSize = bot.getCustomDeck1().size();
            
            // Durstenfeld shuffling algorithm provides O(n) random shuffle
            // Shuffle player deck
            for (int i = 0; i < deckSize; i++)
            {
                int j = ThreadLocalRandom.current().nextInt(i, deckSize);
                Monster temp = playerShuffle.get(j);
                playerShuffle.set(j, playerShuffle.get(i));
                playerShuffle.set(i, temp);
            }

            // Shuffle bot deck
            for (int i = 0; i < deckSize; i++)
            {
                int j = ThreadLocalRandom.current().nextInt(i, deckSize);
                Monster temp = botShuffle.get(j);
                botShuffle.set(j, botShuffle.get(i));
                botShuffle.set(i, temp);
            }

            // Add the player shuffled deck to the player pull stack
            System.out.println("\nPLAYER PULL DECK: ");
            for (Monster card : playerShuffle)
            {
                System.out.print(card.getMonsterName() + " ");
                playerPull.push(card);
            }

            // Add the bot shuffled deck to the bot pull stack
            System.out.println("\n\nBOT PULL DECK: ");
            for (Monster card : botShuffle)
            {
                System.out.print(card.getMonsterName() + " ");
                botPull.push(card);
            }

            System.out.println("\n\n[INFO] Card decks successfully shuffled!");
        }
    }
    
    private void unpackScene()
    {
        this.playerFieldUI = (HBox) arenaUI.getChildren().get(1);
        playerFieldUI.setOnMouseClicked(e -> cleanUp());
        playerFieldUI.setOnMouseEntered(e -> System.out.println("In playerField"));
        
        this.botFieldUI = (HBox) arenaUI.getChildren().get(2);
        botFieldUI.setOnMouseEntered(e -> System.out.println("In botField"));
        
        this.playerHandUI = (HBox) arenaUI.getChildren().get(3);
        playerHandUI.setOnMouseEntered(e -> System.out.println("In playerHand"));
    }

    private void playArenaGame()
    {
        // Coin-flip to determine who goes first
        Random coinFlip = new Random();
        currentTurn = (int) coinFlip.nextInt(2);
        System.out.println("[COIN-FLIP] " + currentTurn + " will go first");
        
        // Initialize all ArrayLists being used
        playerHand = new ArrayList<>();
        playerField = new ArrayList<>();
        botHand = new ArrayList<>();
        botField = new ArrayList<>();
        
        // Give each player 2 cards from the pull decks
        for (int i = 0; i < 2; i++)
        {
            Monster toAdd = playerPull.pop();
            addCardImage(toAdd, playerHandUI);
            playerHand.add(toAdd);
            
            toAdd = botPull.pop();
            addCardImage(toAdd, botFieldUI);
            botHand.add(toAdd);
        }
        
        // Print out cards in player and bot hands
        System.out.println("\nPlayer's starting hand: ");
        for (Monster card : playerHand)
            System.out.print(card.getMonsterName() + " ");
        
        System.out.println("\n\nBot's starting hand: ");
        for (Monster card : botHand)
            System.out.print(card.getMonsterName() + " ");
        
        System.out.println("");
        
        // Set health equal to whatever card they're using as the leader card,
        // since that hasn't been implemented yet, just set each healthbar to 50
        realPlayer.setGameHealth(50);
        bot.setGameHealth(50);
        
        // Start game here
        while (!hasWon)
        {
            
            switch (currentTurn)
            {
                // Real player turn
                case 0:
                    
                    // Check to see if someone has won
                    if (realPlayer.getGameHealth() <= 0 || bot.getGameHealth() <= 0)
                    {
                        hasWon = true;
                        break;
                    }
                    
                    System.out.println("\nReal player's turn");
                    
                    // If there are still cards to draw, draw them
                    if (!playerPull.isEmpty())
                        playerHand.add(playerPull.pop());
                    
                    // If there aren't penalize health or something until we
                    // figure things out
                    else
                    {
                        int currentHealth = realPlayer.getGameHealth();
                        currentHealth -= 5;
                        realPlayer.setGameHealth(currentHealth);
                        
                        if (currentHealth <= 0)
                        {
                            hasWon = true;
                            break;
                        }
                    }
                    
                    // Play game
                    if (!playerHand.isEmpty())
                    {
                        // Grab a card at random from the hand and play it
                        int randCard = ThreadLocalRandom.current().nextInt(0, playerHand.size());
                        Monster cardToPlay = playerHand.get(randCard);
                        playerHand.remove(randCard);
                        playerHand.trimToSize();
                        playerField.add(cardToPlay);
                        
                        // Print contents of the field
                        System.out.println("[PLAYER FIELD]");
                        for (Monster card : playerField)
                        {
                            // Have each card in the field attack the health pool
                            // of the opponent, ignore opponent field cards
                            System.out.print(card.getMonsterName() + " ");
                            int healthAfterDamage = bot.getGameHealth();
                            healthAfterDamage -= card.getStrength();
                            bot.setGameHealth(healthAfterDamage);
                        }
                    }
                    
                    // Print remaining bot healthbar on end of each turn and swap
                    System.out.println("\nBOT HEALTH: " + bot.getGameHealth() + "\n");
                    currentTurn = 1;
                    break;
                
                // Bot turn
                case 1:
                    
                    // Check to see if someone has won
                    if (realPlayer.getGameHealth() <= 0 || bot.getGameHealth() <= 0)
                    {
                        hasWon = true;
                        break;
                    }
                    
                    // If not, try to pull a card from the pull deck
                    System.out.println("\nBot's turn");
                    
                    if (!botPull.isEmpty())
                        botHand.add(botPull.pop());
                    
                    // If it is empty, lower health by 5
                    else
                    {
                        int currentHealth = bot.getGameHealth();
                        currentHealth -= 5;
                        bot.setGameHealth(currentHealth);
                        
                        // Check if game over condition is met
                        if (currentHealth <= 0)
                        {
                            hasWon = true;
                            break;
                        }
                    }
                    
                    // Play game
                    if (!botHand.isEmpty())
                    {
                        int randCard = ThreadLocalRandom.current().nextInt(0, botHand.size());
                        Monster cardToPlay = botHand.get(randCard);
                        botHand.remove(randCard);
                        botHand.trimToSize();
                        botField.add(cardToPlay);
                        
                        System.out.println("[BOT FIELD]");
                        for (Monster card : botField)
                        {
                            System.out.print(card.getMonsterName() + " ");
                            int healthAfterDamage = realPlayer.getGameHealth();
                            healthAfterDamage -= card.getStrength();
                            realPlayer.setGameHealth(healthAfterDamage);
                        }
                    }
                    
                    System.out.println("\nPLAYER HEALTH: " + realPlayer.getGameHealth() + "\n");
                    currentTurn = 0;
                    break;
                    
                default:
                    System.out.println("[ERROR] Turn not processed correctly!");
                    break;
            }
        }
    }

    private void processWinner()
    {
        if (realPlayer.getGameHealth() <= 0)
            System.out.println("[POST-GAME] You have been defeated!");
        
        if (bot.getGameHealth() <= 0)
        {
            // If player wins, give 100 gems
            System.out.println("[POST-GAME] You are victorious!");
            int winnings = realPlayer.getGemBalance();
            winnings += 100;
            realPlayer.setGemBalance(winnings);
            System.out.println("Gem balance = " + realPlayer.getGemBalance());
        }
    }

    private void cleanUp()
    {
        playerHandUI.getChildren().clear();
        botFieldUI.getChildren().clear();
        playerHandUI.getChildren().clear();
        
        System.out.println("[INFO] Image data has been cleared from the scene");
    }

    private void addCardImage(Monster toAdd, HBox whereToAdd)
    {
        try
        {
            ImageView card = new ImageView(new Image("/ImageAssets/Cards/card_" + toAdd.getMonsterID() + ".png", 260, 355, false, false));
            card.setOnMouseClicked(e -> {
                if (whereToAdd.equals(playerHandUI))
                {
                    playerFieldUI.getChildren().add(card);
                    playerHandUI.getChildren().remove(card);
                }
                else
                {
                    botFieldUI.getChildren().add(card);
                    // need to make bot hand ui variable
                }
            });
            whereToAdd.getChildren().add(card);
        } catch (IllegalArgumentException err)
        {
            ImageView card = new ImageView(new Image("/ImageAssets/Cards/missingTexture.png", 260, 355, false, false));
            whereToAdd.getChildren().add(card);
        }
    }

}
