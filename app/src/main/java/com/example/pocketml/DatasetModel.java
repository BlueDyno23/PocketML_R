package com.example.pocketml;

public class DatasetModel {
    private String datasetPath;
    private String datasetName;

    public DatasetModel(String datasetPath, String datasetName){
        this.datasetPath = datasetPath;
        this.datasetName = datasetName;
    }

    public String getDatasetPath(){
        return datasetPath;
    }

    public String getDatasetName(){
        return datasetName;
    }

    @Override
    public String toString(){
        return datasetName;
    }
}
