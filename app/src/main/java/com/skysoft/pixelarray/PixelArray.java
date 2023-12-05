package com.skysoft.pixelarray;

import android.app.Application;

public class PixelArray extends Application {
    private static PixelArray instance;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        CrashHandler.init(this);
        instance = this;
    }
    
    public static PixelArray getInstance() {
        return instance;
    }
}
