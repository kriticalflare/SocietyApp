package com.kriticalflare.community.emergency.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.kriticalflare.community.databinding.ItemEmergencyBinding;
import com.kriticalflare.community.emergency.data.model.EmergencyItem;

public class EmergencyAdapter extends ListAdapter<EmergencyItem, EmergencyViewHolder> {

    private ItemEmergencyBinding binding;

    EmergencyAdapter() {
        super(EmergencyAdapter.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public EmergencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemEmergencyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EmergencyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyViewHolder holder, int position) {
        holder.bind(getCurrentList().get(position));
    }

    public static final DiffUtil.ItemCallback<EmergencyItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<EmergencyItem>() {

                @Override
                public boolean areItemsTheSame(@NonNull EmergencyItem oldItem, @NonNull EmergencyItem newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull EmergencyItem oldItem, @NonNull EmergencyItem newItem) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldItem.getId().equals(newItem.getId());
                }
            };
}
