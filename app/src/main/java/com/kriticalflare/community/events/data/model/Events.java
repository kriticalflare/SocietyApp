package com.kriticalflare.community.events.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "events")
public class Events {

    @SerializedName("description")
    private String description;

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id;

    @SerializedName("time")
    private String time;

    @SerializedName("title")
    private String title;

    @SerializedName("agenda")
    private String agenda;

    public Events(String description, @NonNull String id, String time, String title, String agenda) {
        this.description = description;
        this.id = id;
        this.time = time;
        this.title = title;
        this.agenda = agenda;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getAgenda() {
        return agenda;
    }

    @Override
    public String toString() {
        return
                "EventListItem{" +
                        "description = '" + description + '\'' +
                        ",_id = '" + id + '\'' +
                        ",time = '" + time + '\'' +
                        ",title = '" + title + '\'' +
                        ",agenda = '" + agenda + '\'' +
                        "}";
    }
}