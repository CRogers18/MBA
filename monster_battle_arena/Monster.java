package monster_battle_arena;

/**
 *
 * @author Coleman Rogers
 */
class Monster {
    
    private int monsterID;
    private int hitpoints, strength, defense, energyRequired, rarity;

    private boolean willTaunt;
    
    public Monster initMonster(int monsterID, String monsterName, int hitpoints, int strength, int defense, int energyRequired, int rarity)
    {
        this.monsterID = monsterID;
        this.monsterName = monsterName;
        this.hitpoints = hitpoints;
        this.strength = strength;
        this.defense = defense;
        this.energyRequired = energyRequired;
        this.rarity = rarity;
        return this;
    }
    
    private String monsterName;

    public int getEnergyRequired()
    {
        return energyRequired;
    }

    public void setEnergyRequired(int energyRequired)
    {
        this.energyRequired = energyRequired;
    }

    public String getMonsterName()
    {
        return monsterName;
    }

    public void setMonsterName(String monsterName)
    {
        this.monsterName = monsterName;
    }

    public int getMonsterID()
    {
        return monsterID;
    }

    public void setMonsterID(int monsterID)
    {
        this.monsterID = monsterID;
    }

    public int getHitpoints()
    {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints)
    {
        this.hitpoints = hitpoints;
    }

    public int getStrength()
    {
        return strength;
    }

    public void setStrength(int strength)
    {
        this.strength = strength;
    }

    public int getDefense()
    {
        return defense;
    }

    public void setDefense(int defense)
    {
        this.defense = defense;
    }
    
    public int getRarity()
    {
        return rarity;
    }

    public void setRarity(int rarity)
    {
        this.rarity = rarity;
    }
    
}
