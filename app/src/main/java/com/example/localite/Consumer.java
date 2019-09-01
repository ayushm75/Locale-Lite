package com.example.localite;

public class Consumer {

    String name;
    String phoneNo;
    String email;
    String addr;
    String pwd;

    public Consumer(){}

    public Consumer(String name, String phoneNo, String email, String addr, String pwd) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.addr = addr;
        this.pwd = pwd;
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
