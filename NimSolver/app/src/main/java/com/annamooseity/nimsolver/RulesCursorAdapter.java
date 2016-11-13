package com.annamooseity.nimsolver;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Anna on 11/11/2016.
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
        firstPlayer.setText(rules.getFirstPlayer());
        takeOptions.setText(rules.getTakeOptions().toString());
        numPiles.setText(rules.getPiles().length);
    }

    public NimRules getRules(int position)
    {
        Object o = getItem(position);
        NimRules rules;
        try
        {
            rules = (NimRules) o;
        }
        catch (ClassCastException e)
        {
            // Could not cast
            Log.v("GameRules", "Could not cast rules.");
            return null;
        }

        return rules;
    }
}
