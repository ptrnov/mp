package com.cudo.mproject.Model;

import org.w3c.dom.Text;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

public class DuplicateTemplate extends RealmObject {
    static String TAG = Photo.class.getSimpleName();
    public static String IdDuplicateTemplate= "Id";
    @PrimaryKey
    private int Id;
    @Getter
    @Setter
    private int dpt_id;
    @Getter
    @Setter
    private int pt_id;
    private String pt_code;
    private String pt_name;
    private String pt_desc;
    private int pt_wbs1_id;
    private int wbs1_id;
    private String wbs1_code;
    private String wbs1_name;
    private int wbs_wb_id;
    private int wb1_id;
    private int wbs_id;
    private String wbs_code;
    private String wbs_name;
    private int wbs_wp_id;
    private int wp_id;
}
