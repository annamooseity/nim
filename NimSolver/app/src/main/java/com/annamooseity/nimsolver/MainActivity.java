package com.annamooseity.nimsolver;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
        implements StartFragment.OnStartPageButtonClickedListener,
        NewGameFrag.OnNewGameScreenInteractionListener,
        EditNimRules.OnEditRulesListener
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
        switch(i)
        {
            // New Game
            case 0:
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new NewGameFrag())
                    .addToBackStack("newTrans").commit();
                break;
            // Load Game
            case 1:

                break;

            // How-To
            case 2:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setCancelable(false);
                builder.setTitle("How to play Nim");
                builder.setMessage("Here is how you play Nim. Blah blah blah blah.");
                builder.setPositiveButton("Got it!", null);
                builder.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNewSettings()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new EditNimRules())
                .addToBackStack("editNimRules").commit();

        // Show new settings screen
    }

    @Override
    public void onNewGameWithSettings(int settingsIndex)
    {
        // Start a new game with settings from database
    }

    @Override
    public void onRulesSaved()
    {

    }

    @Override
    public void onCancel()
    {

    }
}
