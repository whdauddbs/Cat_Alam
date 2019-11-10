package com.example.ncbaicam.cat_alam.remote;

import com.example.ncbaicam.cat_alam.Item.ResponseBody;
import com.example.ncbaicam.cat_alam.Item.UserInfoItem;
import com.example.ncbaicam.cat_alam.Item.UserLocationItem;
import com.example.ncbaicam.cat_alam.UserLocation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RemoteService {

    String BASE_URL = "http://1.224.172.213:13080"; // 서버가 실행되고 있는 IP
    String MEMBER_ICON_URL = BASE_URL + "/user/";

    // 사용자 정보
    @POST ("/user/info")
    Call<String> insertUserInfo(@Body UserInfoItem userInfoItem);

    @POST("/user/location")
    Call<ResponseBody> insertLocation(@Body UserLocationItem userLocationItem);
}
