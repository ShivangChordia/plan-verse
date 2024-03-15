package com.example.assignment_2;

/*
File: checklistActivity.java
Project: Assignment 2 - Mobile App Dev
Programmers: Shivang Chordia, 8871092, schordia1092@conestogac.on.ca
First Version: 11 March, 2024
Description: It is the Java page for checklist Activity
*/

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import java.util.ArrayList;

public class ChecklistActivity extends ComponentActivity {

    private static final String TAG = "ChecklistActivity";
    private LinearLayout checklistLayout;
    private EditText editTextItem;
    private DatabaseHelper dbHelper;
    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist);
        Intent intent = getIntent();
        checklistLayout = findViewById(R.id.checklistLayout);
        editTextItem = findViewById(R.id.editTextItem);
        dbHelper = new DatabaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        eventId = (int) sharedPreferences.getLong("eventId", -1); // Default value is -1



        Button btnAddItem = findViewById(R.id.btnAddItem);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToChecklist();
            }
        });

        // Load checklist items from the database
        loadChecklistItems();
    }

    private void addItemToChecklist() {
        String newItemText = editTextItem.getText().toString().trim();
        if (!newItemText.isEmpty()) {
            dbHelper.addChecklistItem(eventId, newItemText);
            editTextItem.setText("");
            Toast.makeText(this, "Item added to checklist", Toast.LENGTH_SHORT).show();
            loadChecklistItems(); // Refresh the checklist UI
        } else {
            Toast.makeText(this, "Please enter a valid item", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadChecklistItems() {
        Cursor cursor = dbHelper.getChecklistItemsForEvent(eventId);
        ArrayList<String> checklistItems = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String itemText = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ITEM_TEXT));
                checklistItems.add(itemText);
            } while (cursor.moveToNext());
        }

        cursor.close();

        checklistLayout.removeAllViews();

        for (String item : checklistItems) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(item);
            checkBox.setTextSize(16);
            checklistLayout.addView(checkBox);
        }
    }



}
