package monster_battle_arena;

import java.util.ArrayList;

/**
 *
 * @author Coleman Rogers
 */
public class Player {
    
    private String name;
    private int gold = Integer.MAX_VALUE;
    private ArrayList <Monster> personalCardDeck = new ArrayList <Monster>();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getGold()
    {
        return gold;
    }

    public void setGold(int gold)
    {
        this.gold = gold;
    }

    public ArrayList<Monster> getPersonalCardDeck()
    {
        return personalCardDeck;
    }

    public void setPersonalCardDeck(ArrayList<Monster> personalCardDeck)
    {
        this.personalCardDeck = personalCardDeck;
    }
    
    
    
}
