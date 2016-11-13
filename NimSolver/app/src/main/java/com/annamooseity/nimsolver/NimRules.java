package com.annamooseity.nimsolver;

import android.net.Uri;

/**
 * Created by Anna on 10/31/2016.
 */

// Container class for rules of nim
public class NimRules
{
    public static String TAKE_OPTIONS = "takeOpts";
    public static String PILES = "piles";
    public static String PLAYER_FIRST = "firstPlayer";
    public static String RULES_ID = "_id";
    public static final Uri CONTENT_URI_rules= Uri.parse("content://"+ NimProvider.PROVIDER+"/rules/1");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.annamooseity.rules";
    private int[] piles;
    private int[] takeOptions;

    public int getFirstPlayer()
    {
        return firstPlayer;
    }

    public void setFirstPlayer(int firstPlayer)
    {
        this.firstPlayer = firstPlayer;
    }

    private int firstPlayer;

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

    public NimRules(int[] piles, int[] takeOptions, String otherPlayerName, int firstPlayer)
    {
        this.piles = piles;
        this.takeOptions = takeOptions;
        this.firstPlayer = firstPlayer;
    }


}
