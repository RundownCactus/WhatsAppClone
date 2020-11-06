package com.salmanqureshi.i170282_170019;

import android.graphics.Bitmap;

public class MyAppContact {
    private Bitmap image;
    private String name;
    private String gender;
    private String age;

    public MyAppContact(Bitmap image, String name, String gender, String age) {
        this.image = image;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
