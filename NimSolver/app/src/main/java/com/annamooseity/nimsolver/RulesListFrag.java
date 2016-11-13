package com.annamooseity.nimsolver;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

// Created by Anna Carrigan
// Oct 31, 2016

public class RulesListFrag extends ListFragment
{

    private OnNewGameScreenInteractionListener mListener;

    public RulesListFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_game, container, false);

        view.findViewById(R.id.addRules).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onNewSettings();
            }
        });

        ListView mListView = (ListView) view.findViewById(android.R.id.list);

        // Sets up magic loaders
        getLoaderManager().initLoader(1, null, this);

        // Adds the adapter to the listView
        RulesCursorAdapter dataAdapter = new RulesCursorAdapter(getContext(), null);
        mListView.setAdapter(dataAdapter);
  
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        mListener.onNewGameWithSettings(position + 1);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnNewGameScreenInteractionListener)
        {
            mListener = (OnNewGameScreenInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewGameScreenInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnNewGameScreenInteractionListener
    {
        void onNewSettings();

        void onNewGameWithSettings(int settingsIndex);
    }
}
