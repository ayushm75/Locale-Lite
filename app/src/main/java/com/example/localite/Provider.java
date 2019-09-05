package com.example.localite;

public class Provider {

    String ownerName;
    String bussType;
    String bussinessName;
    String addr;
    String phone;
    String email;
    String pwd;
    int upvotes;
    int downvotes;
    boolean isAuth;

    public Provider(){};

    public Provider(String ownerName, String bussType, String bussinessName, String addr, String phone, String email, String pwd, int upvotes, int downvotes, boolean isAuth) {
        this.ownerName = ownerName;
        this.bussType = bussType;
        this.bussinessName = bussinessName;
        this.addr = addr;
        this.phone = phone;
        this.email = email;
        this.pwd = pwd;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.isAuth = isAuth;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getBussType() {
        return bussType;
    }

    public String getBussinessName() {
        return bussinessName;
    }

    public String getAddr() {
        return addr;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public boolean isAuth() {
        return isAuth;
    }
}
