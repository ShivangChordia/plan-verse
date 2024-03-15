package com.example.assignment_2;

/*
File: RandomActivity.java
Project: Assignment 2 - Mobile App Dev
Programmers: Shivang Chordia, 8871092, schordia1092@conestogac.on.ca
First Version: 11 March, 2024
Description: It is the Java page for Random Activity
*/

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RandomActivity extends Activity {

    private TextView randomType, randomActivity, randomParticipants, price, link;

    private EditText type, noOfPeople;

    Button openLinkButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_activity);
        openLinkButton = findViewById(R.id.goToLink);
        type = findViewById(R.id.ActivityNameInput);
        noOfPeople = findViewById(R.id.numberOfPeopleInput);

        randomActivity = findViewById(R.id.activityName);
        randomType = findViewById(R.id.type);
        randomParticipants = findViewById(R.id.participants);
        price = findViewById(R.id.price);
        link = findViewById(R.id.link);

        Button btnGenerateEvent = findViewById(R.id.generateActivityButton);
        btnGenerateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityType = type.getText().toString();
                String numberOfPeople = noOfPeople.getText().toString();
                new FetchActivityTask().execute(activityType, numberOfPeople);
            }
        });


        openLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink();
            }
        });
    }

    private void openLink() {
        String url = link.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    private class FetchActivityTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            String type = params[0];
            String numberOfPeople = params[1];
            JSONObject result = null;
            try {
                URL url;
                if ((params[0] == null && params[1] == null) || (params[0].isEmpty() && params[1].isEmpty())) {
                    url = new URL("https://www.boredapi.com/api/activity");
                }
                else if (params[0] == null || params[0].isEmpty())
                {
                    url = new URL("https://www.boredapi.com/api/activity?type=" + type);
                }
                else if(params[1] == null || params[1].isEmpty())
                {
                    url = new URL("https://www.boredapi.com/api/activity?participants=" + numberOfPeople);
                }
                else {
                    url = new URL("https://www.boredapi.com/api/activity?type=" + type + "&participants=" + numberOfPeople);
                }
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    result = new JSONObject(stringBuilder.toString());
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            if (result == null) {

                return;
            }
            try {

                TextView activityLabel, participantLabel, typeLabel, priceLabel,linkLabel;

                activityLabel=findViewById(R.id.activityNameLabel);
                participantLabel=findViewById(R.id.participantsLabel);
                typeLabel=findViewById(R.id.typeLabel);
                priceLabel=findViewById(R.id.priceLabel);
                linkLabel=findViewById(R.id.linkLabel);

                activityLabel.setVisibility(View.VISIBLE);
                participantLabel.setVisibility(View.VISIBLE);
                typeLabel.setVisibility(View.VISIBLE);
                priceLabel.setVisibility(View.VISIBLE);



                randomActivity.setText(result.getString("activity"));
                randomType.setText(result.getString("type"));
                randomParticipants.setText(result.getString("participants"));
                price.setText(result.getString("price"));
                link.setText(result.getString("link"));

                if (link.getText().toString().isEmpty()) {
                    openLinkButton.setVisibility(View.GONE);
                    linkLabel.setVisibility(View.GONE);
                } else {
                    openLinkButton.setVisibility(View.VISIBLE);
                    linkLabel.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}