package com.robin.androidnet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.robin.androidnet.task.CheckTask;
import com.robin.androidnet.task.DownloadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void doUpdate(View view) {
        Log.e(TAG,"doupdate");
        try {
            new CheckTask(new CheckTask.CallBackListener() {
                @Override
                public void checkUpdate(String s) {
                    Log.e(TAG,"checkupdate");
                    if(s==null){
                        Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        JSONArray array=new JSONArray(s);

                        JSONObject obj= (JSONObject) array.get(0);
                        int verCode=obj.getInt("verCode");
                        int crtCode=getPackageManager().getPackageInfo(getPackageName(),0).versionCode;
                        Log.e(TAG,"checkupdate"+verCode+" "+crtCode);

                        if(verCode>crtCode){

                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("提示")
                                    .setMessage("发现新版本，当前版本为："+crtCode+" 新版本为："+verCode+" 是否开始更新")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateInBackground();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();

                        }else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("提示")
                                    .setMessage("暂没发现新版本")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).execute(new URL("http://192.168.7.244:8080/webdata2/GetVer"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void updateInBackground() {
        new DownloadTask(this).execute("http://192.168.7.244:8080/webdata2/apk/app2.apk");
    }
}
