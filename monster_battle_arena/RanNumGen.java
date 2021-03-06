package monster_battle_arena;

import java.util.concurrent.ThreadLocalRandom;

/*
 * @authors Howard Cheung and Coleman Rogers
 */
public class RanNumGen 
{    
    private int generator(int num1, int num2)
    {
        return ThreadLocalRandom.current().nextInt(num1, ++num2);
    }
    
    public int[] packOpening(Player player, boolean isUltra, Monster[] monsterList)
    {
        Monster[] cardsDropped = new Monster[3];
        int[] ids = new int[3];
        
        if (!isUltra)
        {
            // Standard card pack rolls
            int i = 0;
            
            while (i < 3)
            {
                int rarityRoll = generator(1, 100), cardRoll;
                
                if (rarityRoll <= 50)
                {
                    System.out.println("Rarity 1 rolled");
                    // Rarity 1 card drop
                    cardRoll = generator(0, 9);
                    ids[i] = cardRoll;
                    cardsDropped[i] = monsterList[cardRoll];
                    System.out.println("Card dropped: " + cardsDropped[i].getMonsterName());
                }
                
                if (rarityRoll > 50 && rarityRoll <= 80)
                {
                    System.out.println("Rarity 2 rolled");
                    // Rarity 2 card drop
                    cardRoll = generator(10, 24);
                    ids[i] = cardRoll;
                    cardsDropped[i] = monsterList[cardRoll];
                    System.out.println("Card dropped: " + cardsDropped[i].getMonsterName());
                }
                    
                if (rarityRoll > 80 && rarityRoll < 95)
                {
                    System.out.println("Rarity 3 rolled");
                    // Rarity 3 card drop
                    cardRoll = generator(25, 43);
                    ids[i] = cardRoll;
                    cardsDropped[i] = monsterList[cardRoll];
                    System.out.println("Card dropped: " + cardsDropped[i].getMonsterName());
                }
                    
                if (rarityRoll >= 95 && rarityRoll <= 100)
                {
                    System.out.println("Rarity 4 rolled");
                    // Rarity 4 card drop
                    cardRoll = generator(44, 56);
                    ids[i] = cardRoll;
                    cardsDropped[i] = monsterList[cardRoll];
                    System.out.println("Card dropped: " + cardsDropped[i].getMonsterName());
                }
                
                i++;
            }
            
        }
        
        if (isUltra)
        {
            // Ultra card pack rolls
            int j = 0;
            
            while (j < 3)
            {
                int rarityRoll = generator(1, 100), cardRoll;
                
                if (rarityRoll <= 70)
                {
                    System.out.println("Rarity 2 rolled");
                    // Rarity 2 card drop
                    cardRoll = generator(10, 24);
                    ids[j] = cardRoll;
                    cardsDropped[j] = monsterList[cardRoll];
                    System.out.println("Card dropped: " + cardsDropped[j].getMonsterName());
                }
                    
                if (rarityRoll > 70 && rarityRoll <= 88)
                {
                    System.out.println("Rarity 3 rolled");
                    // Rarity 3 card drop
                    cardRoll = generator(25, 43);
                    ids[j] = cardRoll;
                    cardsDropped[j] = monsterList[cardRoll];
                    System.out.println("Card dropped: " + cardsDropped[j].getMonsterName());
                }
                    
                if (rarityRoll > 88 && rarityRoll < 98)
                {
                    System.out.println("Rarity 4 rolled");
                    // Rarity 4 card drop
                    cardRoll = generator(44, 56);
                    ids[j] = cardRoll;
                    cardsDropped[j] = monsterList[cardRoll];
                    System.out.println("Card dropped: " + cardsDropped[j].getMonsterName());
                }
                    
                if (rarityRoll >= 98 && rarityRoll <= 100)
                {
                    System.out.println("Rarity 5 rolled");
                    // Rarity 5 card drop
                    cardRoll = generator(57, 63);
                    ids[j] = cardRoll;
                    cardsDropped[j] = monsterList[cardRoll];
                    System.out.println("Card dropped: " + cardsDropped[j].getMonsterName());
                }
                
                j++;
            }
        }
        
        // Add the cards dropped to the player's card deck
        for (int i = 0; i < cardsDropped.length; i++)
            player.getCardPool().add(cardsDropped[i]);
        
        System.out.println("");
     
        
        return ids;
    }
}