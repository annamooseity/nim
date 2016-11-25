package com.annamooseity.nimsolver;

import android.util.Pair;

/**
 * Solver.java
 * Anna Carrigan
 * Solver to find who is going to win the nim game and how
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
            if(piles[i] == -1)
            {
                sgArray[i] = new int[1];
            }
            else
            {
                sgArray[i] = new int[piles[i] + 1];
            }
        }

        takeOptions = game.getRules().getTakeOptions();

        numPiles = piles.length;

        populateArray();
    }

    private void populateArray()
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
                        sgArray[i][j] = sgArray[i][j-1] + 1;
                        found = true;
                        break;
                    }
                }

                if (!found)
                {
                    sgArray[i][j] = 0;
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




        for(int i = 0; i < sgArray.length; i++)
        {
            if(!(piles[i] == 0) && !(piles[i] == -1))
            {
                for (int j = 0; j < takeOptions.length; j++)
                {
                    // Add up the nim Sum
                    int nimSum = 0;
                    for (int k = 0; k < sgArray.length; k++)
                    {
                        // If its the pile we are looking at taking from, we need to account for new nim Sum

                        if (i == k)
                        {
                            if(takeOptions[j] < sgArray[k].length)
                            {
                                int sg = (sgArray[k][sgArray[k].length - 1 - takeOptions[j]]);
                                nimSum = nimSum ^ sg;
                            }
                        }

                        else
                        {
                            int sg = (sgArray[k][sgArray[k].length - 1]);
                            nimSum = nimSum ^ sg;
                        }
                    }

                    if (nimSum == 0)
                    {
                        return new Pair(i, takeOptions[j]);
                    }
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
