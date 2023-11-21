// SUMMARIZE OF THIS BATCH
/*
 What needs to be done is:
    TODO: Clean the code

     VVVVVV MAIN PROBLEM VVVVVV
    TODO: Get real file path not just URI path

    TODO: Fix the adapter not updating correctly, and fix the debug-delete-button
    TODO: Create the elements or just scheme out a plan for the Dataset Editor, can use fragments
    TODO: Make the UI nicer and less debug-ish, can save this for the end
    TODO: Add the predictor, should be simple.

    TODO: Replan entire model; either find ways to insert parameters into models, try a different library, or do it manually
 */

package com.example.pocketml;

import static com.example.pocketml.FileUtils.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    Button btnMainToEditor, btnMainToPredictor, btnMainToModels, btnMainAddDataset, btnDelete;
    ListView lvMain;

    ArrayList<String> datasetPathArray = new ArrayList<>();
    ArrayList<String> datasetNameArray = new ArrayList<>();
    ArrayAdapter<String> adapterName;
    TextView txt;
    private ActivityResultLauncher<String> filePickerLauncher;

    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        addDataset(uri);
                    }
                });

        InitializeComponents();

        if(getAllFilesInFolder(getApplicationContext()).size() > 0){
            readFilesFromFolder();
        }
    }
    private void InitializeComponents() {

        btnMainToEditor = findViewById(R.id.btnMainToEditor);
        btnMainToPredictor = findViewById(R.id.btnMainToPredictor);
        btnMainToModels = findViewById(R.id.btnMainToModels);
        btnMainAddDataset = findViewById(R.id.btnMainAddDataset);
        btnDelete = findViewById(R.id.btnMainDeleteDebug);
        lvMain = findViewById(R.id.lvMain);
        txt = findViewById(R.id.textView);

        btnMainToEditor.setOnClickListener(this);
        btnMainToPredictor.setOnClickListener(this);
        btnMainToModels.setOnClickListener(this);
        btnMainAddDataset.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        adapterName = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datasetNameArray);
        lvMain.setAdapter(adapterName);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnMainToEditor){
            startActivity(new Intent(this, EditorActivity.class));
        }
        else if(view.getId()==R.id.btnMainAddDataset){
            filePickerLauncher.launch("*/*");
        }
        else if(view.getId()==R.id.btnMainDeleteDebug){

        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void readFilesFromFolder(){
        List<String> files = getAllFilesInFolder(getApplicationContext());
        for(String file : files){
            //addDatasetToLists(file);
            //Toast.makeText(this, file, Toast.LENGTH_SHORT).show();

            datasetNameArray.add(getFileName(getApplicationContext(), Uri.parse(file)));
            datasetPathArray.add(file);

            adapterName.notifyDataSetChanged();
        }

        adapterName.notifyDataSetChanged();
    }


    public void addDatasetToLists(String path){
        datasetNameArray.add(getFileName(getApplicationContext(), Uri.parse(path)));
        datasetPathArray.add(path);

        adapterName.notifyDataSetChanged();
    }
    public void addDataset(Uri uri){
        String s = saveFileFromUri(getApplicationContext(), uri);
        if(s!=null){
            datasetPathArray.add(s);
            datasetNameArray.add(getFileName(this, uri));
        }

        adapterName.notifyDataSetChanged();
    }
}