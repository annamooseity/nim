import com.annamooseity.nimsolver.NimRules;

/**
 * Created by Anna on 11/2/2016.
 */
public class NimGame
{
    private NimRules rules;

    private int[] piles;
    private int move;
    public boolean isOver = false;


    public NimGame(NimRules rules, int[] piles, int move)
    {
        this.rules = rules;
        this.piles = piles;
        this.move = move;
    }

    public void move(int take, int pileIndex)
    {
        piles[pileIndex] = piles[pileIndex] - take;
        checkIfOver();
    }

    // TODO optimize
    private void checkIfOver()
    {
        for(int i = 0; i < piles.length; i++)
        {
            if(piles[i] != 0)
            {
                return;
            }
        }
        // Game is over.
        isOver = true;
    }
}
