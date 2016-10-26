package com.robin.androidnet.task;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.RemoteViews;

import com.robin.androidnet.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/10/17.
 */
public class DownloadTask extends AsyncTask<String,Integer,Boolean> {
    CallBack callBack;
//    ProgressDialog dialog;
    Context context;
    NotificationManager manager;
    private static final String TAG = "DownloadTask";
    private Notification.Builder builder;
    private RemoteViews views;

    public DownloadTask(Context context) {
        this.callBack = callBack;
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        dialog=new ProgressDialog(context);
//        dialog.setTitle("下载任务");
//        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        dialog.setMessage("下载中");
//        dialog.show();
        Log.e("tag","???");
        manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher);
        views = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        builder.setContent(views);

        manager.notify(1, builder.build());
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.e(TAG, "" + values[0]);
//        dialog.setProgress(values[0]);
        views.setProgressBar(R.id.progressBar,100,values[0],false);
        builder.setContent(views);
        manager.notify(1,builder.build());
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Log.e(TAG,"doinba");
        HttpURLConnection connection=null;
        InputStream is=null;
        FileOutputStream fos=null;
        try {
            URL url=new URL(params[0]);
            connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            int length=connection.getContentLength();
            is=connection.getInputStream();
            File file=new File(Environment.getExternalStorageDirectory(),"myapp.apk");
            fos=new FileOutputStream(file);
            //包装成输出流，如果文件不存在会自动创建
            byte[] buffer=new byte[1024*50];
            int cnt;
            int total=0;
            while (((cnt=is.read(buffer))!=-1)){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                total+=cnt;
                publishProgress((int)((total*100)/length));
                fos.write(buffer,0,cnt);
                fos.flush();
            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
                try {
                    if (fos != null)
                    fos.close();
                    if (is != null) {
                        is.close();
                    }
                    if (connection == null) {
                        connection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
        return false;

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
//        dialog.dismiss();
//        manager.cancel(1);
//        callBack.isOK(aBoolean);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "myapp.apk")),
                "application/vnd.android.package-archive");
        PendingIntent pendingIntent=PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        manager.notify(1,builder.build());
    }
    public interface CallBack{
        void isOK(boolean isOK);
    }
}
