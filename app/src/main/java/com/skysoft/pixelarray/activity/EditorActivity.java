package com.skysoft.pixelarray.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.view.View;

import com.skysoft.pixelarray.editor.EditorView;
import com.skysoft.pixelarray.editor.enum.EditorMode;
import com.skysoft.pixelarray.R;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {
    private EditorView editorView;
    
    private ImageButton undo;
    private ImageButton redo;
    private Handler undoRedoUpdate;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
		
        editorView = findViewById(R.id.editor);
        
        ImageButton drawErase = findViewById(R.id.btn_drawErase);
        drawErase.setOnClickListener(this);
        
        undo = findViewById(R.id.btn_undo);
        undo.setOnClickListener(this);
        
        redo = findViewById(R.id.btn_redo);
        redo.setOnClickListener(this);
        
        undoRedoUpdate = new Handler();
        undoRedoUpdate.post(new Runnable(){ 
        
            @Override
            public void run() {
                undo.setEnabled(editorView.canUndo());
                redo.setEnabled(editorView.canRedo());
                undoRedoUpdate.postDelayed(this, 100);
            }
        });
    }

    @Override
    public void onClick(View v) {
        ImageButton btn = (ImageButton) v;
        
        switch (v.getId()) {
            case R.id.btn_drawErase: {
                editorView.setErase(!editorView.isErase());
                btn.setImageResource(editorView.isErase() ? R.drawable.ic_eraser : R.drawable.ic_brush);
                break;
            }
            
            case R.id.btn_undo: {
                editorView.undo();
                break;
            }
            
            case R.id.btn_redo: {
                editorView.redo();
            }
        }
    }
}
