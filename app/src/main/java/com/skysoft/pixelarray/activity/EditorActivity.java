package com.skysoft.pixelarray.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.skysoft.pixelarray.R;
import com.skysoft.pixelarray.editor.EditorView;
import android.widget.ImageButton;
import android.view.View;
import com.skysoft.pixelarray.editor.enum.EditorMode;
import android.widget.Toast;
import android.app.Dialog;
import android.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.SeekBar;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {
    private final int DIALOG = 1;
    private EditorView editorView;
    private LinearLayout view;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
		
        editorView = findViewById(R.id.editor);
        
        ImageButton btnDrawErase = findViewById(R.id.btn_drawErase);
        ImageButton btnColorPicker = findViewById(R.id.btn_color_picker);
        ImageButton btnClear = findViewById(R.id.btn_clear_canvas);
        
        btnDrawErase.setOnClickListener(this);
        btnColorPicker.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ImageButton btnErase = (ImageButton) v;
        
        switch (v.getId()) {
            case R.id.btn_drawErase: {
                if (editorView.getEditorMode() == EditorMode.DRAW) {
                    editorView.setEditorMode(EditorMode.ERASE);
                    btnErase.setImageResource(R.drawable.ic_eraser);
                } else {
                    editorView.setEditorMode(EditorMode.DRAW);
                    btnErase.setImageResource(R.drawable.ic_brush);
                }
                break;
            }
            
            case R.id.btn_color_picker: {
                showDialog(DIALOG);
                break;
            }
            
            case R.id.btn_clear_canvas: {
                editorView.clearBitmap();
            }
        }
    }
    
    

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Color picker");
        view = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_color_picker, null);
        adb.setView(view);
        
        return adb.create();
    }
    
    @Override
    protected void onPrepareDialog(int dialogId, Dialog dialog) {
        super.onPrepareDialog(dialogId, dialog);

        switch (dialogId) {
            case DIALOG: {
                    final AlertDialog alertDialog = (AlertDialog) dialog;
                    Button button = alertDialog.findViewById(R.id.btn_color_picker_apply);
                    button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SeekBar sbAlfa = alertDialog.findViewById(R.id.seek_bar_alfa)
                                       ,sbRed = alertDialog.findViewById(R.id.seek_bar_red)
                                       ,sbGreen = alertDialog.findViewById(R.id.seek_bar_green)
                                       ,sbBlue = alertDialog.findViewById(R.id.seek_bar_blue);

                                int alfa = sbAlfa.getProgress()
                                   ,red = sbRed.getProgress()
                                   ,green = sbGreen.getProgress()
                                   ,blue = sbBlue.getProgress();

                                editorView.setColor(alfa, red, green, blue);
                                alertDialog.dismiss();
                            }
                        });

                    SeekBar sbAlfa = alertDialog.findViewById(R.id.seek_bar_alfa)
                           ,sbRed = alertDialog.findViewById(R.id.seek_bar_red)
                           ,sbGreen = alertDialog.findViewById(R.id.seek_bar_green)
                           ,sbBlue = alertDialog.findViewById(R.id.seek_bar_blue);
                    
                    
                    sbAlfa.setMax(255);
                    sbAlfa.setProgress(255);
                    sbRed.setMax(255);
                    sbGreen.setMax(255);
                    sbBlue.setMax(255);
                }
                break;
        }
    }
    
}
