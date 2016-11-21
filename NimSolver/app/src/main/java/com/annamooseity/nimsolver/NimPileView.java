package com.annamooseity.nimsolver;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * NimPileView.java
 * Anna Carrigan
 *
 * Displays a gui version for our Nim Game
 */
public class NimPileView extends View
{

    public NimPileView(Context context)
    {
        super(context);
        init(null, 0);
    }

    public NimPileView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs, 0);
    }

    public NimPileView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle)
    {

    }

    private void invalidateTextPaintAndMeasurements()
    {

    }

    @Override
    protected void onDraw(Canvas canvas)
    {

    }

}
