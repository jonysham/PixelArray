package com.skysoft.pixelarray.editor;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Paint;
import android.graphics.Color;

public class EditorView extends View {
    private Bitmap bitmap;
    private BitmapDrawable drawable;
    private Canvas myCanvas;
    private Paint paint;
    
    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        //создание пустой битмапки
        bitmap = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_8888);
        //создание дровабла из битмапа, ну думаю понятно итак
        drawable = new BitmapDrawable(getResources(), bitmap);
        drawable.setFilterBitmap(false);
        
        //собственный канвас должен иметь битмап
        //на котором собственно и рисует холст
        //и этот битмап в последствии будет сохраняться в файл
        myCanvas = new Canvas(bitmap);
        
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //заливка фона
		myCanvas.drawARGB(255, 255, 255, 255);
        
        //рисование тестовых точек на своем холсте
        paint.setColor(Color.RED);
        myCanvas.drawPoint(0, 0, paint);
        paint.setColor(Color.GREEN);
        myCanvas.drawPoint(7, 7, paint);
        paint.setColor(Color.BLUE);
        myCanvas.drawPoint(15, 15, paint);
        
        //рисование нашего drawable 
        //на основном канвасе по центру экрана
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;
        drawable.setBounds(centerX -getWidth()/2, centerY - getWidth()/2, centerX + getWidth()/2, centerY + getWidth()/2);
        drawable.draw(canvas);
    }
}
