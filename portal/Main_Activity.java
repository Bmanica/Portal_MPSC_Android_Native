package com.snapcrap.bmanica.portal;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main_Activity extends AppCompatActivity {

    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private URL newUrl = null;
    private static String file_url = "http://www.mpsc.mp.br/spo/diariooficial/ultimaedicao";
    private static final String mapaUrl = "http://www.mpsc.mp.br/mobile/encontre-uma-promotoria-de-justica?bScroll=1&bApp=1";
    private static final String ouvidoriaUrl = "http://ouvidoria.mp.sc.gov.br:8080/mpweb/abrirCadastroManifestacao.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new DownloadFileFromURL().execute(file_url);

        setContentView(R.layout.main);
        overridePendingTransition(R.animator.fadein, R.animator.fadeout);
        Button noticias = (Button) findViewById(R.id.Button01);
        Button ouvidoria = (Button) findViewById(R.id.Button02);
        Button diario = (Button) findViewById(R.id.button1);
        Button diarioAnteriores = (Button) findViewById(R.id.button8);
        Button mapa = (Button) findViewById(R.id.button2);
        Button calculadora = (Button) findViewById(R.id.button4);
        Button twitter = (Button) findViewById(R.id.button5);
        Button youtube = (Button) findViewById(R.id.button6);
        Button facebook = (Button) findViewById(R.id.button7);

        noticias.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Main_Activity.this, News.class);
                        Main_Activity.this.startActivity(myIntent);
                    }
                }
        );
        ouvidoria.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle mapa = new Bundle();
                        mapa.putString("content", ouvidoriaUrl );
                        Intent myIntent = new Intent(Main_Activity.this, News_View.class);
                        myIntent.putExtras(mapa);
                        Main_Activity.this.startActivity(myIntent);
                    }
                }
        );
        diario.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/diario.pdf");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                }
        );
        diarioAnteriores.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Main_Activity.this, Diario.class);
                        Main_Activity.this.startActivity(myIntent);
                    }
                }
        );
        mapa.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle mapa = new Bundle();
                        mapa.putString("content", mapaUrl);
                        Intent myIntent = new Intent(Main_Activity.this, News_View.class);
                        myIntent.putExtras(mapa);
                        Main_Activity.this.startActivity(myIntent);
                    }
                }
        );
        calculadora.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(Main_Activity.this, Calculadora.class);
                        Main_Activity.this.startActivity(myIntent);
                    }
                }
        );
        twitter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
        youtube.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
        twitter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );

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
