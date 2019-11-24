package com.example.ncbaicam.cat_alam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class Nav_meeting extends Fragment{
    String stime="";
    String slat="";
    String slng="";
    View v;
    GridView gridView;
    //point 이미지
    ImageView point;
    //텍스트 배열 선언
    ArrayList<String> textArr = new ArrayList<String>();
    WebView webView;
    WebSettings webViewSetting;

    listAdapter listAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v=inflater.inflate(R.layout.nav_meeting, container, false);
        point=v.findViewById(R.id.log_point);
        makeList();
        gridView = (GridView) v.findViewById(R.id.log_list);
        gridView.setAdapter(new listAdapter((MainPage)getActivity(), textArr, point));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //클릭하면 웹 뷰로 넘어감
                /*
                String web="https://www.google.com/maps/@"+slat+" "+slng+"15z";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
                startActivity(intent);
                */
                webView = v.findViewById(R.id.LibWebview); // 웹뷰 id
                webViewSetting = webView.getSettings();             // testWebview를 webViewSetting 에 선언 해준다.
                String userAgent = webViewSetting.getUserAgentString() ;
                //set the UA of the webview to this value:
                webView.getSettings().setUserAgentString(userAgent);

                webViewSetting.setJavaScriptEnabled(true);    // 웹의 자바스크립트 허용
                webView.setWebViewClient(new WebViewClient(){}); // 내부 webview로 열기
                webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                String url="https://www.google.com/maps/@"+slat.split(";")[position]+","+slng.split(";")[position]+",15z?hl=ko";
                Log.d("nav_meeting", "onItemClick: " + url);
                Log.d("nav_meeting", slat.split(";")[position]);

                webView.loadUrl(url);  // Load할 url 주소

            }
        });
        return v;
    }
    //여기서 string 해체해서 row 만들기
    public void makeList() {
        //분해해서 각자
        String[] atime = stime.split(";");
        String[] alat = slat.split(";");
        String[] alng = slng.split(";");
        String blank="        ";
        //문자열 만들어서 리스트에 넣기
        for (int i=0 ; i<atime.length ; i++)
        {
            textArr.add(atime[i] + blank +  "[ "+alat[i]  + " °N" + blank + alng[i] + " ° E ]" );
        }

    }

}
