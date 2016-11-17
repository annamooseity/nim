package com.annamooseity.nimsolver;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * RulesListFrag.java
 * Anna Carrigan
 * ListView for the list of rules already saved
 */

public class RulesListFrag extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private OnRulesListInteractionListener mListener;
    private RulesCursorAdapter dataAdapter;

    public RulesListFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rules_list, container, false);

        view.findViewById(R.id.addRules).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onNewRules();
            }
        });

        ListView mListView = (ListView) view.findViewById(android.R.id.list);

        // Sets up magic loaders
        getLoaderManager().initLoader(1, null, this);

        // Adds the adapter to the listView
       dataAdapter = new RulesCursorAdapter(getContext(), null);
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

        mListener.onNewGameWithRules(position + 1, dataAdapter.getRules(position));
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnRulesListInteractionListener)
        {
            mListener = (OnRulesListInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnRulesListInteractionListener");
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
                NimRules.CONTENT_URI_rules, NimRules.projection, null, null, null);
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

    public interface OnRulesListInteractionListener
    {
        void onNewRules();

        void onNewGameWithRules(int settingsIndex, NimRules nimRules);
    }
}
