package com.annamooseity.nimsolver;

import android.util.Pair;

/**
 * Created by Anna on 11/16/2016.
 */

// TODO make this optimized by storing info on the game perhaps????
public class Solver
{
    int[] piles;
    NimGame game;
    int[][] sgArray;
    int[] takeOptions;
    int numPiles;

    public Solver(NimGame game)
    {
        piles = game.getPiles();
        this.game = game;

        sgArray = new int[piles.length][];

        for(int i = 0; i < sgArray.length; i++)
        {
            sgArray[i] = new int[piles[i] + 1];
        }

        takeOptions = game.getRules().getTakeOptions();

        numPiles = piles.length;
    }

    public void populateArray()
    {
        boolean found = false;

        // Go through each pile and assign sprague grundy numbers
        for(int i = 0; i < numPiles; i++)
        {
            sgArray[i][0] = 0;

            for (int j = 1; j < sgArray[i].length; j++)
            {
                for(int k = 0; k < takeOptions.length; k++)
                {
                    if (takeOptions[k] <= j && sgArray[i][j - takeOptions[k]] == 0)
                    {
                        sgArray[i][j] = 0;
                        found = true;
                        break;
                    }
                }

                if (!found)
                {
                    sgArray[i][j] = sgArray[i][j - 1] + 1;
                }
                else
                {
                    found = false;
                }
            }
        }


    }

    // returns pile, take option
    public Pair<Integer, Integer> nextMove()
    {
        // Go through each pile and look to see if there's a way to move to a 0 nim sum position...
        // If there is then this is a winning move

        for (int i = 0; i < sgArray.length; i++)
        {
            // Try different take optoins for each pile
            for (int j = 0; j < takeOptions.length; j++)
            {
                // Find the nim sum

                int nimSum = 0;

                for(int k = 0; k < sgArray.length; k++)
                {
                    if(k != i)
                    {
                        nimSum = nimSum ^ sgArray[i][sgArray[i].length - 1];
                    }
                    else
                    {
                        nimSum = nimSum ^ sgArray[i][sgArray[i].length - 1 - takeOptions[j]];
                    }
                }

                if(nimSum == 0)
                {
                    return new Pair(i, takeOptions[j]);
                }

            }

        }

        return new Pair(0, 0);
    }



    public boolean currentPlayerWins()
    {
        int nimSum = 0;

        for(int i = 0; i < sgArray.length; i++)
        {
            nimSum = nimSum ^ sgArray[i][sgArray[i].length - 1];
        }

        if(nimSum == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
