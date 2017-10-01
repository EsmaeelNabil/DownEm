package com.example.esmaeel.downem;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import static java.security.AccessController.getContext;


public class ShowVideoActivity extends YouTubeBaseActivity {
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;

    String titleA,videoIdA,imageUrlA,descriptionA;
    private TextView titletv,descriptiontv;
    private ProgressBar DownloadProgressBar;
    private RequestQueue queue;
    private Button mp3downloadbtn,stopbutton;
    private LayoutInflater inflater;
    private View layout;
    private Toast toast;
    private ListView downloadList;
    private DownloadListAdapter Adapter;
    private ArrayList<DownloadData> DownloadDataArrayList = new ArrayList<DownloadData>();
    Typeface font ;
    String DownloadPath;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);


        font  = Typeface.createFromAsset(getApplication().getAssets(), "title_font.otf");
        titletv = (TextView) findViewById(R.id.titletv);
        descriptiontv = (TextView) findViewById(R.id.descriptiontv);

        GetExtras();

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youTubePlayerView);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoIdA);
//                youTubePlayer.release();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };


        youTubePlayerView.initialize("AIzaSyDN3XsIXMV_vlHiDk2RMi4q0Ux6kcEg42Y",onInitializedListener);



    }

    private void GetExtras() {

        Bundle extras = null;
        try {
            extras = getIntent().getExtras();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String title = extras.getString("title");
        String videoId = extras.getString("videoId");
        String description = extras.getString("description");
        titleA = title ;
        videoIdA = videoId;
        descriptionA= description ;

        //Sart Video
        titletv.setText(titleA);
        titletv.setTypeface(font);
        descriptiontv.setText(descriptionA + "\n");
//        Toast.makeText(getApplicationContext(), title + " \n " + videoId + " \n " + description, Toast.LENGTH_LONG).show();
    }


}
