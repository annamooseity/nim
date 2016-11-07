package com.annamooseity.nimsolver;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayFragment.OnGamePlayListener} interface
 * to handle interaction events.
 */
public class PlayFragment extends Fragment
{
    private NimGame game;
    private OnGamePlayListener mListener;
    private GameView gameView;

    private static String saveStr = "Save Game";
    private static String helpStr = "Help!";
    private static String restartStr = "Restart";

    public PlayFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        MenuItem saveGame = menu.add(saveStr);
        MenuItem help = menu.add(helpStr);
        MenuItem restart = menu.add(restartStr);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String title = item.getTitle().toString();

        if(title.equals(saveStr))
        {
            mListener.onSaveGame(game);
            return false;
        }
        else if(title.equals(helpStr))
        {
            // Call the help dialog!
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
        void onGameOver(NimGame game);
        void onSaveGame(NimGame game);
    }
}
