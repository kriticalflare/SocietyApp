
package com.kriticalflare.community.meetings.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MeetingsResponse {

    @SerializedName("meetingsList")
    private List<Meeting> meeting;

    public List<Meeting> getMeetingsList() {
        return meeting;
    }

    public void setMeetingsList(List<Meeting> meeting) {
        this.meeting = meeting;
    }
}
