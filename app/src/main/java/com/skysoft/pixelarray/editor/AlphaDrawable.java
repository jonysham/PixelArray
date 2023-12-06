package com.skysoft.pixelarray.editor;

import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Color;

public class AlphaDrawable extends Drawable {
    private Paint paint;
    private int width;
    private int height;
    private int firstColor;
    private int secondColor;
    
    public AlphaDrawable() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        
        firstColor = Color.DKGRAY;
        secondColor = Color.LTGRAY;
    }

    public void setColors(int firstColor, int secondColor) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        invalidateSelf();
    }
    
    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        width = right;
        height = bottom;
    }

    @Override
    public void draw(Canvas canvas) {
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        paint.setColor(firstColor);
                    } else {
                        paint.setColor(secondColor);
                    }
                } else {
                    if (j % 2 == 0) {
                        paint.setColor(secondColor);
                    } else {
                        paint.setColor(firstColor);
                    }
                }

                canvas.drawRect(i, j, i+1, j+1, paint);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
        paint.setColorFilter(filter);
    }

    @Override
    public int getOpacity() {
        return 100;
    }
}
