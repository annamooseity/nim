package com.annamooseity.nimsolver;

import android.net.Uri;


/**
 * NimRules.java
 * Anna Carrigan
 * Object class for rules of nim
 */

public class NimRules
{
    // Statics for our content provider to use later
    public static String TAKE_OPTIONS = "takeOpts";
    public static String PILES = "piles";
    public static String PLAYER_FIRST = "firstPlayer";
    public static String RULES_ID = "_id";
    public static final Uri CONTENT_URI_rules= Uri.parse("content://"+ NimProvider.PROVIDER+"/rules/1");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.annamooseity.rules";
    public static String[] projection = new String[]{RULES_ID, PILES, TAKE_OPTIONS, PLAYER_FIRST};
    public static String[] no_id_projection = new String[]{PILES, TAKE_OPTIONS, PLAYER_FIRST};

    // Actual rule paramters
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

    public NimRules(int[] piles, int[] takeOptions, int firstPlayer)
    {
        this.piles = piles;
        this.takeOptions = takeOptions;
        this.firstPlayer = firstPlayer;
    }


}
