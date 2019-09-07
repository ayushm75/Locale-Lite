package com.example.localite;

import com.google.android.gms.maps.model.LatLng;

public class Consumer {

    String name;
    String phoneNo;
    String email;
    String addr;
    String pwd;
    LatLng latLng;

    public Consumer(){}

    public Consumer(String name, String phoneNo, String email, String addr, String pwd , double lat , double lon) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.addr = addr;
        this.pwd = pwd;
        this.latLng = new LatLng(lat , lon);
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public String getAddr() {
        return addr;
    }

    public String getPwd() {
        return pwd;
    }
}
