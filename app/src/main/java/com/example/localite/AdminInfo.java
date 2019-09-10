package com.example.localite;

public class AdminInfo {

    String uUsername , uPwd;
    double lat , lon;

    public AdminInfo() {}

    public AdminInfo(String uUsername, String uPwd, double lat, double lon) {
        this.uUsername = uUsername;
        this.uPwd = uPwd;
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getuUsername() {
        return uUsername;
    }

    public void setuUsername(String uUsername) {
        this.uUsername = uUsername;
    }

    public String getuPwd() {
        return uPwd;
    }

    public void setuPwd(String uPwd) {
        this.uPwd = uPwd;
    }
}
