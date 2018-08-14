package com.cudo.mproject.Model.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by adsxg on 2/23/2018.
 */

public class DataActualPost {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("siteName")
    @Expose
    private String siteName;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("provinsi")
    @Expose
    private String provinsi;
    @SerializedName("kabupaten")
    @Expose
    private String kabupaten;
    @SerializedName("kecamatan")
    @Expose
    private String kecamatan;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("projectId")
    @Expose
    private String projectId;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("isOnline")
    @Expose
    private String isOnline;

    public DataActualPost(){

    }

    public DataActualPost(Integer id, String siteName, String alamat, String provinsi, String kabupaten, String kecamatan, String longitude, String latitude, String userName, String userId, String projectId, String password, String isOnline) {
        this.id = id;
        this.siteName = siteName;
        this.alamat = alamat;
        this.provinsi = provinsi;
        this.kabupaten = kabupaten;
        this.kecamatan = kecamatan;
        this.longitude = longitude;
        this.latitude = latitude;
        this.userName = userName;
        this.userId = userId;
        this.projectId = projectId;
        this.password = password;
        this.isOnline = isOnline;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public String toString() {
        return "postDataActual:{" +
                "siteName='" + siteName + '\'' +
                ", alamat='" + alamat + '\'' +
                ", provinsi='" + provinsi + '\'' +
                ", kabupaten='" + kabupaten + '\'' +
                ", kecamatan='" + kecamatan + '\'' +
                ", longitude=" + longitude + '\'' +
                ", latitude=" + latitude + '\'' +
                ", userName=" + userName + '\'' +
                ", userId=" + userId + '\'' +
                ", projectId=" + projectId + '\'' +
                ", password=" + password +'\'' +
                ", isOnline=" + isOnline +
                '}';
    }
}

