package com.example.assignment_2;

/*
File: infoActivity.java
Project: Assignment 2 - Mobile App Dev
Programmers: Shivang Chordia, 8871092, schordia1092@conestogac.on.ca
First Version: 11 March, 2024
Description: It is the Java page for Info Activity
*/

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class InfoActivity extends Activity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private ProgressBar progressBar;

    private TextView downloadStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);

        Button btnAuthor = findViewById(R.id.btnAuthor);
        Button btnResume = findViewById(R.id.btnAuthorResume);
        Button btnContact = findViewById(R.id.btnAuthorContact);
        progressBar = findViewById(R.id.progressBar);
        downloadStatusTextView = findViewById(R.id.downloadStatusTextView);

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "3658553455";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });

        btnAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://shivangchordia.me";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://shivangchordia.me/Shivang%20Resume.pdf";
                new DownloadPdfTask().execute(url);
            }
        });
    }

    private class DownloadPdfTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            downloadStatusTextView.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            String pdfFilePath = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (!isExternalStorageWritable()) {
                    return null;
                }

                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File outputFile = new File(dir, "Shivang_Resume.pdf");
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = connection.getInputStream();

                byte[] buffer = new byte[1024];
                int len;
                int total = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    total += len;
                    publishProgress((total * 100) / connection.getContentLength());
                }

                fos.close();
                is.close();

                pdfFilePath = outputFile.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return pdfFilePath;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String pdfFilePath) {
            super.onPostExecute(pdfFilePath);
            progressBar.setVisibility(View.GONE);
            downloadStatusTextView.setVisibility(View.GONE);
            if (pdfFilePath != null) {
                openPdfFile(pdfFilePath);
            } else {
                Toast.makeText(InfoActivity.this, "Failed to download PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openPdfFile(String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        Uri uri = FileProvider.getUriForFile(InfoActivity.this, "com.example.assignment_2.provider", file);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(InfoActivity.this, "No PDF viewer app found", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
