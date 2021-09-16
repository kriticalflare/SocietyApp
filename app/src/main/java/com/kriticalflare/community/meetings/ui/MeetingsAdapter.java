package com.kriticalflare.community.meetings.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.kriticalflare.community.databinding.ItemMeetingBinding;
import com.kriticalflare.community.meetings.data.model.Meeting;

public class MeetingsAdapter extends ListAdapter<Meeting, MeetingViewHolder> {
    private ItemMeetingBinding binding;

    MeetingsAdapter() {
        super(MeetingsAdapter.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemMeetingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MeetingViewHolder(binding.getRoot());
    }


    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        holder.bind(getCurrentList().get(position));
    }

    public static final DiffUtil.ItemCallback<Meeting> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Meeting>() {

                @Override
                public boolean areItemsTheSame(@NonNull Meeting oldItem, @NonNull Meeting newItem) {
                    return oldItem.get_id().equals(newItem.get_id());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull Meeting oldItem, @NonNull Meeting newItem) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldItem.get_id().equals(newItem.get_id());
                }
            };
}
