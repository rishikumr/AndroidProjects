package com.dynasty.myapplication.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dynasty.myapplication.utils.Constants;
import com.dynasty.myapplication.utils.EventImagesConverter;
import com.dynasty.myapplication.utils.EventPeopleConverter;

import java.util.ArrayList;


@Entity(tableName = Constants.TABLE_NAME)
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String event_name;
    private String event_date;
    private String event_location;
    private String event_description;
    private String event_creator;
    @TypeConverters({EventImagesConverter.class})
    private ArrayList<String> event_imgURIs;
    @TypeConverters({EventPeopleConverter.class})
    private ArrayList<People> event_invitingList;



    @Ignore
    public Event(String event_name, String event_creator , String event_date, String event_location, String event_description, ArrayList<String>  event_imgURIs , ArrayList<People> event_invitingList) {
        this.event_name = event_name;
        this.event_creator = event_creator;
        this.event_date = event_date;
        this.event_imgURIs = event_imgURIs;
        this.event_location = event_location;
        this.event_description = event_description;
        this.event_invitingList = event_invitingList;
    }

    public Event() {
        this.event_name = "event_name";
        this.event_creator= "event_creator";
        this.event_date = "event_date";
        this.event_imgURIs = new ArrayList<String>();
        this.event_location = "event_location";
        this.event_description = "event_description";
        this.event_invitingList = new ArrayList<People>();
    }




    public int getId() {
        return id;
    }
    public String getEvent_name() {
        return event_name;
    }
    public String getEvent_creator() {        return event_creator;    }
    public String getEvent_date() {
        return event_date;
    }
    public String getEvent_location() {
        return event_location;
    }
    public String getEvent_description() {
        return event_description;
    }
    public ArrayList<String> getEvent_imgURIs() {        return event_imgURIs;    }
    public ArrayList<People> getEvent_invitingList() {
        return event_invitingList;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }
    public void setEvent_creator(String event_creator) {        this.event_creator = event_creator;    }
    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }
    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }
    public void setEvent_description(String event_description) {        this.event_description = event_description;    }
    public void setEvent_imgURIs(ArrayList<String> event_imgURIs) {        this.event_imgURIs = event_imgURIs;    }
    public void setEvent_invitingList(ArrayList<People> event_invitingList) {        this.event_invitingList = event_invitingList;    }


}
