package com.kriticalflare.community.emergency.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.kriticalflare.community.databinding.ItemEmergencyBinding;
import com.kriticalflare.community.emergency.data.model.EmergencyItem;

public class EmergencyViewHolder extends RecyclerView.ViewHolder {

    private ItemEmergencyBinding binding;

    public EmergencyViewHolder(View itemView) {
        super(itemView);
        binding = ItemEmergencyBinding.bind(itemView);
    }

    public void bind(EmergencyItem data) {
        binding.emergencyNumber.setText(data.getNumber());
        binding.emergencyTitle.setText(data.getJobTitle());
        binding.callButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + data.getNumber()));
            if (intent.resolveActivity(binding.getRoot().getContext().getPackageManager()) != null) {
                binding.getRoot().getContext().startActivity(intent);
            }

        });
    }
}
