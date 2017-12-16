package com.example.novak.dayostrackos;

/**
 * Created by novak on 16-Dec-17.
 */

public class Record {

    public String id;
    public String title;
    public String text;
    public String type;
    public String datetime;
    public String category;
    public String location;
    public String link_to_resource;


    public Record(String title, String text, String type, String datetime, String category, String location, String link_to_resource)
    {
        this.title = title;
        this.text = text;
        this.type = type;
        this.datetime = datetime;
        this.category = category;
        this.location = location;
        this.link_to_resource = link_to_resource;
    }
}
