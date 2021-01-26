package com.example.lom.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.lom.model.vo.Track;
import com.example.lom.util.Chooser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Loader {

    public static void download(final Context context, Track... tracks) {
        /*if (!dirToSave.contains("/storage/")) { // если флеш карта
            if (isExternalStorageWritable()) // если доступна .. ??
                new DownloadAudioAsyncTask(urls, fileNames).execute();
        } else*/
        Toast.makeText(context, "Загрузка...", Toast.LENGTH_SHORT).show();
        new DownloadAudioAsyncTask() {
            @Override
            public void printFinish() {
                Toast.makeText(context, "Готово", Toast.LENGTH_LONG).show();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tracks);
    }

    interface ProgressListener {
        void printFinish();
    }

    private abstract static class DownloadAudioAsyncTask extends AsyncTask<Track, Integer, String>
            implements ProgressListener {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Track... tracks) {
            for (Track track : tracks) {
                saveFile(track.getUrlToLoading(), track.getSinger() + " - " + track.getTitle());
            }
            return "OK";
        }

        private boolean saveFile(String sUrl, String fileName) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                System.out.println("2 --");
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("Server returned HTTP", connection.getResponseCode()
                            + " " + connection.getResponseMessage());
                    return false;
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                try (InputStream input = connection.getInputStream();
                     OutputStream output = new FileOutputStream(Chooser.getDirToSave() + "/" + fileName + ".mp3")) {
//                String s = "/storage/emulated/0/Download/file.mp3";
                    System.out.println("path: " + Chooser.getDirToSave());
                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        // allow canceling with back button
                        if (isCancelled()) {
                            input.close();
                            return false;
                        }
                        total += count;
                        // publishing the progress....
                        if (fileLength > 0) // only if total length is known
                            publishProgress((int) (total * 100 / fileLength));
                        output.write(data, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (connection != null) connection.disconnect();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("ptg", "onProgressUpdate: " + values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            printFinish();
        }
    }
}
