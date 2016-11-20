package com.annamooseity.nimsolver;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


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

        mListener.onGameClicked(dataAdapter.getGameWithoutRules(position));
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
        void onGameClicked(NimGame game);
    }
}
