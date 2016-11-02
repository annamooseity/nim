package com.annamooseity.nimsolver;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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

    private boolean isLoaded = false;

    private NimRules rules;
    private EditText p1, p2, p3, p4, p5, p6, otherPlayerName, takeOptions;
    private TextView l1, l2, l3, l4, l5, l6;
    private SeekBar seekBar;
    private TextView seekBarVal;
    private Switch playAI;
    private ViewSwitcher playAISwitcher;
    private int numPiles = 1;
    private EditText[] piles;
    private TextView[] pilesLabels;

    private RadioButton firstPlayerRadio;


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
        takeOptions = (EditText) view.findViewById(R.id.takeOptions);
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

        firstPlayerRadio = (RadioButton) view.findViewById(R.id.player1Radio);
        seekBar.setProgress(0);
        setNumPiles(0);
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

        view.findViewById(R.id.saveRules).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onRulesSaved(getRules());
            }
        });

        view.findViewById(R.id.cancelRules).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onCancel();
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
        numPiles = i + 1;
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

    public void setRules(NimRules rules)
    {

    }

    private NimRules getRules()
    {
        boolean valid = true;
        String opponent = "";
        boolean playingAI = playAI.isChecked();
        if(!playingAI)
        {
            opponent = otherPlayerName.getText().toString().trim();
            if(opponent == null || opponent.equals(""))
            {
                otherPlayerName.setError("Enter player name.");
            }
            else
            {
                otherPlayerName.setError(null);
            }
        }

        int[] takeOpts = getTakeOptions();
        if(takeOpts == null)
        {
            valid = false;
            takeOptions.setError("Enter numbers separated by commas.");
        }
        else
        {
            takeOptions.setError(null);
        }

        int[] pileAmts = getPileAmts();
        if(pileAmts == null)
        {
            valid = false;
        }

        if(valid)
        {
            return new NimRules(pileAmts, takeOpts, opponent, playingAI, firstPlayerRadio.isChecked());
        }
        else
        {
            return null;
        }
    }

    private int[] getPileAmts()
    {
        int[] pileAmts = new int[6];
        boolean valid = true;

        for(int i = 0; i < numPiles; i++)
        {
            String raw = piles[i].getText().toString();
            int amt = 0;
            if(!raw.equals(""))
            {
                amt = Integer.parseInt(piles[i].getText().toString());
                pileAmts[i] = amt;
            }

            if(amt == 0)
            {
                valid = false;
                piles[i].setError("Enter a number.");
            }
            else
            {
                piles[i].setError(null);
            }

        }
        for(int i = numPiles; i < 6; i++)
        {
            pileAmts[i] = 0;
        }

        if(valid)
        {
            return pileAmts;
        }
        else
        {
            return null;
        }
    }

    private int[] getTakeOptions()
    {
        String optsRaw = takeOptions.getText().toString().trim();
        String[] opts = optsRaw.split(",");

        int nonZeroCount = 0;

        // Input validation
        for(int i = 0; i < opts.length; i++)
        {
            if(opts[i] != "")
            {
                nonZeroCount++;
            }
        }
        // TODO CHECK FOR DUPES
        if(nonZeroCount > 0)
        {
            int[] optsInts = new int[nonZeroCount];
            for (int i = 0; i < opts.length; i++)
            {
                if (opts[i] != "")
                {
                    optsInts[i] = Integer.parseInt(opts[i]);
                }
            }


            return optsInts;
        }
        else
        {
            return null;
        }
    }


    public interface OnEditRulesListener
    {

        void onRulesSaved(NimRules rules);

        void onCancel();
    }
}
