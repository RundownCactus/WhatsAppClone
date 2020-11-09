package com.salmanqureshi.i170282_170019;

import android.graphics.Bitmap;

public class MyAppContact {
    private String name;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public MyAppContact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
