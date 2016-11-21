package com.annamooseity.nimsolver;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.Arrays;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
                break;
            default:
                break;
        }
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
        final Switch dialogSwitch = (Switch) dialogView.findViewById(R.id.aiSwitch);
        final ViewSwitcher playAISwitcher = (ViewSwitcher) dialogView.findViewById(R.id.playerNameSwitcher);
        final EditText otherPlayer = (EditText) dialogView.findViewById(R.id.otherPlayerName_dialog);
        dialogSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                playAISwitcher.showNext();
            }
        });
        builder.setTitle("Start a New Game");
        builder.setView(dialogView);
        builder.setPositiveButton("Let's go!", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String otherPlayerName = "";
                if(!dialogSwitch.isChecked())
                {
                    otherPlayerName = otherPlayer.getText().toString();
                }
                playGame(new NimGame(rules, rules.getPiles(), 0, otherPlayerName, position + 1));
                        // Save new game
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    /**
     * Play the game
     *
     * @param game the game to play
     */
    public void playGame(NimGame game)
    {
        // play the game
    }

    /**
     * Determine behavior after rules are saved
     */
    @Override
    public void onRulesSaved()
    {
        // Go back to main screen if necessary
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
     * Save Game
     */
    @Override
    public void onSaveGame()
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

    }
}
