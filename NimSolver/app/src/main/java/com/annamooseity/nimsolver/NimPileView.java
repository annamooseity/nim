package com.annamooseity.nimsolver;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * NimPileView.java
 * Anna Carrigan
 * <p>
 * Displays a nim pile
 */
public class NimPileView extends View
{

    private int count = 24;
    boolean highlighted = false;

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

    public void setCount(int i)
    {
        count = i;
        invalidate();
    }

    public int getCount()
    {
        return count;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int[] center = {canvas.getWidth()/2, canvas.getHeight()/2};
        setBackgroundColor(Color.TRANSPARENT);
        Paint paint = new Paint();


        if(highlighted)
        {
            paint.setColor(Color.WHITE);
            canvas.drawCircle(center[0], center[1], canvas.getHeight()*0.30f, paint);
        }

        paint.setColor(Color.parseColor("#A364FF"));

        canvas.drawCircle(center[0], center[1], canvas.getHeight()*0.25f, paint);

        Rect textBounds = new Rect();
        String str = Integer.toString(count);
        paint.setTextSize(canvas.getHeight()*0.15f);
        paint.getTextBounds(str, 0, str.length(), textBounds);
        paint.setColor(Color.WHITE);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(str, center[0], center[1] + (textBounds.height()/2), paint);
    }

    public void setPileHighlighted(boolean highlight)
    {
        highlighted = highlight;
        invalidate();
    }

}