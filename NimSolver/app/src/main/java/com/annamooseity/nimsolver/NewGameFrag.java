package com.annamooseity.nimsolver;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// Created by Anna Carrigan
// Oct 31, 2016

public class NewGameFrag extends Fragment
{

    private OnNewGameScreenInteractionListener mListener;

    public NewGameFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_game, container, false);
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
