package com.annamooseity.nimsolver;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * GameCursorAdapter.java
 * Anna Carrigan
 *
 * Adapter for the list of games to make things pretty and nice
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
        TextView piles = (TextView) view.findViewById(R.id.numPiles_gameList);
        TextView moves = (TextView) view.findViewById(R.id.moves_gameList);

        NimGame game = getGameWithoutRules(cursor.getPosition());
        if(game != null) {
            if(game.getMove() == 0)
            {
                moves.setText("No moves yet");
            }
            else
            {
                moves.setText("Currently on move " + Integer.toString(game.getMove()));
            }
            //lastPlayedOn.setText(game.getLastPlayedOn());


            String str = "Piles of ";
            int[] pileArray = game.getPiles();
            boolean firstComma = true;
            for (int i = 0; i < 6; i++)
            {
                if(pileArray[i] != 0 && pileArray[i] != -1)
                {
                    if(firstComma)
                    {
                        str = str + Integer.toString(pileArray[i]);
                        firstComma = false;
                    }
                    else
                    {
                        str = str + ", " + Integer.toString(pileArray[i]);
                    }
                }
            }

            str = str + " chip(s)";

            if(str.equals("Piles of  chip(s)"))
            {
                str = "All piles are empty.";
            }
            piles.setText(str);

            opponent.setText("Game against " + game.getOtherPlayerName());
        }
    }

    /**
     * Gets an actual game from the listview
     * @param position The index position in the list of the objects
     * @return NimGame the game object for that list item -- SANS RULES!
     */
    public NimGame getGameWithoutRules(int position)
    {
        NimGame game;
        String move;
        String nimRulesIndex;
        String otherPlayerName;
        String piles;

        if(getCursor().moveToPosition(position))
        {
            move = getCursor().getString(getCursor().getColumnIndex(NimGame.MOVE));
            otherPlayerName = getCursor().getString(getCursor().getColumnIndex(NimGame.OPPONENT));
            nimRulesIndex = getCursor().getString(getCursor().getColumnIndex(NimGame.RULES_INDEX));
            piles = getCursor().getString(getCursor().getColumnIndex(NimGame.PILES));

            game = new NimGame(new NimRules(null, null, 1),
                    MainActivity.stringToIntArray(piles), Integer.parseInt(move), otherPlayerName, Integer.parseInt(nimRulesIndex));

            return game;
        }
        else
        {
            return null;
        }

    }


}
