package com.annamooseity.nimsolver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
        implements StartFragment.OnStartPageButtonClickedListener,
        NewGameFrag.OnNewGameScreenInteractionListener
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
                break;
            default:
                break;
        }
    }

    @Override
    public void onNewSettings()
    {
        // Show new settings screen
    }

    @Override
    public void onNewGameWithSettings(int settingsIndex)
    {
        // Start a new game with settings from database
    }
}
