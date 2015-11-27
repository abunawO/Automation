package com.chatinitiator.abunaw.automation;

/**
 * Created by Abunaw on 10/29/15.
 */
public class DBEvent{
    String msg;
    String phn_num;
    String str_date;
    String str_time;
    String frq;
    String pend_int;


    public DBEvent(String msg,String phn_num,String str_date,String str_time,String frq,String pend_int)
    {
        this.msg = msg;
        this.phn_num = phn_num;
        this.str_date = str_date;
        this.str_time = str_time;
        this.frq = frq;
        this.pend_int = pend_int;

    }


    public DBEvent()
    {
        this.msg = "";
        this.phn_num = "";
        this.str_date = "";
        this.str_time = "";
        this.frq = "";
        this.pend_int = "";

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStr_date() {
        return str_date;
    }

    public void setStr_date(String str_date) {
        this.str_date = str_date;
    }

    public String getPhn_num() {
        return phn_num;
    }

    public void setPhn_num(String phn_num) {
        this.phn_num = phn_num;
    }

    public String getStr_time() {
        return str_time;
    }

    public void setStr_time(String str_time) {
        this.str_time = str_time;
    }

    public String getFrq() {
        return frq;
    }

    public void setFrq(String frq) {
        this.frq = frq;
    }

    public String getPend_int() {
        return pend_int;
    }

    public void setPend_int(String pend_int) {
        this.pend_int = pend_int;
    }
}
