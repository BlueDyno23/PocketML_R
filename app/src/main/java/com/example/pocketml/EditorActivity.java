package com.example.pocketml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {

    LinearLayout columnsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        int n = Integer.parseInt(getIntent().getExtras().getString("selectedIndex"));
        Toast.makeText(this, DatasetManager.getInstance().getDatasetList().get(n).toString(), Toast.LENGTH_SHORT).show();
    }
}