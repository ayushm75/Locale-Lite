package com.example.localite;

import java.util.Date;

public class MessageClass {

    public static int  count =0;

    String me, promsg;

    String day , time;


    public MessageClass() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getConmsg() {
        return me;
    }

    public void setConmsg(String conmsg) {
        me = conmsg;
    }

    public String getPromsg() {
        return promsg;
    }

    public void setPromsg(String promsg) {
        this.promsg = promsg;
    }
}
