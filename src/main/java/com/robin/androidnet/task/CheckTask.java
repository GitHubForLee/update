package com.robin.androidnet.task;

import android.os.AsyncTask;

import com.robin.androidnet.utils.HttpUtils;

import java.net.URL;

/**
 * Created by Administrator on 2016/10/17.
 */
public class CheckTask extends AsyncTask<URL,Void,byte[]> {
    private CheckTask.CallBackListener callBack;

    public CheckTask(CallBackListener callBack) {
        this.callBack = callBack;
    }

    @Override
    protected byte[] doInBackground(URL... params) {
        return HttpUtils.getWebData(params[0]);
    }
    @Override
    protected void onPostExecute(byte[] s) {
        super.onPostExecute(s);
        //
        if(s!=null){
            callBack.checkUpdate(new String(s));
        }else {
            callBack.checkUpdate(null);
        }
    }
    public interface CallBackListener{
        void checkUpdate(String s);
    }
}
