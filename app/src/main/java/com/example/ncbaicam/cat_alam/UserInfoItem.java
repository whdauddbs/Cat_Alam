package com.example.ncbaicam.cat_alam;

import android.support.annotation.NonNull;

public class UserInfoItem {

    public int seq;
    public String phone;
    public String name;
    public String nickname;
    public String youPhone;

    @NonNull
    @Override
    public String toString() {
        return "UserInfo{" +
                "seq" + seq +
                ", phone='" + phone + "'" +
                ", name='" + name + "'" +
                ", nickname='" + nickname + "'" +
                ", youPhone='" + youPhone + "'}";

    }
}
