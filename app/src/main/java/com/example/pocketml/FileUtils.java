package com.example.pocketml;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    // Specify the folder name where you want to save the files
    private static final String APP_FOLDER_NAME = "DatasetsFolder";

    public static List<String> getAllFilesInFolder(Context context) {
        List<String> filePaths = new ArrayList<>();

        // Specify the path of the folder
        String folderPath = context.getFilesDir() + File.separator + APP_FOLDER_NAME;

        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Add the absolute path of each file to the list
                    filePaths.add(file.getAbsolutePath());
                }
            }
        }

        return filePaths;
    }
    public static String saveFileFromUri(Context context, Uri uri) {
        String destinationPath = context.getFilesDir() + File.separator + APP_FOLDER_NAME;

        // Create the app folder if it doesn't exist
        File folder = new File(destinationPath);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                Log.e("FileUtil", "Failed to create directory: " + destinationPath);
                return null;
            }
        }

        // Get the file name from the URI
        String fileName = getFileName(context, uri);
        if (fileName == null) {
            Log.e("FileUtil", "Failed to retrieve file name from URI");
            return null;
        }

        // Generate a unique destination file path
        String destinationFilePath = destinationPath + File.separator + fileName;

        for (String s: getAllFilesInFolder(context)) {
            if(s.equals(destinationFilePath)){
                Log.e("FileUtil", "File already exists");
                return null;
            }
        }

        Log.e("FileUtil", "Its ok");
        // Copy the file to the app folder
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(destinationFilePath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            return destinationFilePath;
        } catch (IOException e) {
            Log.e("FileUtil", "Error copying file: " + e.getMessage());
            return null;
        }
    }

    public static String getFileName(Context context, Uri uri) {
        String fileName = null;
        String scheme = uri.getScheme();

        if (scheme != null && scheme.equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME);
                if (index != -1) {
                    fileName = cursor.getString(index);
                }
                cursor.close();
            }
        } else if (scheme != null && scheme.equals("file")) {
            fileName = uri.getLastPathSegment();
        }

        return fileName;
    }

    public static String getFileName(String filePath){
        return filePath.substring(filePath.lastIndexOf("/")+1);
    }
}