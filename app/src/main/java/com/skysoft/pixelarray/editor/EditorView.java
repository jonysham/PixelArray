package com.skysoft.pixelarray.editor;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Paint;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.view.ViewGroup;
import android.view.Gravity;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.graphics.Path;
import java.util.List;
import java.util.ArrayList;

public class EditorView extends FrameLayout {
    private int width = 16;
    private int height = 16;
    private float scale;
    
    private Paint paint;
    private Bitmap bitmap;
    private BitmapDrawable drawable;
    private Canvas myCanvas;
    private CanvasView canvasView;
    
    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        setWillNotDraw(false);
        setClickable(true);
        
        paint = new Paint();
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        drawable = new BitmapDrawable(getResources(), bitmap);
        drawable.setFilterBitmap(false);
        myCanvas = new Canvas(bitmap);
        
        canvasView = new CanvasView(context);
        canvasView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        addView(canvasView);
        
        post(new Runnable() {
            @Override
            public void run() {
                scale = getWidth() / width;
                canvasView.setScaleX(scale);
                canvasView.setScaleY(scale);
                ((FrameLayout.LayoutParams) canvasView.getLayoutParams()).gravity = Gravity.CENTER;
                canvasView.requestLayout();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scrollGestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    
    private GestureDetector scrollGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            canvasView.setX(canvasView.getX() - distanceX);
            canvasView.setY(canvasView.getY() - distanceY);
            invalidate();
            return true;
        }
    });
        
    private ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            canvasView.setScaleX(scale);
            canvasView.setScaleY(scale);
            invalidate();
            return true;
        }
    });
    
    private class CanvasView extends View {
        private List<Path> paths;
        
        private CanvasView(Context context) {
            super(context);
            setClickable(true);
            paths = new ArrayList<>();
        }

        Path next;
        float lastX;
        float lastY;
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    next = new Path();
                    next.moveTo(x, y);
                    next.lineTo(x, y);
                    paths.add(next);
                    break;
                }
                
                case MotionEvent.ACTION_MOVE: {
                    next.lineTo(x, y);
                }
            }
            
            lastX = x;
            lastY = y;
            invalidate();
            return super.onTouchEvent(event);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            
            myCanvas.drawARGB(255, 255, 255, 255);
            paint.setARGB(255, 0, 0, 0);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1);
            
            for (Path p : paths) {
                myCanvas.drawPath(p, paint);
            }
            
            drawable.setBounds(0, 0, getWidth(), getHeight());
            drawable.draw(canvas);
        }
    }
}
