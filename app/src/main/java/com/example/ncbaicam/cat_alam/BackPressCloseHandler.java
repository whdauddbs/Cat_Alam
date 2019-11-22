package com.example.ncbaicam.cat_alam;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;
    private Fragment fragment;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        // TODO: 2019-11-22 제어권?이 엑티비티에 있음 뒤로가기 수정.
        if(fragment != null) {
            Log.d("backpress -F", String.valueOf(fragment));
        }
        if(activity!= null) {
            Log.d("backpress -A", String.valueOf(activity));
        }

        if(fragment != null) {
            FragmentTransaction FragTran = fragment.getFragmentManager().beginTransaction();
            FragTran.remove(fragment).commit();
            fragment = null;
            return;
        }
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }

    }

    public void showGuide() {
        toast = Toast.makeText(activity,
                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setFragment(Fragment fragment){
        this.fragment = fragment;
    }
}
