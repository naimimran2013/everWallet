package com.everwallet.everwalletapp;

public class RUser {
    public String uemail;
    public String uname;
    public String pass;


    public RUser(String uemail, String uname, String pass ) {
        this.uemail = uemail;
        this.uname = uname;
        this.pass = pass;
    }

    public RUser() {

    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}