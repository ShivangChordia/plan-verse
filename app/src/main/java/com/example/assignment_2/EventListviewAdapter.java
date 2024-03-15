package com.example.assignment_2;

/*
File: EventListViewActivity.java
Project: Assignment 2 - Mobile App Dev
Programmers: Shivang Chordia, 8871092, schordia1092@conestogac.on.ca
First Version: 11 March, 2024
Description: It is the Adapter to populate the list view
*/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EventListviewAdapter extends BaseAdapter {

    private static final String TAG = "EventListviewAdapter";

    private Context context;

    private Activity activity;
    private List<Event> eventList;
    private LayoutInflater layoutInflater;
    private DatabaseHelper db;
    private static final int MODE_PRIVATE = 0;

    public EventListviewAdapter(Activity activity, List<Event> events) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.eventList = events;
        layoutInflater = LayoutInflater.from(this.context);
        this.db = new DatabaseHelper(this.context);
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.event_list_view, null);
            }

            TextView eventNameTextView = convertView.findViewById(R.id.eventNameTextView);
            TextView numberOfPeopleTextView = convertView.findViewById(R.id.numberOfPeopleTextView);
            TextView eventDateTextView = convertView.findViewById(R.id.eventDateTextView);
            TextView eventDescriptionTextView = convertView.findViewById(R.id.eventDescriptionTextView);

            final Event currentEvent = eventList.get(position);

            eventNameTextView.setText(currentEvent.getEventName());
            numberOfPeopleTextView.setText("No. of People: " + currentEvent.getNoOfPeople());
            eventDateTextView.setText("Date: " + currentEvent.getEventDate());
            eventDescriptionTextView.setText("Description: " + currentEvent.getEventDescription());


            Button deleteButton = convertView.findViewById(R.id.delete_event_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.deleteEvent(currentEvent.getId());


                    eventList.remove(position);
                    notifyDataSetChanged();


                    Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show();
                }
            });

            Button checkListButton = convertView.findViewById(R.id.btnMakeChecklist);
            checkListButton.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(activity, ChecklistActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    SharedPreferences sharedPreferences = activity.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putLong("eventId", currentEvent.getId());
                    myEdit.apply();
                    context.startActivity(intent);
                }
            });

            Button editButton = convertView.findViewById(R.id.edit_event_button); // Assuming you have an edit button in your layout
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences sharedPreferences = activity.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putLong("eventId", currentEvent.getId());
                    myEdit.putString("eventName", currentEvent.getEventName());
                    myEdit.putInt("noOfPeople", currentEvent.getNoOfPeople());
                    myEdit.putString("eventDate", currentEvent.getEventDate());
                    myEdit.putString("eventDescription", currentEvent.getEventDescription());
                    myEdit.apply();


                    Intent intent = new Intent(activity, CreateEventActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error in getView at position: " + position, e);
            Toast.makeText(context, "Error loading event at position: " + position, Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }
}
