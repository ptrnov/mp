package com.cudo.mproject.Model.Response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class ResponseIMB {
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

    @Getter
    @Setter
    @SerializedName("data_imb_site_id")
    private Integer data_imb_site_id;

    public ResponseIMB(Boolean error, String message) {
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

    public Integer getData_imb_site_id() {
        return data_imb_site_id;
    }

    public String data_imb_site_id() {
        String isiData_imb_site_id = null;
        if (data_imb_site_id !=null) {
            isiData_imb_site_id =  data_imb_site_id.toString();
        }
        return isiData_imb_site_id;
    }

    public boolean isStatusSubmit() {
        return statusSubmit;
    }
}
