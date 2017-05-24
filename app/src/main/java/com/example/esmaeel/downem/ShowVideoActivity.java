package com.example.esmaeel.downem;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
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


public class ShowVideoActivity extends YouTubeBaseActivity {
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    String titleA,videoIdA,imageUrlA,descriptionA;
    private TextView titletv,descriptiontv;
    private ProgressBar DownloadProgressBar;
    private RequestQueue queue;

    private Button mp3downloadbtn,stopbutton;
    private DownloadManager downloadManager;
    private LayoutInflater inflater;
    private View layout;
    private Toast toast;

    private ListView downloadList;
    private DownloadListAdapter Adapter;
    private ArrayList<DownloadData> DownloadDataArrayList = new ArrayList<DownloadData>();

    long reference;
    String DownloadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);

        AndroidNetworking.initialize(getApplicationContext());
        DownloadProgressBar = (ProgressBar)findViewById(R.id.DownloadProgressbar);
        DownloadProgressBar.setVisibility(View.INVISIBLE);
        queue = Volley.newRequestQueue(this);
        downloadList = (ListView) findViewById(R.id.search_list3);
        mp3downloadbtn = (Button) findViewById(R.id.mp3button);
        stopbutton = (Button) findViewById(R.id.Stopbutton);
        stopbutton.setVisibility(View.INVISIBLE);


        titletv = (TextView) findViewById(R.id.titletv);
        descriptiontv = (TextView) findViewById(R.id.descriptiontv);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youTubePlayerView);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoIdA);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        GetExtras();
        youTubePlayerView.initialize("AIzaSyDN3XsIXMV_vlHiDk2RMi4q0Ux6kcEg42Y",onInitializedListener);
        Adapter = new DownloadListAdapter(this, R.layout.my_row_layout2, DownloadDataArrayList);
        downloadList.setAdapter(Adapter);

        GetDownloadUrls(videoIdA);
        downloadList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String DownloadUrl= ((TextView) view.findViewById(R.id.txUsername)).getText().toString();

                DownloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                AndroidNetworking.download(DownloadUrl,DownloadPath+"s DownEm App",titleA.concat(".mp4"))
                        .setTag("VideoDownload")
                        .setPriority(Priority.HIGH)
                        .build()
                        .setDownloadProgressListener(new DownloadProgressListener() {
                            @Override
                            public void onProgress(long bytesDownloaded, long totalBytes) {
                                stopbutton.setVisibility(View.VISIBLE);
                                DownloadProgressBar.setVisibility(View.VISIBLE);
                                DownloadProgressBar.setMax(((int) totalBytes));
                                DownloadProgressBar.setProgress(((int) bytesDownloaded));
                            }
                        }).startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        stopbutton.setVisibility(View.INVISIBLE);
                        ShowThisToast("Download Completed");
                        DownloadProgressBar.setVisibility(View.INVISIBLE);
                        ShowThisToast("You will find the Video in (downloads DownEm App) Folder");

                    }
                    @Override
                    public void onError(ANError anError) {
                        stopbutton.setVisibility(View.INVISIBLE);
                        DownloadProgressBar.setVisibility(View.INVISIBLE);
                    }
                });


            }
        });

        mp3downloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadMp3File(videoIdA);

            }
        });
        stopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidNetworking.cancel("VideoDownload");
            }
        });

    }

    private void ShowThisToast(String ToastMessage) {
        //---------------------- Custome Toast ----------
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(ToastMessage);

        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
        //-----------------------------------------------
    }
    public void DownloadMp3File(String videoid){
        String FirstLink="https://www.youtubeinmp3.com/fetch/?video=https://www.youtube.com/watch?v=";
        downloadManager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(FirstLink+videoid);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        long reference = downloadManager.enqueue(request);
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
        descriptiontv.setText(descriptionA + "\n");
//        Toast.makeText(getApplicationContext(), title + " \n " + videoId + " \n " + description, Toast.LENGTH_LONG).show();
    }
    public void GetDownloadUrls(String videoId){
        String FirstLink="https://www.saveitoffline.com/process/?url=https://www.youtube.com/watch?v=";
        String LastLink="&type=json";
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, FirstLink + videoId + LastLink
                        , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray array = response.getJSONArray("urls");

                            for(int i=0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String label = jsonObject.getString("label");
//                                Toast.makeText(getApplicationContext(),label + id,Toast.LENGTH_LONG).show();
                                DownloadDataArrayList.add(new DownloadData(id,label));
                                Adapter = new DownloadListAdapter(ShowVideoActivity.this, R.layout.my_row_layout2, DownloadDataArrayList);
                                // display the list.
                                Adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        jsonObjectRequest.setShouldCache(false);
        queue.add(jsonObjectRequest);

    }

}
