/*
 *  FILE : MainActivity.java
*   PROJECT : PROG3150 – Mobile Application Development - Assignment 1
*   PROGRAMMER : Shivang Chordia - schordia1092@conestogac.on.ca - 8871092
*   VERSION : 14th Feb 2024
 *  DESCRIPTION : This file includes the functions to assemble the main activity and output it
 */

package com.example.assignment_2;

/*
File: mainActivity.java
Project: Assignment 2 - Mobile App Dev
Programmers: Shivang Chordia, 8871092, schordia1092@conestogac.on.ca
First Version: 11 March, 2024
Description: It is the Java page for Main Activity
*/

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button myEventButton;
    private Button checkListButton;

    private Button editButton;
    private Button deleteButton;
    private ListView eventListView;
    private EventListviewAdapter eventListviewAdapter;
    private List<Event> events;

    private DatabaseHelper db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        db = new DatabaseHelper(this);

        myEventButton = findViewById(R.id.btnAddEvent);
        myEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateEventActivity();
            }
        });

        eventListView = findViewById(R.id.upcomingEventsListView);
        events = new ArrayList<>();
        eventListviewAdapter = new EventListviewAdapter(MainActivity.this, events);
        eventListView.setAdapter(eventListviewAdapter);


        showNoEventsMessage();


        loadEventsFromDatabase();

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Event clickedEvent = events.get(position);

                Button editButton = view.findViewById(R.id.edit_event_button); // Assuming the button's id is editButton
                Button checkListButton = view.findViewById(R.id.btnMakeChecklist); // Assuming the button's id is editButton
                Button deleteButton = view.findViewById(R.id.delete_event_button); // Assuming the button's id is editButton

                if (editButton.getVisibility() == View.VISIBLE) {
                    editButton.setVisibility(View.GONE);
                    checkListButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                } else {
                    editButton.setVisibility(View.VISIBLE);
                    checkListButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                }

            }
        });
    }





    private static final int CREATE_EVENT_REQUEST_CODE = 1;

    public void startCreateEventActivity() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivityForResult(intent, CREATE_EVENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_EVENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                loadEventsFromDatabase();
            }
        }
    }




    private void showNoEventsMessage() {
        TextView noEventsTextView = findViewById(R.id.noEventsTextView);
        if (events.isEmpty()) {
            noEventsTextView.setVisibility(View.VISIBLE);
        } else {
            noEventsTextView.setVisibility(View.GONE);
        }
    }

    private void loadEventsFromDatabase() {
        Cursor data = db.getAllData();
        events.clear();
        if (data.getCount() == 0) {
            return;
        } else {
            while (data.moveToNext()) {
                long id = data.getLong(data.getColumnIndex(DatabaseHelper.COL1)); // Retrieve the ID
                String eventName = data.getString(data.getColumnIndex(DatabaseHelper.COL2));
                String description = data.getString(data.getColumnIndex(DatabaseHelper.COL3));
                int numberOfPeople = data.getInt(data.getColumnIndex(DatabaseHelper.COL4));
                String date = data.getString(data.getColumnIndex(DatabaseHelper.COL5));
                Event event = new Event(id, eventName, description, numberOfPeople, date); // Use the ID
                events.add(event);
            }
            eventListviewAdapter.notifyDataSetChanged();
            showNoEventsMessage();
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Log when the activity is paused
        Log.d("MainActivity", "onPause: Events saved to SharedPreferences");
    }

    @Override
    protected void onResume() {
        super.onResume();

            loadEventsFromDatabase();


        Log.d("MainActivity", "onResume: Events loaded from SharedPreferences");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Log when the configuration changes
        Log.d("MainActivity", "onConfigurationChanged: Events saved to SharedPreferences");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        if (item.getItemId() == R.id.menu_info) {
            intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.mymenu_images) {
            intent = new Intent(this, RandomActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
