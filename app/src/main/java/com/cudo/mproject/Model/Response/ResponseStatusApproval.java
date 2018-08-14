package com.cudo.mproject.Model.Response;

import com.cudo.mproject.Model.MActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ResponseStatusApproval {
    @Getter
    @Setter
    JsonArray jsonArray;
    public void setJsonArray(JsonArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public List<MActivity> getAs_id()
    {
        List<MActivity>list = new ArrayList<>();
        MActivity mActivity;
        for (JsonElement jsonElement : jsonArray) {
            mActivity = new Gson().fromJson(jsonElement,MActivity.class);
            list.add(mActivity);
        }

        return list;
    }
}
