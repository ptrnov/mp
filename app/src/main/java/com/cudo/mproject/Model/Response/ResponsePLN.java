package com.cudo.mproject.Model.Response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class ResponsePLN {
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
    @SerializedName("data_pln_site_id")
    private Integer data_pln_site_id;

    public ResponsePLN(Boolean error, String message) {
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

    public Integer getData_pln_site_id() {
        return data_pln_site_id;
    }

    public String data_pln_site_id() {
        String isiData_pln_site_id = null;
        if (data_pln_site_id !=null) {
            isiData_pln_site_id =  data_pln_site_id.toString();
        }
        return isiData_pln_site_id;
    }

    public boolean isStatusSubmit() {
        return statusSubmit;
    }
}
