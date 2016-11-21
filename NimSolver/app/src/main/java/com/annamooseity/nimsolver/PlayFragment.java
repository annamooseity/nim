package com.annamooseity.nimsolver;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import java.util.Arrays;


/**
 * PlayFragment.java
 * Anna Carrigan
 * Fragment to hold the gameplay view
 */
public class PlayFragment extends Fragment
{
    // Strings for the menu
    private static String saveStr = "Save Game";
    private static String helpStr = "Help!";
    private static String restartStr = "Restart";

    // Parameters for gameplay
    private NimGame game;
    private OnGamePlayListener mListener;
    private NimPileView nimPileView;
    private int numPiles = 5;

    public PlayFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true);
        View view =  inflater.inflate(R.layout.fragment_play, container, false);

        setUpPiles(view);



        return view;
    }

    /**
     * Lays out our nim piles neatly for us
     */

    private void setUpPiles(View view)
    {
        NimPileView pile1 = (NimPileView) view.findViewById(R.id.pile1);
        NimPileView pile2 = (NimPileView) view.findViewById(R.id.pile2);
        NimPileView pile3 = (NimPileView) view.findViewById(R.id.pile3);
        NimPileView pile4 = (NimPileView) view.findViewById(R.id.pile4);
        NimPileView pile5 = (NimPileView) view.findViewById(R.id.pile5);
        NimPileView pile6 = (NimPileView) view.findViewById(R.id.pile6);

        LinearLayout.LayoutParams all = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
        LinearLayout.LayoutParams half = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.5f);

        switch(numPiles)
        {
            case 1:
                pile2.setVisibility(View.GONE);
                pile3.setVisibility(View.GONE);
                pile4.setVisibility(View.GONE);
                pile5.setVisibility(View.GONE);
                pile6.setVisibility(View.GONE);

                pile1.setLayoutParams(all);
                break;
            case 2:
                pile3.setVisibility(View.GONE);
                pile4.setVisibility(View.GONE);
                pile5.setVisibility(View.GONE);
                pile6.setVisibility(View.GONE);

                pile1.setLayoutParams(half);
                pile2.setLayoutParams(half);
                break;
            case 3:
                pile4.setVisibility(View.GONE);
                pile5.setVisibility(View.GONE);
                pile6.setVisibility(View.GONE);
                break;
            case 4:
                pile5.setVisibility(View.GONE);
                pile6.setVisibility(View.GONE);

                pile5.setLayoutParams(all);
                break;
            case 5:
                pile6.setVisibility(View.GONE);
                pile4.setLayoutParams(half);
               pile5.setLayoutParams(half);

                break;
            default:
                break;
        }

    }

    /**
     * Creates custom options menu
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        MenuItem saveGame = menu.add(saveStr);
        MenuItem help = menu.add(helpStr);
        MenuItem restart = menu.add(restartStr);
    }

    /**
     * Handles behavior of our custom menu
     * @param item item selected from options
     * @return whether or not the item was in the main dropdown
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String title = item.getTitle().toString();

        if(title.equals(saveStr))
        {
            saveGame(game);
            return false;
        }
        else if(title.equals(helpStr))
        {
            MainActivity.displayHelpDialog(getContext());
            return false;
        }
        else if(title.equals(restartStr))
        {
            restart();
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Restarts the game
     */
    private void restart()
    {

    }

    public void saveGame(NimGame game)
    {
        // Check if already saved (should be saved at very beginning)

        // Update the current entry
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnGamePlayListener)
        {
            mListener = (OnGamePlayListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnGamePlayListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }


    public interface OnGamePlayListener
    {
        void onGameOver();
    }
}
