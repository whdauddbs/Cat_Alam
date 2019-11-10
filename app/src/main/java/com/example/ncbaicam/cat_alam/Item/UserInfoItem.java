package com.example.ncbaicam.cat_alam.Item;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class UserInfoItem implements Serializable {
    public String phone;
    public String name;
    public String nickname;
    public String youPhone;

    public UserInfoItem(String phone, String name, String nickname, String youPhone){
        this.phone = phone;
        this.name = name;
        this.nickname = nickname;
        this.youPhone = youPhone;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserInfo{" +
                "phone='" + phone + "'" +
                ", name='" + name + "'" +
                ", nickname='" + nickname + "'" +
                ", youPhone='" + youPhone + "'}";

    }
}
