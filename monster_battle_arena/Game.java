package monster_battle_arena;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.image.Image;

/*
 * @author Coleman Rogers
 */
public class Game {
    
    private Monster[] monsterList = new Monster[40];
    private File playerData = new File("playerData.txt");
    private boolean isBeginner = false;

    public void initGame(Player player) throws IOException, FileNotFoundException
    {
        // Array will contain monster data extracted from the text file as strings
        String[] monsterData = new String[40];
        
        // Input stream retrieves monsterData text file to be read from
        InputStream input = getClass().getResourceAsStream("monsterData.txt");
        
        // In the event this fails, print this pretty error message before crashing
        if (input == null)
            System.out.println("[ERROR] Input stream failed to retrieve monsterData.txt");
        
        // Create a reader for the file and pass it the input stream of the text file
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        
        // Loop to read a line from the text file and create a monster from the extracted data
        for (int i = 0; i < monsterList.length; i++)
        {
            // NOTE: convertedData[4] may be used later for ability to taunt
            monsterData[i] = br.readLine();
            String[] convertedData = monsterData[i].split(" ");
            String monsterName = convertedData[0];
            int hitpoints = Integer.parseInt(convertedData[1]);
            int strength = Integer.parseInt(convertedData[2]);
            int defense = Integer.parseInt(convertedData[3]);
            int energyRequired = Integer.parseInt(convertedData[5]);
            int rarity = Integer.parseInt(convertedData[6]);
            
            Monster newCard = new Monster();
            monsterList[i] = newCard.initMonster(i, monsterName, hitpoints, strength, defense, energyRequired, rarity);
        }
        
        System.out.println("[INFO] Monster data successfully loaded");
        
        /*  Commented out as this system works perfectly and checking it is not needed
        for (int i = 0; i < monsterList.length; i++)
        {
            System.out.println("Name: " + monsterList[i].getMonsterName() + ", Hitpoints: "
                                        + monsterList[i].getHitpoints() + ", Strength: "
                                        + monsterList[i].getStrength() + ", Defense: "
                                        + monsterList[i].getDefense() + ", Energy Required: "
                                        + monsterList[i].getEnergyRequired() + ", Rarity: "
                                        + monsterList[i].getRarity());
        }
        */
        
        // Player data loader
        try
        {
            Scanner scan = new Scanner(playerData);
            
            while (scan.hasNextLine())
            {
                String[] data = scan.nextLine().split(" ");
                
                switch(data[0])
                {
                    case "deck1:":
                    {
                        // Build custom deck 1
                        ArrayList <Monster> deck1 = new ArrayList<>();
                        
                        for (int i = 1; i < data.length; i++)
                            deck1.add(monsterList[Integer.parseInt(data[i])]);
                        
                        player.setCustomDeck1(deck1);
                        System.out.println("[INFO] Custom deck 1 loaded successfully");
                        break;
                    }
                    
                    case "deck2:":
                    {
                        // Build custom deck 2
                        ArrayList <Monster> deck2 = new ArrayList<>();
                        
                        for (int i = 1; i < data.length; i++)
                            deck2.add(monsterList[Integer.parseInt(data[i])]);
                        
                        player.setCustomDeck2(deck2);
                        System.out.println("[INFO] Custom deck 2 loaded successfully");
                        break;
                    }
                    
                    case "deck3:":
                    {
                        // Build custom deck 3
                        ArrayList <Monster> deck3 = new ArrayList<>();
                        
                        for (int i = 1; i < data.length; i++)
                            deck3.add(monsterList[Integer.parseInt(data[i])]);
                        
                        player.setCustomDeck3(deck3);
                        System.out.println("[INFO] Custom deck 3 loaded successfully");
                        break;
                    }
                    
                    case "name:":
                    {
                        player.setName(data[1]);
                        break;
                    }
                    
                    case "isNewPlayer:":
                    {
                        if (data[1].equals("1"))
                        {
                            isBeginner = true;
                            System.out.println("[INFO] New player, load beginner scene instead");
                        }
                        else
                            System.out.println("[INFO] Returning player");
                        
                        break;
                    }
                    
                    case "gemBalance:":
                    {
                        int gemBalance = Integer.parseInt(data[1]);
                        System.out.println("[INFO] Player gem balance is: " + gemBalance);
                        player.setGemBalance(gemBalance);
                        break;
                    }
                    
                    case "cardPool:":
                    {
                        ArrayList <Monster> cardPool = new ArrayList<>();
                        
                        for (int i = 1; i < data.length; i++)
                            cardPool.add(monsterList[Integer.parseInt(data[i])]);
                        
                        player.setCardPool(cardPool);
                        System.out.println("[INFO] Player card pool has been successfully loaded");
                        break;
                    }
                }
            }
            
        } catch (IOException err)
        {
            System.out.println("[ERROR] playerData.txt was not found!");
        }
        
        System.out.println("[INFO] Player data successfully loaded");
    }

    public Image[] loadCardImages()
    {
        Image[] cardImages = new Image[monsterList.length];
        
        // This line cuts 10-12 MB of active memory from the JVM heap by loading
        // this missing texture image only one time
        Image missingTexture = new Image("/ImageAssets/Cards/missingTexture.png", 260, 355, false, false);
        
        for (int i = 0; i < monsterList.length; i++)
        {
            try
            {
                cardImages[i] = new Image("/ImageAssets/Cards/card_" + i + ".png", 260, 355, false, false);
            } catch (IllegalArgumentException err)
            {
                cardImages[i] = missingTexture;
            }
        }
        
        System.out.println("[INFO] " + cardImages.length + " card images successfully loaded");
        return cardImages;
    }
    
    public Image[] loadCardBanners()
    {
        Image[] cardBanners = new Image[monsterList.length];
        
        // This saves about 5 MB of active memory from the JVM heap
        Image defaultBanner = new Image("/ImageAssets/shark.jpg", 460, 70, false, false);
        
        for (int i = 0; i < monsterList.length; i++)
        {
            try
            {
                cardBanners[i] = new Image("/ImageAssets/Cards/cardBanner" + i + ".png", 460, 70, false, false);
            } catch (IllegalArgumentException err)
            {
                cardBanners[i] = defaultBanner;
            }
        }
        
        System.out.println("[INFO] " + cardBanners.length + " card banners successfully loaded");
        return cardBanners;
    }
    
    public Monster[] getMonsterList()
    {
        return monsterList;
    }

    public void setMonsterList(Monster[] monsterList)
    {
        this.monsterList = monsterList;
    }
    
    public File getPlayerData()
    {
        return playerData;
    }

    public boolean isBeginner()
    {
        return isBeginner;
    }
    
}
