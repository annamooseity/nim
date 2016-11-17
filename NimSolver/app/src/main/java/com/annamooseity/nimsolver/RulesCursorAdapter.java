package com.annamooseity.nimsolver;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Arrays;

/**
 * RulesCursorAdapter.java
 * Anna Carrigan
 * Adapter to make the list of rules pretty and nice
 */
public class RulesCursorAdapter extends CursorAdapter
{
    public RulesCursorAdapter(Context context, Cursor cursor)
    {
        super(context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_rules_row, parent, false);
    }

    // The important part
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView numPiles = (TextView) view.findViewById(R.id.piles_rulesList);
        TextView firstPlayer = (TextView) view.findViewById(R.id.firstPlayer_rulesList);
        TextView takeOptions = (TextView) view.findViewById(R.id.takeOptions_rulesList);

        NimRules rules = getRules(cursor.getPosition());
        firstPlayer.setText(String.valueOf(rules.getFirstPlayer()));
        takeOptions.setText(Arrays.toString(rules.getTakeOptions()));
        numPiles.setText(String.valueOf(rules.getPiles().length));
    }

    /**
     * Returns an actual rules object
     * @param position of the rules in the listview
     * @return rules object
     */
    public NimRules getRules(int position)
    {
        NimRules rules;
        String piles;
        String takeOpts;
        String firstPlayer;

        if(getCursor().moveToPosition(position)) {
            piles = getCursor().getString(getCursor().getColumnIndex(NimRules.PILES));
            takeOpts = getCursor().getString(getCursor().getColumnIndex(NimRules.TAKE_OPTIONS));
            firstPlayer = getCursor().getString(getCursor().getColumnIndex(NimRules.PLAYER_FIRST));

            rules = new NimRules(MainActivity.stringToIntArray(piles),
                    MainActivity.stringToIntArray(takeOpts),
                    Integer.parseInt(firstPlayer));

            return rules;
        }

        else
        {
            return null;
        }


    }
}
