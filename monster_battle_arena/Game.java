package monster_battle_arena;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.image.Image;

/*
 * @author Coleman Rogers
 */
public class Game {
    
    private Monster[] monsterList = new Monster[40];
    
    public void initGame(Player player) throws IOException, FileNotFoundException
    {
        // Array will contain monster data extraced from the text file as strings
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
            monsterData[i] = br.readLine();
            String[] convertedData = monsterData[i].split("[ ]");
            String monsterName = convertedData[0];
            int hitpoints = Integer.parseInt(convertedData[1]);
            int strength = Integer.parseInt(convertedData[2]);
            int defense = Integer.parseInt(convertedData[3]);
            int energyRequired = Integer.parseInt(convertedData[5]);
            int rarity = Integer.parseInt(convertedData[6]);
            
            Monster newCard = new Monster();
            monsterList[i] = newCard.initMonster(i, monsterName, hitpoints, strength, defense, energyRequired, rarity);
        }
        
        System.out.println("[INFO] Monster Data Successfully Loaded");
        for (int i = 0; i < monsterList.length; i++)
        {
            System.out.println("Name: " + monsterList[i].getMonsterName() + ", Hitpoints: "
                                        + monsterList[i].getHitpoints() + ", Strength: "
                                        + monsterList[i].getStrength() + ", Defense: "
                                        + monsterList[i].getDefense() + ", Energy Required: "
                                        + monsterList[i].getEnergyRequired() + ", Rarity: "
                                        + monsterList[i].getRarity());
        }
        
        System.out.println("[INFO] Generating player data...");
        player.setName("Bob");
        player.getPersonalCardDeck().add(monsterList[7]);
        
        System.out.println("[INFO] Player data successfully created");
    }

    public Monster[] getMonsterList()
    {
        return monsterList;
    }

    public void setMonsterList(Monster[] monsterList)
    {
        this.monsterList = monsterList;
    }

    public Image[] loadCardImages()
    {
        Image[] cardImages = new Image[monsterList.length];
        
        for (int i = 0; i < 8; i++)
        {
            try
            {
                cardImages[i] = new Image("/ImageAssets/Cards/card" + i + ".png", 260, 355, false, false);
            } catch (IllegalArgumentException err)
            {
                cardImages[i] = new Image("/ImageAssets/Cards/missingTexture.png", 260, 355, false, false);
            }
        }
        
        return cardImages;
    }
    
}
