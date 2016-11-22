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
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    /**
     * Creates custom options menu
     *
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
     *
     * @param item item selected from options
     * @return whether or not the item was in the main dropdown
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        String title = item.getTitle().toString();

        if (title.equals(saveStr))
        {
            String[] values = {Arrays.toString(game.getPiles()),
                    Integer.toString(game.getRulesIndex()),
                    game.getOtherPlayerName(),
                    Integer.toString(game.getMove())};
            ContentValues cv = MainActivity.createData(NimGame.no_id_projection, values);

            // TODO check if this is right
            // Goes and looks for the game and updates it

            String[] selectionArgs = {Integer.toString(game.getMove()), game.getOtherPlayerName(),  Arrays.toString(game.getPiles()), Integer.toString(game.getRulesIndex())};
            getActivity().getContentResolver().update(NimGame.CONTENT_URI_game, cv,
                    NimGame.MOVE + "=? AND " +
                            NimGame.OPPONENT + "=? AND" +
                            NimGame.PILES + "=? AND" +
                            NimGame.RULES_INDEX + "=?", selectionArgs);

            return false;
        }
        else if (title.equals(helpStr))
        {
            MainActivity.displayHelpDialog(getContext());
            return false;
        }
        else if (title.equals(restartStr))
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

        void onSaveGame();
    }
}
