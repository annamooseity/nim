package com.annamooseity.nimsolver;

import android.content.Context;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditNimRules.OnEditRulesListener} interface
 * to handle interaction events.
 */
public class EditNimRules extends Fragment
{

    private NimRules rules;
    private EditText p1, p2, p3, p4, p5, p6, otherPlayerName;
    private TextView l1, l2, l3, l4, l5, l6;
    private SeekBar seekBar;
    private TextView seekBarVal;
    private Switch playAI;
    private ViewSwitcher playAISwitcher;

    private EditText[] piles;
    private TextView[] pilesLabels;

    private RadioGroup firstPlayerRadios;


    private OnEditRulesListener mListener;

    public EditNimRules()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_nim_rules, container, false);

        p1 = (EditText) view.findViewById(R.id.pile1);
        p2 = (EditText) view.findViewById(R.id.pile2);
        p3 = (EditText) view.findViewById(R.id.pile3);
        p4 = (EditText) view.findViewById(R.id.pile4);
        p5 = (EditText) view.findViewById(R.id.pile5);
        p6 = (EditText) view.findViewById(R.id.pile6);

        l1 = (TextView) view.findViewById(R.id.lPile1);
        l2 = (TextView) view.findViewById(R.id.lPile2);
        l3 = (TextView) view.findViewById(R.id.lPile3);
        l4 = (TextView) view.findViewById(R.id.lPile4);
        l5 = (TextView) view.findViewById(R.id.lPile5);
        l6 = (TextView) view.findViewById(R.id.lPile6);

        piles = new EditText[]{p1, p2, p3, p4, p5, p6};
        pilesLabels = new TextView[]{l1, l2, l3, l4, l5, l6};

        seekBarVal = (TextView) view.findViewById(R.id.sliderText);

        otherPlayerName = (EditText) view.findViewById(R.id.otherPlayerName);
        seekBar = (SeekBar) view.findViewById(R.id.numPilePicker);
        playAI = (Switch) view.findViewById(R.id.aiSwitch);
        playAISwitcher = (ViewSwitcher) view.findViewById(R.id.playerNameSwitcher);
        playAI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                playAISwitcher.showNext();
            }
        });

        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                setNumPiles(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                // do nothing
            }
        });

        return view;
    }

    // TODO optimize
    private void setNumPiles(int i)
    {
        seekBarVal.setText("" + (i + 1));
        for(int j = 0; j <= i; j++)
        {
            piles[j].setVisibility(View.VISIBLE);
            pilesLabels[j].setVisibility(View.VISIBLE);
        }

        for(int j = i + 1; j < 6; j++)
        {
            piles[j].setVisibility(View.INVISIBLE);
            pilesLabels[j].setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnEditRulesListener)
        {
            mListener = (OnEditRulesListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnEditRulesListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnEditRulesListener
    {

        void onRulesSaved();

        void onCancel();
    }
}
