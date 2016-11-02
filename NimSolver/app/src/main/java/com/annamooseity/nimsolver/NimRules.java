package com.annamooseity.nimsolver;

/**
 * Created by Anna on 10/31/2016.
 */

// Container class for rules of nim
public class NimRules
{
    public int[] getPiles()
    {
        return piles;
    }

    public void setPiles(int[] piles)
    {
        this.piles = piles;
    }

    public int[] getTakeOptions()
    {
        return takeOptions;
    }

    public void setTakeOptions(int[] takeOptions)
    {
        this.takeOptions = takeOptions;
    }

    public String getOtherPlayer()
    {
        return otherPlayer;
    }

    public void setOtherPlayer(String otherPlayer)
    {
        this.otherPlayer = otherPlayer;
    }

    public boolean isVsAI()
    {
        return vsAI;
    }

    public void setVsAI(boolean vsAI)
    {
        this.vsAI = vsAI;
    }

    private int[] piles;
    private int[] takeOptions;
    private String otherPlayer;
    private boolean vsAI;

    public boolean isPlayer1First()
    {
        return player1First;
    }

    public void setPlayer1First(boolean firstPlayer)
    {
        this.player1First = firstPlayer;
    }

    private boolean player1First;

    public NimRules(int[] piles, int[] takeOptions, String otherPlayerName, boolean vsAI, boolean player1first)
    {
        this.piles = piles;
        this.takeOptions = takeOptions;
        this.otherPlayer = otherPlayerName;
        this.vsAI = vsAI;
        this.player1First = player1first;
    }


}
