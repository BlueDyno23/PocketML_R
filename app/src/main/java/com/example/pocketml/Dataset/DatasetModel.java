package com.example.pocketml.Dataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public String[][] toStringArray(){
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(datasetPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                rows.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[][] result = new String[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            result[i] = rows.get(i);
        }

        return result;
    }
}
