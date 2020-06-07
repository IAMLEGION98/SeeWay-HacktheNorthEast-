package com.example.anupam.logix1;

import android.app.Application;

public class GlobalVariables extends Application {

    public int wentin=0;
    public String role ="driver";
    public String email;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public GlobalVariables()
    {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getWentin() {
        return wentin;
    }

    public void setWentin(int wentin) {
        this.wentin = wentin;
    }
}

