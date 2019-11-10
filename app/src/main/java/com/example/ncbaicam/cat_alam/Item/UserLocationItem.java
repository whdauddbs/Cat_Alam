package com.example.ncbaicam.cat_alam.Item;

public class UserLocationItem {

    public String phone;
    public double lng; // 위도
    public double lat; // 경도
    public long nextSearchTime; // 다음 탐색 시간

    public  UserLocationItem(String phone, double lng, double lat){
        this.phone = phone;
        this.lng = lng;
        this.lat = lat;
    }
}
