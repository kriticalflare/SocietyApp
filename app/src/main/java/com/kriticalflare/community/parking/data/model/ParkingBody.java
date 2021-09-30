package com.kriticalflare.community.parking.data.model;

import com.google.gson.annotations.SerializedName;

public class ParkingBody {
    @SerializedName("qrCode")
    private String qrCode;

    public ParkingBody(String qrCode){
        this.qrCode = qrCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
