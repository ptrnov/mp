package com.cudo.mproject.Model.Response;

import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by adsxg on 2/21/2018.
 */

public class ResponseLogoff {
    @Getter
    @Setter
    @SerializedName("user")
    User user;

    private String rcCode;

    public ResponseLogoff(User user, String rcCode) {
        this.user = user;
        this.rcCode = rcCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRcCode() {
        return rcCode;
    }

    public void setRcCode(String rcCode) {
        this.rcCode = rcCode;
    }
}
