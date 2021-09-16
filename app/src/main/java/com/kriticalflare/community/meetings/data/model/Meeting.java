
package com.kriticalflare.community.meetings.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "meetings")
public class Meeting {
    public Meeting(@NonNull String _id, String agenda, String description, String time, String title) {
        this._id = _id;
        this.agenda = agenda;
        this.description = description;
        this.time = time;
        this.title = title;
    }

    @PrimaryKey
    @SerializedName("_id")
    @NonNull
    private String _id;
    @SerializedName("agenda")
    private String agenda;
    @SerializedName("description")
    private String description;
    @SerializedName("time")
    private String time;
    @SerializedName("title")
    private String title;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
