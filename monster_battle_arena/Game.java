package monster_battle_arena;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ThreadLocalRandom;

/*
 * @author Coleman Rogers
 */
public class Game {
    
    private Monster[] monsterList = new Monster[12];
    
    public void initGame() throws IOException, FileNotFoundException
    {
        // Array will contain monster data extraced from the text file as strings
        String[] monsterData = new String[12];
        
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
            
            Monster newCard = new Monster();
            monsterList[i] = newCard.initMonster(i, monsterName, hitpoints, strength, defense, energyRequired);
        }
        
        System.out.println("[INFO] Monster Data Successfully Loaded");
        for (int i = 0; i < monsterList.length; i++)
        {
            System.out.println("Name: " + monsterList[i].getMonsterName() + ", Hitpoints: "
                                        + monsterList[i].getHitpoints() + ", Strength: "
                                        + monsterList[i].getStrength() + ", Defense: "
                                        + monsterList[i].getDefense() + ", Energy Required: "
                                        + monsterList[i].getEnergyRequired());
        }
        
        System.out.println("[INFO] Generating player data...");
        Player player = new Player();
        player.setName("Bob");
        
        for (int i = 0; i < 3; i++)
        {
            /* Randomly pick 3 cards for the player to start with from the
               specified range of cards. Range values are [x,y) */
            switch (i)
            {
                case 0:
                    int j = ThreadLocalRandom.current().nextInt(0, 4);
                    player.getPersonalCardDeck().add(monsterList[j]);
                    break;
                    
                case 1:
                    int k = ThreadLocalRandom.current().nextInt(4, 8);
                    player.getPersonalCardDeck().add(monsterList[k]);
                    break;
                    
                case 2:
                    int l = ThreadLocalRandom.current().nextInt(8, 12);
                    player.getPersonalCardDeck().add(monsterList[l]);
                    break;
                    
                default:
                    System.err.println("[ERROR] Failed to add card to player hand!");
                    break;
            }
        }
        
        System.out.println("[INFO] Player data successfully created");
        
        System.out.println(player.getName() + "'s randomly assigned cards are:");
        for (int i = 0; i < 3; i++)
            System.out.println(player.getPersonalCardDeck().get(i).getMonsterName());
    }

    public Monster[] getMonsterList()
    {
        return monsterList;
    }

    public void setMonsterList(Monster[] monsterList)
    {
        this.monsterList = monsterList;
    }
    
}
