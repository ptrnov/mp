package com.cudo.mproject.Model.Response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;


public class ResponseSubmitAlamatActual {

    @SerializedName("statusSubmit")
    @Getter
    @Setter
    boolean statusSubmit;
    @Getter
    @Setter
    String rcCode;

    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    public ResponseSubmitAlamatActual(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String rcCode() {
        String isiRcCode = null;
        if (rcCode.equals("1")) {
            isiRcCode = "1";
        }
        return isiRcCode;
    }

    public boolean isStatusSubmit() {
        return statusSubmit;
    }
}
