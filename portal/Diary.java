package com.snapcrap.bmanica.portal;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Diary extends AppCompatActivity {

    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private static String file_url = "http://www.mpsc.mp.br/spo/diariooficial/ultimaedicao";
    private URL newUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        overridePendingTransition(R.animator.fadein, R.animator.fadeout);

        new DownloadFileFromURL().execute(file_url);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/diario.pdf");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Fazendo o download to arquivo.");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
        }
        return null;
    }
    public String redirectedUrl(String f_url) {
        String redirectedURL = null;
        try {
            String newLocationHeader = null;
            URL url = new URL(f_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.connect();
            connection.getInputStream();

            while(connection.getResponseCode() > 300 && connection.getResponseCode() < 400) {
                redirectedURL = connection.getHeaderField("Location");
                newUrl = new URL(redirectedURL);
                connection = (HttpURLConnection) newUrl.openConnection();
            }
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        return redirectedURL;
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String newLocationHeader = null;
                URL url = new URL(f_url[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setInstanceFollowRedirects(false);
                connection.connect();
                connection.getInputStream();

                while(connection.getResponseCode() > 300 && connection.getResponseCode() < 400) {
                    String redirectedURL = connection.getHeaderField("Location");
                    newUrl = new URL(redirectedURL);
                    Log.d("URL", redirectedURL);
                    connection = (HttpURLConnection) newUrl.openConnection();
                }

                connection.connect();

                int fileLenght = connection.getContentLength();

                InputStream input = new BufferedInputStream(newUrl.openStream(), 8192);

                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/diario.pdf");

                byte data[] = new byte[1024];

                long total = 0;

                while((count = input.read(data)) != -1) {
                    total += count;

                    publishProgress("" + (int) ((total*100)/fileLenght));

                    output.write(data, 0, count);
                }

                output.flush();

                output.close();
                input.close();


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(progress_bar_type);
        }
    }

}
