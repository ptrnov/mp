package com.cudo.mproject.Model.Post;

import android.widget.TextView;

import com.cudo.mproject.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import butterknife.BindView;

public class DataIMBPost {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("namaPemilikLahan")
    @Expose
    private String namaPemilikLahan;
    @SerializedName("noIMB")
    @Expose
    private String noIMB;
    @SerializedName("alamatIMB")
    @Expose
    private String alamatIMB;
    @SerializedName("pathPhotoIMB")
    @Expose
    private String pathPhotoIMB;

    @SerializedName("tglberlakuAwal")
    @Expose
    private String tglberlakuAwal;
    @SerializedName("tglberlakuAkhir")
    @Expose
    private String tglberlakuAkhir;

    public DataIMBPost(Integer id, String namaPemilikLahan, String noIMB, String alamatIMB, String pathPhotoIMB, String tglberlakuAwal, String tglberlakuAkhir) {
        this.id = id;
        this.namaPemilikLahan = namaPemilikLahan;
        this.noIMB = noIMB;
        this.alamatIMB = alamatIMB;
        this.pathPhotoIMB = pathPhotoIMB;
        this.tglberlakuAwal = tglberlakuAwal;
        this.tglberlakuAkhir = tglberlakuAkhir;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaPemilikLahan() {
        return namaPemilikLahan;
    }

    public void setNamaPemilikLahan(String namaPemilikLahan) {
        this.namaPemilikLahan = namaPemilikLahan;
    }

    public String getNoIMB() {
        return noIMB;
    }

    public void setNoIMB(String noIMB) {
        this.noIMB = noIMB;
    }

    public String getAlamatIMB() {
        return alamatIMB;
    }

    public void setAlamatIMB(String alamatIMB) {
        this.alamatIMB = alamatIMB;
    }

    public String getPathPhotoIMB() {
        return pathPhotoIMB;
    }

    public void setPathPhotoIMB(String pathPhotoIMB) {
        this.pathPhotoIMB = pathPhotoIMB;
    }

    public String getTglberlakuAwal() {
        return tglberlakuAwal;
    }

    public void setTglberlakuAwal(String tglberlakuAwal) {
        this.tglberlakuAwal = tglberlakuAwal;
    }

    public String getTglberlakuAkhir() {
        return tglberlakuAkhir;
    }

    public void setTglberlakuAkhir(String tglberlakuAkhir) {
        this.tglberlakuAkhir = tglberlakuAkhir;
    }

    @Override
    public String toString() {
        return "postDataIMB{" +
                "  namaPemilikLahan='" + namaPemilikLahan + '\'' +
                ", noIMB='" + noIMB + '\'' +
                ", alamatIMB='" + alamatIMB + '\'' +
                ", pathPhotoIMB='" + pathPhotoIMB + '\'' +
                ", tglberlakuAwal='" + tglberlakuAwal + '\'' +
                ", tglberlakuAkhir='" + tglberlakuAkhir +
                '}';
    }
}
