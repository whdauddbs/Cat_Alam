package com.example.ncbaicam.cat_alam;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class listAdapter extends BaseAdapter {
    private Activity mContext; //Mainpage
    private ArrayList<String> textArr;
    private ImageView point;
    LayoutInflater inf;

    //초기화
    public listAdapter(Activity mContext, ArrayList<String> textArr, ImageView point) {
        super();
        this.mContext = mContext;
        inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.textArr=textArr;
        this.point= point;
        Log.d("listAdapter----------------init", String.valueOf(textArr));
        Log.d("listAdapter", String.valueOf(mContext));
        /*D/listAdapter: [20190108   41.40338   37.757687, 20190908   2.17403   128.873749, 20201225   41.40338   128.873749]
        com.example.ncbaicam.cat_alam.MainPage@f45fdce*/
    }


    @Override
    public int getCount() {
        return textArr.size();
    }

    @Override
    public Object getItem(int position) {
        return textArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO: 2019-11-22 서버랑 테스트
        //ImageView v;
        if(convertView == null) {
            //v = new ImageView(mContext);
            //v.setLayoutParams(new GridView.LayoutParams(textArr, point));
            convertView = inf.inflate(R.layout.log_layout, null);
            Log.d("dd", "convertView!");
            Log.d("dd", String.valueOf(convertView));
            //D/dd: convertView!
        }
        convertView = inf.inflate(R.layout.log_layout, null);

        ImageView imageView =  convertView.findViewById(R.id.log_point);
        TextView textView = convertView.findViewById(R.id.log_text);

        // TODO: 2019-11-22 제목이 안나옴.
        if(textView== null){
            Log.d("dd", "TextView is null ");
        }
        //뷰에 세팅
        imageView.setImageResource(R.drawable.point);
        textView.setText(textArr.get(position));
        Log.d("listadapter", String.valueOf(textArr.get(position)));
        Log.d("listadapter", String.valueOf(textView));
        //D/listadapter: android.support.v7.widget.AppCompatTextView{cdc9851 V.ED..... ......ID 0,0-0,0 #7f08006b app:id/log_text}

        return convertView;
    }
}
