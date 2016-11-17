package com.annamooseity.nimsolver;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

/**
 * EditNimRules.java
 * Anna Carrigan
 * Editor page for Nim rules / game settings
 */
public class EditNimRules extends Fragment
{
    // Text views, etc
    private EditText takeOptions;
    private TextView seekBarVal;
    private RadioButton firstPlayerRadio;

    // Organizational arrays for textviews and edit texts
    private EditText[] piles;
    private TextView[] pilesLabels;

    // Actual object-related parameters
    private int numPiles = 1;
    private boolean isLoaded = false;
    private NimRules rules = null;

    // Listener for Main Activity interaction
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
        View view = inflater.inflate(R.layout.fragment_edit_nim_rules, container, false);

        // Set up the arrays for piles and their labels

        // Pile entry boxes
        EditText p1 = (EditText) view.findViewById(R.id.pile1);
        EditText p2 = (EditText) view.findViewById(R.id.pile2);
        EditText p3 = (EditText) view.findViewById(R.id.pile3);
        EditText p4 = (EditText) view.findViewById(R.id.pile4);
        EditText p5 = (EditText) view.findViewById(R.id.pile5);
        EditText p6 = (EditText) view.findViewById(R.id.pile6);

        // Label entry boxes
        TextView l1 = (TextView) view.findViewById(R.id.lPile1);
        TextView l2 = (TextView) view.findViewById(R.id.lPile2);
        TextView l3 = (TextView) view.findViewById(R.id.lPile3);
        TextView l4 = (TextView) view.findViewById(R.id.lPile4);
        TextView l5 = (TextView) view.findViewById(R.id.lPile5);
        TextView l6 = (TextView) view.findViewById(R.id.lPile6);

        piles = new EditText[]{p1, p2, p3, p4, p5, p6};
        pilesLabels = new TextView[]{l1, l2, l3, l4, l5, l6};

        // Hooking up the other views to accessible variables
        takeOptions = (EditText) view.findViewById(R.id.takeOptions);
        seekBarVal = (TextView) view.findViewById(R.id.sliderText);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.numPilePicker);
        firstPlayerRadio = (RadioButton) view.findViewById(R.id.player1Radio);

        // Set SeekBar and number of piles.
        // If loaded later, the listener will be triggered and take care of thing.
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

        // Set the save button listener
        view.findViewById(R.id.saveRules).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                // Save the rules

                // Get rules and set it up in database-friendly format
                rules = getRules();
                if(rules != null) {
                    String[] values = {Arrays.toString(rules.getPiles()), Arrays.toString(rules.getTakeOptions()), Integer.toString(rules.getFirstPlayer())};
                    ContentValues cv = MainActivity.createData(NimRules.no_id_projection, values);

                    // Send data to database
                    getActivity().getContentResolver().insert(NimRules.CONTENT_URI_rules, cv);
                }
                else
                {
                    Toast.makeText(getContext(), "Something went wrong. Please double check your input.", Toast.LENGTH_SHORT).show();
                }

                // Wrap it up in the Main Activity
                mListener.onRulesSaved();
            }
        });

        // Set the cancel button listener
        view.findViewById(R.id.cancelRules).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onCancel();
            }
        });

        // If this is an already existing game, we need to set up the GUI with those parameters
        if (isLoaded) {
            loadRules();
        }

        return view;
    }

    // TODO optimize

    /**
     * Makes the appropriate number of EditText piles visible
     *
     * @param numPiles:int Integer number of piles in game
     */
    private void setNumPiles(int numPiles)
    {
        seekBarVal.setText(Integer.toString(numPiles + 1));
        for (int j = 0; j <= numPiles; j++) {
            piles[j].setVisibility(View.VISIBLE);
            pilesLabels[j].setVisibility(View.VISIBLE);
        }

        for (int j = numPiles + 1; j < 6; j++) {
            piles[j].setVisibility(View.INVISIBLE);
            pilesLabels[j].setVisibility(View.INVISIBLE);
        }
        this.numPiles = numPiles + 1;
    }

    /**
     * Sets a rules object for the GUI
     * Usually used for loading
     *
     * @param rules:NimRules
     */
    public void setRules(NimRules rules)
    {
        this.rules = rules;
        isLoaded = true;
    }

    /**
     * Loads rule paramters into the GUI
     */
    private void loadRules()
    {
        setNumPiles(rules.getPiles().length);
        for (int i = 0; i < numPiles; i++) {
            piles[i].setText("" + rules.getPiles()[i]);
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < rules.getTakeOptions().length; i++) {
            s.append(rules.getTakeOptions()[i]);
            s.append(",");
        }
        s.deleteCharAt(s.length());
        takeOptions.setText(s.toString());

        if (rules.getFirstPlayer() == 1) {
            firstPlayerRadio.setChecked(true);
        }
        else {
            firstPlayerRadio.setChecked(false);
        }
    }


    // TODO encapsulate input validation

    /**
     * Gets a rules object from the actively edited GUI
     * Also does input validation
     */
    private NimRules getRules()
    {
        boolean valid = true;

        int[] takeOpts = getTakeOptions();
        if (takeOpts == null) {
            valid = false;
            takeOptions.setError("Enter numbers separated by commas.");
        }
        else {
            takeOptions.setError(null);
        }

        int[] pileAmts = getPileAmts();
        if (pileAmts == null) {
            valid = false;
        }

        int firstPlayer = 1;
        if (!firstPlayerRadio.isChecked()) {
            firstPlayer = 2;
        }

        if (valid) {
            return new NimRules(pileAmts, takeOpts, firstPlayer);
        }
        else {
            return null;
        }
    }

    /**
     * Pulls the contents of the pile text fields in a friendly format
     *
     * @return an array that lists the number of chips in each pile
     */
    private int[] getPileAmts()
    {
        int[] pileAmts = new int[6];
        boolean valid = true;

        for (int i = 0; i < numPiles; i++) {
            String raw = piles[i].getText().toString();
            int amt = 0;
            if (!raw.equals("")) {
                amt = parseInt(piles[i].getText().toString());
                pileAmts[i] = amt;
            }

            if (amt == 0) {
                valid = false;
                piles[i].setError("Enter a number.");
            }
            else {
                piles[i].setError(null);
            }

        }

        for (int i = numPiles; i < 6; i++) {
            pileAmts[i] = 0;
        }

        if (valid) {
            return pileAmts;
        }
        else {
            return null;
        }
    }

    /**
     * Pulls options for removal of chips in a friendly format
     *
     * @return an array that lists the options for taking chips
     */
    private int[] getTakeOptions()
    {
        String optsRaw = takeOptions.getText().toString().trim();
        String[] opts = optsRaw.split(",");

        int nonZeroCount = 0;

        // Input validation
        for (String opt : opts) {
            if (!opt.equals("")) {
                nonZeroCount++;
            }
        }
        // TODO CHECK FOR DUPES
        if (nonZeroCount > 0) {
            int[] optsInts = new int[nonZeroCount];
            for (int i = 0; i < opts.length; i++) {
                if (!opts[i].equals("")) {
                    optsInts[i] = parseInt(opts[i]);
                }
            }
            return optsInts;
        }
        else {
            return null;
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnEditRulesListener) {
            mListener = (OnEditRulesListener) context;
        }
        else {
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

    /**
     * Interface for interaction with Main Activity
     */
    public interface OnEditRulesListener
    {
        void onRulesSaved();

        void onCancel();
    }
}
