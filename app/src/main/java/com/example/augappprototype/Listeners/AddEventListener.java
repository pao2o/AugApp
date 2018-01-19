package com.example.augappprototype.Listeners;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.augappprototype.MainActivity;
import com.example.augappprototype.R;
import com.roomorama.caldroid.CaldroidFragment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Pao on 1/7/2018.
 * AddEventListener
 * implements View.OnClickListener
 * Responsible for all the popups that are displayed when the user is adding an event such as the
 * select date popup, the select time popup and then the enter event details popup.
 *
 * Methods:
 * onClick(View v)
 *      On click will either say not available to guests if in guest mode or bring up the select
 *      date popup if in student or faculty mode.
 * selectDate()
 *      Displays a popup that allows the user to select a date with a scroll bar
 * closeWindowListener(final Dialog addEvents)
 *      Dismisses the popup dialog that is on the screen when clicked
 * continueButtonListener(final Dialog addEvents)
 *      When clicked, will either go to the select time popup, enter event details popup, or
 *      dismiss the popup, adding the event
 * saveEventDate(DatePicker datePicker)
 *      Stores the date that the user entered
 * saveEventTime(TimePicker timePicker)
 *      Stores the time the user selected
 * saveEventDetails(EditText title, EditText location, EditText description)
 *      Converts the event details the user entered to a string
 * openEventTime()
 *      Displays the popup for the user to select a time
 * openEventDetails()
 *      Opens the popup for the event details
 * addEventButtonListener(final Dialog addEvents)
 *      Stores the details the user has entered
 * checkAmorPm(int hourOfDay)
 *      Checks if the time the user entered is AM or PM
 *
 */

public class AddEventListener implements View.OnClickListener {

    /*--Data--*/
    public static HashMap<Date, HashMap<Integer, ArrayList<String>>> allEvents = new HashMap<>();
    private int day;
    private int month;
    private int year;
    private int minute;
    private int hour;
    private String step;
    private final MainActivity mainActivity;
    private String amOrPm;
    EditText titleBox;
    EditText locationBox;
    EditText descriptionBox;
    public static String eventTitle;
    public static String eventDescription;
    public static String eventLocation;
    public static String eventTime;

    /*--Constructor--*/
    public AddEventListener(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    /*--Methods--*/
    /**
     * onClick(View) --> void
     * Calls the selectDate method when clicked which will bring up a scroll bar where the user
     * can select a day, month, and year. If in guest mode it will not be clickable and will
     * display a message saying Button is not available in guest mode
     * @param v
     */
    @Override
    public void onClick(View v) {
      //  if(GuestButtonListener.isGuest)//guests cannot add events
      //      Toast.makeText(mainActivity, "This button is not available on guest mode",
      //              Toast.LENGTH_SHORT).show();
      //  else
            selectDate();//students and faculty will be brought up to the select date popup
    }//onClick

    /**
     * selectDate() --> void
     * Creates a dialog popup that contains a scroll to select a day, month, and year and calls
     * the closeWindowListener and continueButtonListener which will either close the window going
     * back to the calendar or continue to the next dialog popup
     */
    public void selectDate(){
        final Dialog addEventDialog = new Dialog(mainActivity);
        addEventDialog.setContentView(R.layout.addeventpopup);
        addEventDialog.show();
        step = "date";
        closeWindowListener(addEventDialog);
        continueButtonListener(addEventDialog);
    }//selectDate

    /**
     * closeWindowListener(Dialog) --> void
     * If the closeWindow button is clicked, it will close the dialog and go back to the calendar
     * screen
     * @param addEvents
     */
    public void closeWindowListener(final Dialog addEvents){
        Button closeWindow = addEvents.findViewById(R.id.closeWindow);
        closeWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvents.dismiss();

            }//onClick
        });
    }//closeWindowListener

    /**
     * continueButtonListener(Dialog) --> void
     * Clicking the continue button throughout the event details will take the user to the select
     * time popup, the enter event details popup or close the dialog
     * @param addEvents
     */
    public void continueButtonListener(final Dialog addEvents){
        Button continueButton = addEvents.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
        DatePicker eventDate = addEvents.findViewById(R.id.datePicker);
        TimePicker eventTime = addEvents.findViewById(R.id.timePicker);
            @Override
            public void onClick(View v) {
                if (step == "date") {
                    saveEventDate(eventDate);
                    addEvents.dismiss();
                    openEventTime();
                    step = "time";
                }//if
                else if (step == "time"){
                    saveEventTime(eventTime);
                    addEvents.dismiss();
                    Toast.makeText(mainActivity, "lol" + minute,
                            Toast.LENGTH_SHORT).show();
                    openEventDetails();
                    step = "details";
                }//else
                else
                    addEvents.dismiss();
            }//onClick
        });
    }//continueButtonListener

    /**
     * saveEventDate(DatePicker) --> void
     * Stores the date that the user has selected on the select date dialog
     * @param datePicker
     */
    public void saveEventDate(DatePicker datePicker){
        year = datePicker.getYear() - 1900;
        month = datePicker.getMonth();
        day = datePicker.getDayOfMonth();
    }//saveEventDate

    /**
     * saveEventTime(TimePicker) --> void
     * Stores the time that the user has selected on the select time dialog
     * @param timePicker
     */
    public void saveEventTime(TimePicker timePicker){
        minute = timePicker.getCurrentMinute();
        String doubleDigitMinute = String.format("%02d", minute);
        hour = timePicker.getCurrentHour();
        if (hour > 12) {
            hour = (timePicker.getCurrentHour() - 12);
            amOrPm = "PM";
        }
        else if (hour == 0){
            hour = (timePicker.getCurrentHour() + 12);
            amOrPm = "AM";
        }
        else{
            hour = timePicker.getCurrentHour();
            amOrPm = "AM";
        }
        eventTime = hour + ":" + doubleDigitMinute + " " + amOrPm;
    }//saveEventTime

    /**
     * saveEventDetails(EditText, EditText, EditText) --> void
     * Converts event details to a string
     * @param title
     * @param location
     * @param description
     */
    public void saveEventDetails(EditText title, EditText location, EditText description){
        eventTitle = title.getText().toString();
        eventDescription = description.getText().toString();
        eventLocation = location.getText().toString();
    }//saveEventDetails

    /**
     * openEventTime() --> void
     * When called, will open the time dialog that consists of a scroll to choose the hour and
     * minute
     */
    public void openEventTime(){
        final Dialog addEventDialog = new Dialog(mainActivity);
        addEventDialog.setContentView(R.layout.addeventtime);
        addEventDialog.show();
        continueButtonListener(addEventDialog);
        closeWindowListener(addEventDialog);
    }//openEventTime

    /**
     * openEventDetails() --> void
     * When called, will open the event details dialog where the user enters the event title,
     * location, and description
     */
    public void openEventDetails(){
        final Dialog addEventDialog = new Dialog(mainActivity);
        addEventDialog.setContentView(R.layout.addeventdetails);
        addEventDialog.show();
        addEventButtonListener(addEventDialog);
        closeWindowListener(addEventDialog);
        locationBox = addEventDialog.findViewById(R.id.eventLocation);
        descriptionBox = addEventDialog.findViewById(R.id.eventDescription);
        titleBox = addEventDialog.findViewById(R.id.eventTitle);
    }//openEventDetails

    /**
     * addEventButtonListener(Dialog) --> void
     * Saves the events title, location, and description and adds them to an array list and then
     * puts the array list into a hash map that puts the event the date the user selected only if
     * the user has filled out every credential
     * @param addEvents
     */
    public void addEventButtonListener(final Dialog addEvents){
        Button continueButton = addEvents.findViewById(R.id.submitEvent);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEventDetails(titleBox, locationBox, descriptionBox);
                if (eventTitle.equals("")) {
                    Toast.makeText(mainActivity, "Please Enter An Event Title",
                            Toast.LENGTH_SHORT).show();
                }//else if
                else if (eventLocation.equals("")) {
                    Toast.makeText(mainActivity, "Please Enter An Event Location",
                            Toast.LENGTH_SHORT).show();
                }//else if
                else if (eventDescription.equals("")) {
                    Toast.makeText(mainActivity, "Please Enter An Event Description",
                            Toast.LENGTH_SHORT).show();
                }//else if
                else {
                    saveEvent(new Date(year, month, day));
                    Toast.makeText(mainActivity, "Event Added!",
                            Toast.LENGTH_LONG).show();
                    addEvents.dismiss();
                }//else
            }//onClick
        });
    }//addEventButtonListener

    /**
     * saveEvent(Date) --> void
     * stores the event details in an array list that is stored in a hashmap and sets the count for
     * the events on each day
     * @param eventDate
     */
    public void saveEvent(Date eventDate) {
        ArrayList<String> eventDetails = new ArrayList();
        HashMap<Integer, ArrayList<String>> events = new HashMap<>();
        eventDetails.add(0, eventTitle);
        eventDetails.add(1, eventTime);
        eventDetails.add(2, eventLocation);
        eventDetails.add(3, eventDescription);
        if (allEvents.containsKey(eventDate)) {//events in hash map
            allEvents.get(eventDate).put(allEvents.get(eventDate).size(), eventDetails);
        }
        else {
            events.put(events.size(), eventDetails);
            allEvents.put(eventDate, events);
        }
        mainActivity.setEventCount(eventDate, 0);
    }

    /**
     * checkAmOrPm(int) --> String
     * Checks if the time the user selected is in the AM or the PM
     * @param hourOfDay
     * @return String
     */
    private String checkAmOrPm(int hourOfDay){
        if (hourOfDay < 12)
            return "AM";
        else
            return "PM";
    }//checkAmOrPm
}//AddEventListener
