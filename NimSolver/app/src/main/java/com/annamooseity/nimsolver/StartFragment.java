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


public class StartFragment extends Fragment
{

    private OnStartPageButtonClickedListener mListener;

    public StartFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        // Hook up listeners

        view.findViewById(R.id.bNewGame).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onButtonClicked(0);
            }
        });

        view.findViewById(R.id.bLoadGame).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onButtonClicked(1);
            }
        });

        view.findViewById(R.id.bHowTo).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onButtonClicked(2);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnStartPageButtonClickedListener)
        {
            mListener = (OnStartPageButtonClickedListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnStartPageButtonClickedListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    // Listener for buttons clicked to launch other fragments
    public interface OnStartPageButtonClickedListener
    {
        void onButtonClicked(int i);
    }
}
