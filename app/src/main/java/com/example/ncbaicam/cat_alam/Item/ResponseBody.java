package com.example.ncbaicam.cat_alam.Item;

import java.util.HashMap;

public class ResponseBody {
    public double distance;
    public String status;
    public int ringing_cnt;


    public ResponseBody(HashMap<String, Object> parameters){
        this.distance = (double) parameters.get("distance");
        this.status = (String) parameters.get("status");
        this.ringing_cnt = (int) parameters.get("ringing_cnt");

    }
}
