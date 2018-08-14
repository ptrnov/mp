package com.cudo.mproject.Model.Response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by adsxg on 12/11/2017.
 */

public class ResponseSubmit {

    @SerializedName("status")
    @Getter
    @Setter
    String status;

    @SerializedName("pa_id")
    @Getter@Setter
    String pa_id;

    public boolean isSucces()
    {
        return status.equals("OK");
    }

    public String getPa_id() {
        return pa_id;
    }
}

