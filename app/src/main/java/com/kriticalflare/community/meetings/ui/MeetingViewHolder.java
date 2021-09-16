package com.kriticalflare.community.meetings.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kriticalflare.community.R;
import com.kriticalflare.community.databinding.ItemMeetingBinding;
import com.kriticalflare.community.meetings.data.model.Meeting;

public class MeetingViewHolder extends RecyclerView.ViewHolder {
    private ItemMeetingBinding binding;

    public MeetingViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemMeetingBinding.bind(itemView);
    }

    public void bind(Meeting meeting) {
        binding.meetingAgenda.setText(meeting.getAgenda());
        binding.meetingDate.setText(itemView.getContext().getString(R.string.date_representation, meeting.getTime()));
    }
}
