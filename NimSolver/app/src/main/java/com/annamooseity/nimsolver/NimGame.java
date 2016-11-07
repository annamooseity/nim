package com.annamooseity.nimsolver;

import android.net.Uri;

import com.annamooseity.nimsolver.NimRules;

/**
 * Created by Anna on 11/2/2016.
 */
public class NimGame
{

    public static String RULES_INDEX = "rules_index";
    public static String MOVE = "move";
    public static String PILES = "piles";
    public static String GAME_ID = "_id";
    public static final Uri CONTENT_URI_game = Uri.parse("content://"+ NimProvider.PROVIDER+"/games/1");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.annamooseity.game";
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
