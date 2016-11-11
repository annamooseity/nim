package com.annamooseity.nimsolver;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Anna on 11/11/2016.
 */
public class GameCursorAdapter extends CursorAdapter
{
    public GameCursorAdapter(Context context, Cursor cursor)
    {
        super(context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_game_row, parent, false);
    }

    // The important part
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView opponent = (TextView) view.findViewById(R.id.opponent_gameList);
        TextView numPiles = (TextView) view.findViewById(R.id.numPiles_gameList);
        TextView lastPlayedOn = (TextView) view.findViewById(R.id.lastPlayedOn_gameList);
        TextView moves = (TextView) view.findViewById(R.id.moves_gameList);

        NimGame game = getGame(cursor.getPosition());
        moves.setText(game.getMove());
        lastPlayedOn.setText(game.getLastPlayedOn());
        numPiles.setText(game.getRules().getPiles().length);
        opponent.setText(game.getOtherPlayerName());
    }

    public NimGame getGame(int position)
    {
        Object o = getItem(position);
        NimGame game;
        try
        {
            game = (NimGame) o;
        }
        catch (ClassCastException e)
        {
            // Could not cast
            Log.v("GameCursor", "Could not cast game.");
            return null;
        }

        return game;
    }


}
