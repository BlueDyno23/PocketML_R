package com.example.pocketml;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketml.Dataset.DatasetManager;
import com.example.pocketml.Utilities.HelperDb;

import java.util.ArrayList;

public class EditorActivity extends AppCompatActivity {

    TableLayout tableLayout;
    ArrayList<TableRow> tableRows;

    HelperDb helperDb;
    SQLiteDatabase db;
    int n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        InitializeComponents();
        createRows();
    }

    private void InitializeComponents() {
        n = Integer.parseInt(getIntent().getExtras().getString("selectedIndex"));
        String[][] data = DatasetManager.getInstance().getDatasetList().get(n).toStringArray();
        HelperDb.COLUMNS = data[0];
        helperDb = new HelperDb(this, "null", null, 1);
        db = helperDb.getWritableDatabase();
        db.close();
        datasetToDatabase();

        tableLayout = findViewById(R.id.editorTable);
    }

    private void createRows(){
        String[][] data = DatasetManager.getInstance().getDatasetList().get(n).toStringArray();

        for (int i = 0; i<data[0].length; i++){
            TextView textView = new TextView(this);
            textView.setText(data[0][i]);
            textView.setTextSize(20);
            TableRow row = new TableRow(this);
            row.addView(textView);
            tableLayout.addView(row);
        }

        for (int i = 1; i < data.length; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < data[i].length; j++) {
                TextView textView = new TextView(this);
                textView.setText(data[i][j]);
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

}