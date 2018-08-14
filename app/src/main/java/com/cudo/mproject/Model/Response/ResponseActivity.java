package com.cudo.mproject.Model.Response;

import com.cudo.mproject.Model.MActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by adsxg on 12/11/2017.
 */

public class ResponseActivity {
    @Getter
    @Setter
    JsonArray jsonArray;

    public List<MActivity> getMActivityList() {
        List<MActivity> list = new ArrayList<>();
        MActivity workPkg;
        for (JsonElement jsonElement : jsonArray) {
            workPkg = new Gson().fromJson(jsonElement, MActivity.class);
            list.add(workPkg);
        }

        return list;
    }

    public void setJsonArray(JsonArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
