package com.esp.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class EditActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editText;
    private TextView toolbarTitle;
    private ImageButton editTitleButton;
    private ImageButton textColorPicker;
    private ImageButton textSizeButton;

    private int backgroundResId;
    private int position;
    private int id;

    Button cancelSave;
    Button confirmSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        hideStatusBar();

        init();
    }

    private void hideStatusBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                final AlertDialog dialog;
                View view = LayoutInflater.from(EditActivity.this).inflate(R.layout.dialog_save, null);
                builder.setView(view);
                cancelSave = (Button) view.findViewById(R.id.save_cancel_button);
                confirmSave = (Button) view.findViewById(R.id.save_confirm_button);
                dialog = builder.create();
                cancelSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        finish();
                        overridePendingTransition(R.anim.anim_intent, R.anim.anim_intent_out);

                    }
                });
                confirmSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", toolbarTitle.getText().toString());
                        String desc = editText.getText().toString();
                        bundle.putString("desc", desc);
                        bundle.putInt("bg",backgroundResId);
                        bundle.putInt("pos", position);
                        bundle.putInt("id", id);
                        intent.putExtra("data", bundle);
                        setResult(MainActivity.REQUEST_CODE, intent);
                        dialog.cancel();
                        finish();
                        overridePendingTransition(R.anim.anim_intent, R.anim.anim_intent_out);

                    }
                });
                dialog.show();

            }
        });
        textSizeButton = (ImageButton) findViewById(R.id.text_width_button);
        textColorPicker = (ImageButton) findViewById(R.id.text_color_picker);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        editText = (EditText) findViewById(R.id.edit_text);
        editTitleButton = (ImageButton) findViewById(R.id.edit_title);
        Bundle bundle = getIntent().getBundleExtra("item");
        final String title = bundle.getString("title","Chưa có tên");
        toolbarTitle.setText(title);
        String str = bundle.getString("desc","");
        backgroundResId = bundle.getInt("bg");
        position = bundle.getInt("pos");
        id = bundle.getInt("id");
        editText.setText(str);
        editText.setSelection(editText.getText().length());
        editText.setBackgroundResource(backgroundResId);

        editTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                final AlertDialog dialog;
                View view = LayoutInflater.from(EditActivity.this).inflate(R.layout.dialog_edit_title, null);
                builder.setView(view);
                final EditText editTextTitle = (EditText) view.findViewById(R.id.edit_text_title);
                editTextTitle.setText(toolbarTitle.getText().toString());
                editTextTitle.setSelection(editTextTitle.getText().toString().length());
                Button cancel = (Button) view.findViewById(R.id.edit_title_cancel);
                Button confirm = (Button) view.findViewById(R.id.edit_title_confirm);
                final TextView error = (TextView) view.findViewById(R.id.error_text_view);
                dialog = builder.create();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = editTextTitle.getText().toString();
                        if (title.equals("")) {
                            error.setVisibility(View.VISIBLE);
                        } else {
                            toolbarTitle.setText(title);
                            error.setVisibility(View.GONE);
                            dialog.cancel();
                        }
                    }
                });
                dialog.show();
            }
        });

        textColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(EditActivity.this)
                        .setTitle("Choose color")
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                editText.setTextColor(selectedColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        textSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                final AlertDialog dialog;
                View view = LayoutInflater.from(EditActivity.this).inflate(R.layout.dialog_text_size, null);
                builder.setView(view);
                final SeekBar seekBar = (SeekBar) view.findViewById(R.id.seek_bar_text_size);
                final TextView textViewDemo = (TextView) view.findViewById(R.id.text_demo);
                int defaltSize = (int)(editText.getTextSize() / getResources().getDisplayMetrics().scaledDensity);
                seekBar.setProgress(defaltSize-10);
                textViewDemo.setTextSize(seekBar.getProgress() + 10);
                textViewDemo.setText(seekBar.getProgress() + 10 + "");
                Button cancel = (Button) view.findViewById(R.id.text_size_cancel_button);
                Button confirm = (Button) view.findViewById(R.id.text_size_confirm_button);
                dialog = builder.create();

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        textViewDemo.setTextSize(seekBar.getProgress() + 10);
                        textViewDemo.setText(seekBar.getProgress() + 10 + "");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setTextSize(seekBar.getProgress() + 10);
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

    }


}
