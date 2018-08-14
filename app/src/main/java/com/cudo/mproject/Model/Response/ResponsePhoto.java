package com.cudo.mproject.Model.Response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by adsxg on 12/11/2017.
 */

public class ResponsePhoto {
    @SerializedName("status")
    @Getter
    @Setter
    String status;

    public boolean isSucces()
    {
        return status.equals("success");
    }
}
