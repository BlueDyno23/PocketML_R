package com.example.pocketml;

import java.util.ArrayList;

public class DatasetManager {
    private static DatasetManager _instance;
    private ArrayList<DatasetModel> datasets = new ArrayList<>();

    public static DatasetManager getInstance(){
        if(_instance == null){
            _instance = new DatasetManager();
        }
        return _instance;
    }

    public ArrayList<DatasetModel> getDatasetList(){
        return datasets;
    }

    public void addDataset(DatasetModel dataset){
        datasets.add(dataset);
    }
}
