package com.example.assignment_2;


/*
File: Event.java
Project: Assignment 2 - Mobile App Dev
Programmers: Shivang Chordia, 8871092, schordia1092@conestogac.on.ca
First Version: 11 March, 2024
Description: It is the Java page for Event class to make object for populating the list view
*/

import java.util.ArrayList;
import java.util.List;

public class Event {
    private long id; // Unique identifier for the event
    private String eventName;
    private String eventDescription;
    private int noOfPeople;
    private String eventDate;

    public Event(long id, String eventName, String eventDescription, int noOfPeople, String eventDate) {
        this.id = id;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.noOfPeople = noOfPeople;
        this.eventDate = eventDate;
    }

    public long getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public int getNoOfPeople() {
        return noOfPeople;
    }

    public String getEventDate() {
        return eventDate;
    }
}
