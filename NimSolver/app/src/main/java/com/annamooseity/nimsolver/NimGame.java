package com.annamooseity.nimsolver;

import android.net.Uri;

import com.annamooseity.nimsolver.NimRules;

/**
 * NimGame.java
 * Anna Carrigan
 * Object class for a game of Nim
 */
public class NimGame
{
    // Statics for our content provider to use later
    public static String RULES_INDEX = "rules_index";
    public static String MOVE = "move";
    public static String PILES = "piles";
    public static String GAME_ID = "_id";
    public static String OPPONENT = "opponent";
    public static final Uri CONTENT_URI_game = Uri.parse("content://"+ NimProvider.PROVIDER+"/games/1");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.annamooseity.game";
    public static String[] projection = new String[]{GAME_ID, PILES, RULES_INDEX, OPPONENT, MOVE};
    public static String[] no_id_projection = new String[] {PILES, RULES_INDEX, OPPONENT, MOVE};

    // Actual game parameters
    private int rulesIndex;
    private int lastPlayedOn;
    private NimRules rules;
    private int[] piles;
    private int move;
    private boolean isOver = false;
    private String otherPlayerName = "";

    public NimRules getRules()
    {
        return rules;
    }

    public int[] getPiles()
    {
        return piles;
    }

    public int getLastPlayedOn()
    {
        return lastPlayedOn;
    }

    public int getMove()
    {
        return move;
    }

    public String getOtherPlayerName()
    {
        return otherPlayerName;
    }

    public NimGame(NimRules rules, int[] piles, int move, String otherPlayerName, int rulesIndex)
    {
        this.rules = rules;
        this.piles = piles;
        this.move = move;
        this.otherPlayerName = otherPlayerName;
        this.rulesIndex = rulesIndex;
        lastPlayedOn = 0;
    }

    /**
     * Makes a move in the game
     * @param take number of chips to take
     * @param pileIndex which pile to take the chips from
     */
    public void move(int take, int pileIndex)
    {
        piles[pileIndex] = piles[pileIndex] - take;
        checkIfOver();
    }

    // TODO optimize

    /**
     * Checks if the game is over
     */
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
