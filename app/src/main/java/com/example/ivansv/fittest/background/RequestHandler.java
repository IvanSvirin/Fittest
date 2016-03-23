package com.example.ivansv.fittest.background;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.os.ResultReceiver;

import com.example.ivansv.fittest.controller.DataResultReceiver;
import com.example.ivansv.fittest.model.DataList;
import com.example.ivansv.fittest.model.Datum;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestHandler extends IntentService {
    public static final String url = "http://www.extremefitness.ru/api/";
    public static final String DATA_LIST = "data_list";
    private ArrayList<Datum> dataList = new ArrayList<>();
    private int count;
    private ResultReceiver resultReceiver;
    public RequestHandler() {
        super("RequestHandler");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getApplicationContext().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        resultReceiver = intent.getParcelableExtra(DataResultReceiver.RECEIVER);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        restInterface.getData().enqueue(new Callback<DataList>() {
            @Override
            public void onResponse(Call<DataList> call, Response<DataList> response) {
                dataList = (ArrayList<Datum>) response.body().getData();
                getImages();
            }

            private void getImages() {
                String subPath;
                count = dataList.size();
                for (Datum data : dataList) {
                    subPath = data.getV() + ".jpg";
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + subPath);
                    if (!file.exists()) {
                        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://i.ytimg.com/vi/" +
                                data.getV() + "/mqdefault.jpg"));
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, subPath);
                        downloadManager.enqueue(request);
                        count--;
                    } else {
                        getApplicationContext().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        sendResult();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataList> call, Throwable t) {
            }
        });
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ((DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) && (count == 0)) {
                sendResult();
            }
        }
    };

    private void sendResult() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(DATA_LIST, dataList);
        resultReceiver.send(DataResultReceiver.RESULT, bundle);
        getApplicationContext().unregisterReceiver(receiver);
    }
}