package com.miguel_lm.appjsondata.modelo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "User")
public class User implements Serializable {

    @PrimaryKey
    @NonNull
    private int id;

    @ColumnInfo(name="name")
    private String name;

    @ColumnInfo(name="nickname")
    private String nickname;

    @ColumnInfo(name="email")
    private String email;

    @ColumnInfo(name="phone")
    private String phone;

    @ColumnInfo(name="company")
    private String company;

    @NonNull
    public int getId() {
        return id;
    }

    public User(int id, String name, String nickname, String email, String phone, String company) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.company = company;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
