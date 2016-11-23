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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Arrays;

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
    private NimPileView pile1, pile2, pile3, pile4, pile5, pile6, currentHighlightView;

    private View thisView;

    private Spinner takeChipsSpinner;
    private Button takeChipsButton;

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
        thisView = inflater.inflate(R.layout.fragment_play, container, false);






        takeChipsSpinner = (Spinner) thisView.findViewById(R.id.takeOptionsSpinner);
        takeChipsButton = (Button) thisView.findViewById(R.id.takeTheChipsButton);

        setUpPiles();
        return thisView;
    }

    public void setGame(NimGame game)
    {
        this.game = game;
        int[] piles = game.getPiles();
        int numPiles = 6;

        for (int i = 1; i < 6; i++)
        {
            if(piles[i - 1] == 0)
            {
                numPiles = i - 1;
                break;
            }
        }

        this.numPiles = numPiles;
    }

    // TODO optimized
    private void setHighlighted(NimPileView view)
    {
        if(currentHighlightView != null)
        {
            currentHighlightView.setPileHighlighted(false);
        }

        view.setPileHighlighted(true);

        currentHighlightView = view;


    }

    /**
     * Method for taking chips from pile
     */

    private void takeChips(int chipsToTake)
    {
        int newCount = currentHighlightView.getCount() - (int) takeChipsSpinner.getSelectedItem();
        currentHighlightView.setCount(newCount);
    }

    /**
     * Lays out our nim piles neatly for us
     */

    private void setUpPiles()
    {
        pile1 = (NimPileView) thisView.findViewById(R.id.pile1);
        pile2 = (NimPileView) thisView.findViewById(R.id.pile2);
        pile3 = (NimPileView) thisView.findViewById(R.id.pile3);
        pile4 = (NimPileView) thisView.findViewById(R.id.pile4);
        pile5 = (NimPileView) thisView.findViewById(R.id.pile5);
        pile6 = (NimPileView) thisView.findViewById(R.id.pile6);

        LinearLayout.LayoutParams all = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
        LinearLayout.LayoutParams half = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.5f);

        View.OnClickListener pileListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NimPileView nimView = (NimPileView) view;
                setHighlighted(nimView);
            }
        };

        switch (numPiles)
        {
            case 1:
                pile2.setVisibility(View.GONE);
                pile3.setVisibility(View.GONE);
                pile4.setVisibility(View.GONE);
                pile5.setVisibility(View.GONE);
                pile6.setVisibility(View.GONE);

                pile1.setOnClickListener(pileListener);

                pile1.setLayoutParams(all);
                break;
            case 2:
                pile3.setVisibility(View.GONE);
                pile4.setVisibility(View.GONE);
                pile5.setVisibility(View.GONE);
                pile6.setVisibility(View.GONE);

                pile1.setLayoutParams(half);
                pile2.setLayoutParams(half);

                pile1.setOnClickListener(pileListener);
                pile2.setOnClickListener(pileListener);

                break;
            case 3:
                pile4.setVisibility(View.GONE);
                pile5.setVisibility(View.GONE);
                pile6.setVisibility(View.GONE);

                pile1.setOnClickListener(pileListener);
                pile2.setOnClickListener(pileListener);
                pile3.setOnClickListener(pileListener);
                break;
            case 4:
                pile5.setVisibility(View.GONE);
                pile6.setVisibility(View.GONE);
                pile1.setOnClickListener(pileListener);
                pile2.setOnClickListener(pileListener);
                pile3.setOnClickListener(pileListener);
                pile4.setOnClickListener(pileListener);
                pile5.setLayoutParams(all);
                break;
            case 5:
                pile6.setVisibility(View.GONE);
                pile4.setLayoutParams(half);
                pile5.setLayoutParams(half);
                pile1.setOnClickListener(pileListener);
                pile2.setOnClickListener(pileListener);
                pile3.setOnClickListener(pileListener);
                pile4.setOnClickListener(pileListener);
                pile5.setOnClickListener(pileListener);


                break;
            default:
                pile1.setOnClickListener(pileListener);
                pile2.setOnClickListener(pileListener);
                pile3.setOnClickListener(pileListener);
                pile4.setOnClickListener(pileListener);
                pile5.setOnClickListener(pileListener);
                pile6.setOnClickListener(pileListener);
                break;
        }

        switch(numPiles)
        {
            case 6:
                pile6.setCount(game.getPiles()[5]);
            case 5:
                pile5.setCount(game.getPiles()[4]);
            case 4:
                pile4.setCount(game.getPiles()[3]);
            case 3:
                pile3.setCount(game.getPiles()[2]);
            case 2:
                pile2.setCount(game.getPiles()[1]);
            case 1:
                pile1.setCount(game.getPiles()[0]);
            default:
                break;
        }
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

            String[] selectionArgs = {Integer.toString(game.getMove()), game.getOtherPlayerName(), Arrays.toString(game.getPiles()), Integer.toString(game.getRulesIndex())};
            getActivity().getContentResolver().update(NimGame.CONTENT_URI_game, cv,
                    NimGame.MOVE + "=? AND " +
                            NimGame.OPPONENT + "=? AND " +
                            NimGame.PILES + "=? AND " +
                            NimGame.RULES_INDEX + "=?", selectionArgs);
            saveGame(game);
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
