package com.annamooseity.nimsolver;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.Arrays;
import java.util.List;

/**
 * MainActivity.java
 * Anna Carrigan
 *
 * Control for our NimGame
 */

public class MainActivity extends AppCompatActivity
        implements StartFragment.OnStartPageButtonClickedListener,
        RulesListFrag.OnRulesListInteractionListener,
        EditNimRules.OnEditRulesListener,
        PlayFragment.OnGamePlayListener,
        GameListFrag.OnGameListInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new StartFragment())
                .commit();

       getSupportActionBar().hide();
    }

    @Override
    public void onButtonClicked(int i)
    {
        switch (i)
        {
            // New Game
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new RulesListFrag())
                        .addToBackStack("newTrans").commit();
                break;
            // Load Game
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new GameListFrag())
                        .addToBackStack("newTrans").commit();
                break;

            // How-To
            case 2:
                displayHelpDialog(this);
                break;
            default:
                break;
        }
    }

    public static void displayHelpDialog(Context c)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        builder.setCancelable(false);
        builder.setTitle("How to play Nim");
        builder.setMessage("Nim, a mathematical game of strategy, is not a difficult " +
                "game to learn even if you are allergic to math. \n \n" +
                "Players take turns removing coins, chips, or beans from the piles. " +
                "The amount of chips you can remove on your turn depends on the rules " +
                "set in the beginning. If there are multiple piles, you can only take " +
                "chips from one pile. \n \n" +
                "The player to take the last chip is the winner!");
        builder.setPositiveButton("Got it!", null);
        builder.show();
    }

    /**
     * Displays screen to create new set of rules and allows back button
     */
    @Override
    public void onNewRules()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new EditNimRules())
                .addToBackStack("editNimRules").commit();
    }

    /**
     * Loads a new game with the specified rules
     *
     * @param position index of the rules in the database
     * @param rules actual rules object
     */
    @Override
    public void onNewGameWithRules(final int position, final NimRules rules)
    {

        // Start a new game with settings from database

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.new_game_dialog, null);
      //  final Switch dialogSwitch = (Switch) dialogView.findViewById(R.id.aiSwitch);
       // final ViewSwitcher playAISwitcher = (ViewSwitcher) dialogView.findViewById(R.id.playerNameSwitcher);
        final EditText otherPlayer = (EditText) dialogView.findViewById(R.id.otherPlayerName_dialog);


        builder.setTitle("Start a New Game");
        builder.setView(dialogView);
        builder.setPositiveButton("Let's go!", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String otherPlayerName = otherPlayer.getText().toString();
                // save a copy of the new game

                NimGame game = new NimGame(rules, rules.getPiles(), 0, otherPlayerName, position);
                String[] values = {Arrays.toString(game.getPiles()),
                        Integer.toString(game.getRulesIndex()),
                        game.getOtherPlayerName(),
                        Integer.toString(game.getMove())};
                ContentValues cv = createData(NimGame.no_id_projection, values);
                // Send data to database
               Uri uri =  getContentResolver().insert(NimGame.CONTENT_URI_game, cv);





                playGame(game);
                        // Save new game
            }
        });
        builder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        otherPlayer.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(otherPlayer.getText().length() < 1)
                {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
                else
                {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                // do nothing
            }
        });
    }

    /**
     * Play the game
     *
     * @param game the game to play
     */
    public void playGame(NimGame game)
    {



        PlayFragment frag = new PlayFragment();
        frag.setGame(game);

        // play the game
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, frag)
                .addToBackStack("newTrans").commit();


    }

    /**
     * Determine behavior after rules are saved
     */
    @Override
    public void onRulesSaved()
    {
        // Go back to main screen if necessary
        super.onBackPressed();
    }

    /**
     * Go back to the screen before
     */
    @Override
    public void onCancel()
    {
        super.onBackPressed();
    }

    /**
     * Game is over
     */
    @Override
    public void onGameOver()
    {

    }

    /**
     * Creates content values from a string array for a database
     * Stolen from my teacher, Jim Ward, at University of Wyoming
     * @param key the name of the database column
     * @param data the actual string data we want to go in the table
     * @return ContentValues ready to insert into the database
     */
    public static ContentValues createData(String[] key, String[] data)
    {
        ContentValues cv = new ContentValues();
        for (int i = 0; i < key.length; i++)
        {
            cv.put(key[i], data[i]);
        }
        return cv;
    }

    /**
     * Converts a string of numbers to an array of integers
     * @param string
     * @return an integer array
     */
    public static int[] stringToIntArray(String string)
    {
        String[] items = string.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

        int[] array = new int[items.length];

        for (int i = 0; i < items.length; i++)
        {
            try
            {
                array[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe)
            {
                Log.e("Error", "Oops! Your string did not format nicely into integers.");
            }

        }
        return array;
    }

    @Override
    public void onLoadGame(NimGame game)
    {
        // Start a new game with settings from database
        playGame(game);
    }

    public void goBack()
    {
        super.onBackPressed();
    }


    boolean processed = false;
    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        final AppCompatActivity activity = this;

        if(processed == true)
        {
            super.onBackPressed();
            processed = false;
        }
        boolean foundPlayFragment = false;

        if (fragmentList != null) {
            //TODO: Perform your logic to pass back press here
            for(final Fragment fragment : fragmentList){
                if(fragment instanceof PlayFragment && ((PlayFragment) fragment).changed){
                    foundPlayFragment = true;
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("Do you want to save before you leave?");
                    dialog.setPositiveButton("Save", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            ((PlayFragment) (fragment)).saveGame();
                            ((PlayFragment) (fragment)).changed = false;
                            processed = true;
                            activity.onBackPressed();
                            return;
                        }
                    });

                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            processed = true;
                            activity.onBackPressed();

                            return;
                        }
                    });

                    dialog.setNeutralButton("Cancel", null);

                    dialog.show();
                }
            }

            if(!foundPlayFragment)
            {
                super.onBackPressed();
            }

        }


    }
}
