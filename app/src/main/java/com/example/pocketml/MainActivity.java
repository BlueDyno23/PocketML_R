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

import static com.example.pocketml.Utilities.FileUtils.*;

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

import com.example.pocketml.Dataset.DatasetManager;
import com.example.pocketml.Dataset.DatasetModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    //region Views and Variables
    Button btnMainToEditor, btnMainToPredictor, btnMainToModels, btnMainAddDataset, btnDelete;
    ListView lvMain;

    ArrayList<DatasetModel> datasetArray = DatasetManager.getInstance().getDatasetList();
    ArrayAdapter<DatasetModel> adapterName;
    TextView txt;
    private ActivityResultLauncher<String> filePickerLauncher;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (getFileName(this, uri) != null && getFileName(this, uri).toLowerCase().endsWith(".csv")) {
                        addDataset(uri);
                    } else {
                        Toast.makeText(this, "Please select a CSV file", Toast.LENGTH_SHORT).show();
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
        lvMain.setOnItemClickListener(this);

        adapterName = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datasetArray);
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
            deleteLists();
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(this, Integer.toString(i) + " " + DatasetManager.getInstance().getDatasetList().get(i).toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("selectedIndex", Integer.toString(i));
        startActivity(intent);
    }

    // Reads all files in the folder and adds them to the ListView and the datasets ArrayList
    public void readFilesFromFolder(){
        List<String> files = getAllFilesInFolder(getApplicationContext());
        for(String file : files){
            addDatasetToLists(file);
            Toast.makeText(this, file, Toast.LENGTH_SHORT).show();
        }
        adapterName.notifyDataSetChanged();
    }

    // Adds a dataset using its path to the ListView and the datasets ArrayList
    public void addDatasetToLists(String path){

        datasetArray.add(new DatasetModel(path, getFileName(path)));

        txt.setText("datasetNameArray at 0: "+datasetArray.get(0).toString() +" count:");
    }

    // Adds a dataset using the Uri to the ListView and datasets ArrayList, used only once initially in the filepicker. Also saves the file to the device.
    public void addDataset(Uri uri){
        String s = saveFileFromUri(getApplicationContext(), uri);
        if(s!=null){
            datasetArray.add(new DatasetModel(s, getFileName(this, uri)));
        }
        adapterName.notifyDataSetChanged();
    }

    // Deletes all files in the folder, clears the ListView and the datasets ArrayList
    private void deleteLists(){
        for (DatasetModel dataset : datasetArray) {
            File file = new File(dataset.getDatasetPath());
            file.delete();
        }

        datasetArray.clear();
        adapterName.notifyDataSetChanged();
    }
}