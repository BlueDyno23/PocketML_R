package com.example.pocketml;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketml.Dataset.DatasetManager;
import com.example.pocketml.Utilities.HelperDb;

import java.util.ArrayList;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {

    TableLayout tableLayout;
    HelperDb helperDb;
    SQLiteDatabase db;
    int n;
    String[][] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        InitializeComponents();
        createRows();
    }

    private void InitializeComponents() {
        n = Integer.parseInt(getIntent().getExtras().getString("selectedIndex"));
        data = DatasetManager.getInstance().getDatasetList().get(n).toStringArray();
        HelperDb.COLUMNS = data[0];
        helperDb = new HelperDb(this, "null", null, 1);
        db = helperDb.getWritableDatabase();
        db.close();
        datasetToDatabase();

        tableLayout = findViewById(R.id.editorTable);
    }

    // TODO: Move to a custom array adapter version
    private void createRows(){
        tableLayout.removeAllViews();

        TableRow headerRow = new TableRow(this);
        for (int i = 0; i<data[0].length; i++){
            TextView textView = new TextView(this);
            textView.setText(data[0][i].replace("\"", " "));
            textView.setTextSize(18);
            textView.setTag(0+"_"+i);
            headerRow.addView(textView);
        }
        tableLayout.addView(headerRow);

        for (int i = 1; i < data.length; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < data[i].length; j++) {
                TextView textView = new TextView(this);
                textView.setText(data[i][j]);
                textView.setTag(i+"_"+j);
                textView.setOnClickListener(this);
                row.addView(textView);
            }
            tableLayout.addView(row);
        }
    }

    private void datasetToDatabase(){
        ContentValues cv = new ContentValues();
        String[][] data = DatasetManager.getInstance().getDatasetList().get(n).toStringArray();
        helperDb.updateTableStructure(data[0]);
        db = helperDb.getWritableDatabase();


        for (int i = 1; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                cv.put(data[0][j], data[i][j]);
            }
            db.insert(HelperDb.TABLE_NAME, null, cv);
        }
        db.close();
    }

    @Override
    public void onClick(View view) {
        if(view.getTag().toString().contains("_")){
            String[] tag = view.getTag().toString().split("_");
            int row = Integer.parseInt(tag[0]);
            int col = Integer.parseInt(tag[1]);

        }
    }
}