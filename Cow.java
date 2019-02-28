package com.moo.moostockm;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "cow_table")
public class Cow {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String color;
    private String dob;
    private int gender;
    private String birthweight;
    private String imgurl;

    public Cow(String name, String color, String dob, int gender, String birthweight, String imgurl) {
        this.name = name;
        this.color = color;
        this.dob = dob;
        this.gender = gender;
        this.birthweight = birthweight;
        this.imgurl = imgurl;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getDob() {
        return dob;
    }

    public int getGender() {
        return gender;
    }

    public String getBirthweight() {
        return birthweight;
    }

    public String getImgurl() {
        return imgurl;
    }
}

