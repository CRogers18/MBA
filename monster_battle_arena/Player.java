package monster_battle_arena;

import java.util.ArrayList;

/**
 * @author Coleman Rogers
 **/
public class Player {
    
    private String name;
    private int gemBalance = 10000, gameHealth;
    private ArrayList <Monster> cardPool = new ArrayList <>();
    private ArrayList <Monster> customDeck1 = new ArrayList <> (), customDeck2 = new ArrayList <> (), customDeck3 = new ArrayList <> ();
    
    // May be used at a later date, for now it is un-used
    private String[] customDeckNames = new String[3];
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getGemBalance()
    {
        return gemBalance;
    }

    public void setGemBalance(int gemBalance)
    {
        this.gemBalance = gemBalance;
    }
    
    public int getGameHealth()
    {
        return gameHealth;
    }

    public void setGameHealth(int gameHealth)
    {
        this.gameHealth = gameHealth;
    }
    
    public ArrayList<Monster> getCardPool()
    {
        return cardPool;
    }

    public void setCardPool(ArrayList<Monster> cardPool)
    {
        this.cardPool = cardPool;
    }

    public ArrayList<Monster> getCustomDeck1()
    {
        return customDeck1;
    }

    public void setCustomDeck1(ArrayList<Monster> customDeck1)
    {
        this.customDeck1 = customDeck1;
    }

    public ArrayList<Monster> getCustomDeck2()
    {
        return customDeck2;
    }

    public void setCustomDeck2(ArrayList<Monster> customDeck2)
    {
        this.customDeck2 = customDeck2;
    }

    public ArrayList<Monster> getCustomDeck3()
    {
        return customDeck3;
    }

    public void setCustomDeck3(ArrayList<Monster> customDeck3)
    {
        this.customDeck3 = customDeck3;
    }
    
    public String[] getCustomDeckNames()
    {
        return customDeckNames;
    }

    public void setCustomDeckNames(String[] customDeckNames)
    {
        this.customDeckNames = customDeckNames;
    }

    public void sortCardPool(ArrayList <Monster> unsortedCardPool, Monster[] monsterList)
    {
        // WARNING: array is HARD-CODED for size of 40, will need to be replaced
        // with a VARIABLE array size when the total card pool goes beyond 40 cards
        int[] cardCounts = new int[40];
        ArrayList <Monster> orderedPool = new ArrayList <>();
        
        for (Monster card : unsortedCardPool)
            cardCounts[card.getMonsterID()] += 1;
        
        for (int i = 0; i < cardCounts.length; i++)
        {
            int count = cardCounts[i];
            
            for (int j = 0; j < count; j++)
                orderedPool.add(monsterList[i]);
        }
        
        this.cardPool = orderedPool;
        
        /* Un-comment if debugging this loop
        for (Monster card : cardPool)
            System.out.println("CardID: " + card.getMonsterID());
        */
    }
    
}