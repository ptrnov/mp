package com.cudo.mproject.Model.Response;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class ResponsePerson { //penjagaLahan

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
    @SerializedName("penjaga_lahan_site_project_id")
    private Integer penjaga_lahan_site_project_id;

    public ResponsePerson(Boolean error, String message) {
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

    public Integer getPenjaga_lahan_site_project_id() {
        return penjaga_lahan_site_project_id;
    }

    public String penjaga_lahan_site_project_id() {
        String isipenjaga_lahan_site_project_id = null;
        if (penjaga_lahan_site_project_id !=null) {
            isipenjaga_lahan_site_project_id =  penjaga_lahan_site_project_id.toString();
        }
        return isipenjaga_lahan_site_project_id;
    }


    public boolean isStatusSubmit() {
        return statusSubmit;
    }
}

