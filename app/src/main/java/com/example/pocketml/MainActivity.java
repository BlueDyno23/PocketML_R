package com.example.pocketml;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private SharedPreferences sharedPreferences;

    Button btnMainToEditor, btnMainToPredictor, btnMainToModels, btnMainAddDataset;
    ListView lvMain;


    private ActivityResultLauncher<String> filePickerLauncher;
    ArrayList<String> datasetPath = new ArrayList<>();
    ArrayList<String> datasetsName = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getPreferences(MODE_PRIVATE);

        InitViews();

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        String filePath = uri.getPath();
                        datasetPath.add(filePath);
                        String fileName = new File(datasetPath.get(datasetPath.size()-1)).getName();
                        datasetsName.add(filePath);
                        SaveDatasetPath(filePath);
                    }
                });

        LoadSavedDatasetPaths();
    }
    private void InitViews() {

        btnMainToEditor = findViewById(R.id.btnMainToEditor);
        btnMainToPredictor = findViewById(R.id.btnMainToPredictor);
        btnMainToModels = findViewById(R.id.btnMainToModels);
        btnMainAddDataset = findViewById(R.id.btnMainAddDataset);
        lvMain = findViewById(R.id.lvMain);

        btnMainToEditor.setOnClickListener(this);
        btnMainToPredictor.setOnClickListener(this);
        btnMainToModels.setOnClickListener(this);
        btnMainAddDataset.setOnClickListener(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datasetsName);
        lvMain.setAdapter(adapter);
    }

    private void AddDatasetsToListView(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datasetsName);
        lvMain.setAdapter(adapter);
    }
    private void AddDataset(){
        filePickerLauncher.launch("*/*");
    }
    private void SaveDatasetPath(String datasetPath) {
        Set<String> datasetPaths = sharedPreferences.getStringSet("dataset_paths", new HashSet<>());
        datasetPaths.add(datasetPath);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("dataset_paths", datasetPaths);
        editor.apply();
    }
    private void LoadSavedDatasetPaths() {
        Set<String> datasetPaths = sharedPreferences.getStringSet("dataset_paths", new HashSet<>());
        for (String path : datasetPaths) {
             datasetPath.add(path);
            String fileName = new File(path).getName();
            datasetsName.add(path);
            Log.d("Dataset", path);
        }

    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnMainToEditor){
            startActivity(new Intent(this, EditorActivity.class));
        }
        else if(view.getId()==R.id.btnMainAddDataset){
            AddDataset();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


}