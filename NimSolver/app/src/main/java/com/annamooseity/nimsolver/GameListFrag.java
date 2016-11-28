package com.annamooseity.nimsolver;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;


/**
 * GameListFrag.java
 * Anna Carrigan
 * ListView for the list of games previously saved
 */
public class GameListFrag extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>
{

    private OnGameListInteractionListener mListener;
    public GameCursorAdapter dataAdapter;

    public GameListFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_game_list, container, false);

        ListView mListView = (ListView) view.findViewById(android.R.id.list);

        // Setting up magic loaders
        getLoaderManager().initLoader(2, null, this);

        // Add our data adapter to the listView
        dataAdapter = new GameCursorAdapter(getContext(), null);
        mListView.setAdapter(dataAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView adapterView, View view, int i, long l)
            {

                final int index = i + 1;
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                final NimGame game = dataAdapter.getGameWithoutRules(i);
                dialog.setMessage("Delete your game with " + game.getOtherPlayerName() + "?");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        String[] args = {game.getOtherPlayerName(),
                                Arrays.toString(game.getPiles()),
                                Integer.toString(game.getMove()),
                                Integer.toString(game.getRulesIndex())};
                        getActivity().getContentResolver().delete(NimGame.CONTENT_URI_game,
                                NimGame.OPPONENT + "=? AND " +
                                NimGame.PILES + "=? AND " +
                                NimGame.MOVE + "=? AND " +
                                NimGame.RULES_INDEX + "=?", args);
                    }
                });


                dialog.setNegativeButton("Cancel", null);
                dialog.show();
                return true;
            }
        });

        return view;
    }


    /**
     * When a list item is clicked, start a new game with it
     * @param l the listview
     * @param v view
     * @param position the position of the click
     * @param id the id of the click
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        final int index = position;

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setMessage("Load your game with " + dataAdapter.getGameWithoutRules(position).getOtherPlayerName() + "?");
        NimGame game = (dataAdapter.getGameWithoutRules(index));

    String[] args = {Integer.toString(game.getRulesIndex())};

        Cursor rulesCursor = getActivity().getContentResolver().query(NimRules.CONTENT_URI_rules, NimRules.projection, NimRules.RULES_ID + " =?", args, null);
        rulesCursor.moveToFirst();
        NimRules rules = new NimRules(MainActivity.stringToIntArray(rulesCursor.getString(rulesCursor.getColumnIndex(NimRules.PILES))),
                MainActivity.stringToIntArray(rulesCursor.getString(rulesCursor.getColumnIndex(NimRules.TAKE_OPTIONS))),
                        Integer.parseInt(rulesCursor.getString(rulesCursor.getColumnIndex(NimRules.PLAYER_FIRST))));

        game.setRules(rules);

        rulesCursor.close();

        final NimGame finalGame = game;

        dialog.setPositiveButton("Load", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                mListener.onLoadGame(finalGame);

            }
        });

        dialog.setNegativeButton("Cancel", null);
        dialog.show();

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnGameListInteractionListener)
        {
            mListener = (OnGameListInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnGameListInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                NimGame.CONTENT_URI_game, NimGame.projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        dataAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        dataAdapter.swapCursor(null);
    }

    /**
     * Allows for interactions with Main Activity
     */
    public interface OnGameListInteractionListener
    {
        void onLoadGame(NimGame game);
    }
}
