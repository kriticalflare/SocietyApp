package com.kriticalflare.community.events;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kriticalflare.community.databinding.ItemEventBinding;
import com.kriticalflare.community.events.data.model.Events;

public class EventsViewHolder extends RecyclerView.ViewHolder {
    public EventsViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemEventBinding.bind(itemView);
    }

    private ItemEventBinding binding;

    public void bind(Events data) {
        binding.eventAgenda.setText(data.getAgenda());
        binding.eventDate.setText(data.getTime());
    }
}
