package com.cudo.mproject.Model.Response;

import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by adsxg on 12/11/2017.
 */

public class ResponseLogin {
    @Getter
    @Setter
    @SerializedName("user")
    User user;
    @Getter
    @Setter
    @SerializedName("project")
    List<Project> projects;

    @Getter
    @Setter
    String rcCode;

    public String rcCode() {
        String isiRcCode = null;
        if (rcCode.equals("1")) {
            isiRcCode = "1";
        }
        if (rcCode.equals("x001")) {
            isiRcCode = "x001";
        }
        if (rcCode.equals("x003")) {
            isiRcCode = "x003";
        }
        return isiRcCode;
    }
}

