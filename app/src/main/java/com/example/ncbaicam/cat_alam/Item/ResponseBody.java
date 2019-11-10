package com.example.ncbaicam.cat_alam.Item;

import java.util.HashMap;

public class ResponseBody {
    public int nextSearchTime;

    public ResponseBody(HashMap<String, Object> parameters){
        this.nextSearchTime = (int) parameters.get("ok");
    }
}
