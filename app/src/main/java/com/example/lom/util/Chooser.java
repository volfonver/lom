package com.example.lom.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.widget.TextView;

public class Chooser {
    public static final String dirToSaveInDownloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    private static String dirToSave = dirToSaveInDownloads;

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static void choosePath(Activity activity) {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        activity.startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
//        }
    }

    public static void setDownloadPath(TextView view) {
        String path = "Каталог загрузки: \n" + Chooser.getDirToSave();
        view.setText(path);
    }

    public static void setDirToSave(String path) {
        dirToSave = path;
    }

    public static String getDirToSave() {
        return dirToSave;
    }
}
