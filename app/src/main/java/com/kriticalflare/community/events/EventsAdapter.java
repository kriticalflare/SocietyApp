package com.kriticalflare.community.events;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.kriticalflare.community.databinding.ItemEventBinding;
import com.kriticalflare.community.events.data.model.Events;

public class EventsAdapter extends ListAdapter<Events, EventsViewHolder> {
    protected EventsAdapter() {
        super(EventsAdapter.DIFF_CALLBACK);
    }

    private ItemEventBinding binding;

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemEventBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EventsViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static final DiffUtil.ItemCallback<Events> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Events>() {

                @Override
                public boolean areItemsTheSame(@NonNull Events oldItem, @NonNull Events newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull Events oldItem, @NonNull Events newItem) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldItem.getId().equals(newItem.getId());
                }
            };
}
