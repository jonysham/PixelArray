package com.skysoft.pixelarray.editor.model;

import android.graphics.Path;

public class PathWrapper {
    private Path path;
    private int width;
    private int color;
    private boolean isStroke;
    private boolean isErase;
    
    public PathWrapper(Path path, int width, int color, boolean isStroke) {
        this.path = path;
        this.width = width;
        this.color = color;
        this.isStroke = isStroke;
    }

    public void setIsErase(boolean isErase) {
        this.isErase = isErase;
    }

    public boolean isErase() {
        return isErase;
    }

    public void setIsStroke(boolean isStroke) {
        this.isStroke = isStroke;
    }
    
    public Path getPath() {
        return path;
    }

    public int getWidth() {
        return width;
    }

    public int getColor() {
        return color;
    }

    public boolean isStroke() {
        return isStroke;
    }
}
