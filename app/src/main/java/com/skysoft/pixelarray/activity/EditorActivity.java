package com.skysoft.pixelarray.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.skysoft.pixelarray.R;
import com.skysoft.pixelarray.editor.EditorView;
import android.widget.ImageButton;
import android.view.View;
import com.skysoft.pixelarray.editor.enum.EditorMode;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {
    private EditorView editorView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
		
        editorView = findViewById(R.id.editor);
        
        ImageButton btnDrawErase = findViewById(R.id.btn_drawErase);
        btnDrawErase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ImageButton btn = (ImageButton) v;
        
        switch (v.getId()) {
            case R.id.btn_drawErase: {
                if (editorView.getEditorMode() == EditorMode.DRAW) {
                    editorView.setEditorMode(EditorMode.ERASE);
                    btn.setImageResource(R.drawable.ic_eraser);
                } else {
                    editorView.setEditorMode(EditorMode.DRAW);
                    btn.setImageResource(R.drawable.ic_brush);
                }
            }
        }
    }
}
