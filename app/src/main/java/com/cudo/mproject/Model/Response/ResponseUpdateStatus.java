package com.cudo.mproject.Model.Response;

import com.cudo.mproject.Model.MActivity;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ResponseUpdateStatus {

    @Getter
    @Setter
    @SerializedName("user")
    User user;
    @Getter
    @Setter
    @SerializedName("activity")
    List<MActivity> activity;

    @Getter
    @Setter
    String rcCodeActivity;

    public String rcCodeActivity() {
        String isiRcCode = null;
        if (rcCodeActivity.equals("1")) {
            isiRcCode = "1";
        }
        if (rcCodeActivity.equals("x001")) {
            isiRcCode = "x001";
        }
        if (rcCodeActivity.equals("x003")) {
            isiRcCode = "x003";
        }
        return isiRcCode;
    }
}
