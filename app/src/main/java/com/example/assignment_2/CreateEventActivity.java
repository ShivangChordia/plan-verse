package com.example.assignment_2;

/*
File: CreateEventActivity.java
Project: Assignment 2 - Mobile App Dev
Programmers: Shivang Chordia, 8871092, schordia1092@conestogac.on.ca
First Version: 11 March, 2024
Description: It is the Java page for Create Event Activity
*/

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

public class CreateEventActivity extends ComponentActivity {

    private Button saveEvent;
    private EditText eventNameInput;
    private EditText descriptionInput;
    private EditText numberOfPeopleInput;
    private EditText dateInput;

    private long eventId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        eventId = sharedPreferences.getLong("eventId", -1);
        String eventName = sharedPreferences.getString("eventName", "");
        int noOfPeople = sharedPreferences.getInt("noOfPeople", 0);
        String eventDate = sharedPreferences.getString("eventDate", "");
        String eventDescription = sharedPreferences.getString("eventDescription", "");

        saveEvent = findViewById(R.id.saveEventButton);
        eventNameInput = findViewById(R.id.eventNameInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        numberOfPeopleInput = findViewById(R.id.numberOfPeopleInput);
        dateInput = findViewById(R.id.dateInput);


        if (eventId != -1) {
            eventNameInput.setText(eventName);
            descriptionInput.setText(eventDescription);
            numberOfPeopleInput.setText(String.valueOf(noOfPeople));
            dateInput.setText(eventDate);
            saveEvent.setText("Update Event");
        }

        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String eventName = eventNameInput.getText().toString().trim();
                String description = descriptionInput.getText().toString().trim();

                if (TextUtils.isEmpty(eventName)) {
                    showToast("Please enter event name");
                    return;
                }

                if (TextUtils.isEmpty(description)) {
                    showToast("Please enter event description");
                    return;
                }

                int numberOfPeople = 0;
                try {
                    numberOfPeople = Integer.parseInt(numberOfPeopleInput.getText().toString());
                } catch (NumberFormatException e) {
                    showToast("Please enter a valid number for number of people");
                    return;
                }

                String date = dateInput.getText().toString().trim();

                if (TextUtils.isEmpty(date)) {
                    showToast("Please enter event date");
                    return;
                }


                DatabaseHelper db = new DatabaseHelper(CreateEventActivity.this);
                if (eventId == -1) {
                    // Create a new event
                    long newEventId = db.insertData(eventName, description, numberOfPeople, date);
                    showToast("Event created with ID: " + newEventId);
                } else {
                    // Update the existing event
                    boolean isUpdated = db.updateEvent(eventId, eventName, description, numberOfPeople, date);
                    if (isUpdated) {
                        showToast("Event updated successfully");
                    } else {
                        showToast("Failed to update event");
                    }
                }
                sharedPreferences.edit().clear().apply();


                finish();


            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
