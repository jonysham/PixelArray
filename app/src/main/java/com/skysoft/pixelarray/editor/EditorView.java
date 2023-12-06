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
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;

import java.util.List;
import java.util.ArrayList;

import com.skysoft.pixelarray.editor.model.PathWrapper;
import com.skysoft.pixelarray.editor.enum.EditorMode;
import com.skysoft.pixelarray.R;

public class EditorView extends FrameLayout {
    static final String TAG = "EditorView";
    
    private int width = 32;
    private int height = 32;
    private int brushSize = 1;
    private int color = Color.BLACK;
    private float scale;
   
    private Paint paint;
    private Bitmap bitmap;
    private BitmapDrawable drawable;
    private AlphaDrawable alphaDrawable;
    
    private Canvas myCanvas;
    private CanvasView canvasView;
    
    private EditorMode editorMode;
    
    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        alphaDrawable = new AlphaDrawable();
        alphaDrawable.setColors(Color.rgb(160, 160, 160), Color.rgb(120, 120, 120));
        
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
                scale = (float) getWidth() / width;
                canvasView.setScaleX(scale);
                canvasView.setScaleY(scale);
                ((FrameLayout.LayoutParams) canvasView.getLayoutParams()).gravity = Gravity.CENTER;
                canvasView.requestLayout();
            }
        });
        
        setWillNotDraw(false);
        setClickable(true);
        setEditorMode(EditorMode.DRAW);
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
        private List<PathWrapper> paths;
        private PathWrapper nextPath;
        
        private CanvasView(Context context) {
            super(context);
            setClickable(true);
            paths = new ArrayList<>();
        }

        float startX;
        float startY;
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    startX = x;
                    startY = y;
                    nextPath = new PathWrapper(new Path(), brushSize, color, true);
                    nextPath.setIsErase(editorMode == EditorMode.ERASE);
                    nextPath.getPath().moveTo(x, y);
                    paths.add(nextPath);
                    break;
                }
                
                case MotionEvent.ACTION_MOVE: {
                    nextPath.getPath().lineTo(x, y);
                    break;
                }
                
                case MotionEvent.ACTION_UP: {
                    if (Math.abs(x - startX) <= 1 && Math.abs(y - startY) <= 1) {
                        nextPath.setIsStroke(false);
                        nextPath.getPath().addRect(x, y, x+1, y+1, Path.Direction.CW);
                    }
                }
            }
            
            invalidate();
            return super.onTouchEvent(event);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            alphaDrawable.setBounds(0, 0, w, h);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            
            alphaDrawable.draw(canvas);
            
            for (PathWrapper path : paths) {
                if (path.isErase()) {
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                } else {
                    paint.setXfermode(null);
                }
                
                paint.setColor(path.getColor());
                paint.setStyle(path.isStroke() ? Paint.Style.STROKE : Paint.Style.FILL);
                paint.setStrokeWidth(path.getWidth());
                myCanvas.drawPath(path.getPath(), paint);
            }
            
            drawable.setBounds(0, 0, getWidth(), getHeight());
            drawable.draw(canvas);
        }
    }
    
    public void setEditorMode(EditorMode editorMode) {
        this.editorMode = editorMode;
    }
    
    public EditorMode getEditorMode() {
        return editorMode;
    }
}
