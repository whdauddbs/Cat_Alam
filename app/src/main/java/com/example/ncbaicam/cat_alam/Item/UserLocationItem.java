package com.example.ncbaicam.cat_alam.Item;

public class UserLocationItem {

    public double lng; // 위도
    public double lat; // 경도
    public String phone;

    public  UserLocationItem(String phone, double lng, double lat){
        this.phone = phone;
        this.lng = lng;
        this.lat = lat;
    }
}
