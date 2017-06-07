package monster_battle_arena;

import java.util.ArrayList;

/**
 *
 * @author Coleman Rogers
 */
public class Player {
    
    private String name;
    private int gemBalance = Integer.MAX_VALUE;
    private ArrayList <Monster> cardPool = new ArrayList <>();
    private ArrayList <Monster> customDeck1 = new ArrayList <> (), customDeck2 = new ArrayList <> (), customDeck3 = new ArrayList <> ();
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
    
}
