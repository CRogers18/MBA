package monster_battle_arena;

import java.util.Random;

public class RanNumGen 
{
    Random generator = new Random();
    
    public int generator(int num1, int num2)
    {
        int range = (num2 - num1) + 1;
        int randomNumber = generator.nextInt(range) + num1;
        return randomNumber;
    }        
}
