package com.example.ivansv.fittest.view;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.ivansv.fittest.R;
import com.example.ivansv.fittest.background.RequestHandler;
import com.example.ivansv.fittest.controller.DataResultReceiver;
import com.example.ivansv.fittest.model.Datum;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataResultReceiver.Receiver,
        ListFragment.OnListFragmentInteractionListener {
    public static ArrayList<Datum> datums;
    private ListFragment listFragment = new ListFragment();
    private Button downloadButton;
    private AlertDialog alertDialog;
    private DataResultReceiver dataResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadButton = (Button) findViewById(R.id.downloadButton);
        if (downloadButton != null && datums != null) {
            downloadButton.setVisibility(View.INVISIBLE);
        }
    }
    public void download(View view) {
        downloadButton.setVisibility(View.INVISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        dataResultReceiver = new DataResultReceiver(new Handler());
        dataResultReceiver.setReceiver(this);
        Intent loadIntent = new Intent(this, RequestHandler.class);
        loadIntent.putExtra(DataResultReceiver.RECEIVER, dataResultReceiver);
        startService(loadIntent);

        ContextThemeWrapper wrapper = new ContextThemeWrapper(this, android.R.style.Theme_Holo);
        AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
                .create();
        alertDialog = builder.show();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        alertDialog.cancel();
        datums = data.getParcelableArrayList(RequestHandler.DATA_LIST);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainContainer, listFragment)
                .commit();

        dataResultReceiver.setReceiver(null);
    }

    @Override
    public void onListFragmentInteraction(Datum datum) {
        String videoId = datum.getV();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        intent.putExtra("VIDEO_ID", videoId);
        startActivity(intent);
    }
}
