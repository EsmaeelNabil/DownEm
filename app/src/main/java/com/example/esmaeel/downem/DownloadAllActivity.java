package com.example.esmaeel.downem;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DownloadAllActivity extends AppCompatActivity {
    String titleA,videoIdA,descriptionA;
    private TextView titletv,progresstv;
    private ProgressBar DownloadProgressBar;
    private RequestQueue queue;
    private Button mp3downloadbtn,stopbutton,playbtn;
    private LayoutInflater inflater;
    private LinearLayout downloadview;
    private View layout;
    private Toast toast;
    private ListView downloadList;
    private DownloadListAdapter Adapter;
    private ArrayList<DownloadData> DownloadDataArrayList = new ArrayList<DownloadData>();
    Typeface font ;
    String DownloadPath;
    ProgressDialog progressDialog;
    int n = 0 ;
    private DownloadManager downloadManager;
    long reference;

    String JsoupUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_all);

        AndroidNetworking.initialize(getApplicationContext());
        GetExtras();

        font  = Typeface.createFromAsset(getApplication().getAssets(), "title_font.otf");
        progressDialog = new ProgressDialog(this);
        DownloadProgressBar = (ProgressBar)findViewById(R.id.DownloadProgressbar);
        queue = Volley.newRequestQueue(this);
        downloadList = (ListView) findViewById(R.id.download_all_search_list);
        mp3downloadbtn = (Button) findViewById(R.id.mp3button);
        playbtn = (Button) findViewById(R.id.playbtn);
        stopbutton = (Button) findViewById(R.id.stopbuttonall);
        downloadview = (LinearLayout)findViewById(R.id.downloadview);
        downloadview.setVisibility(View.GONE);

        font  = Typeface.createFromAsset(getApplication().getAssets(), "title_font.otf");
        titletv = (TextView) findViewById(R.id.downloadalltitle);
        titletv.setText(titleA);
        progresstv = (TextView) findViewById(R.id.progresstv);

        Adapter = new DownloadListAdapter(this, R.layout.my_row_layout2, DownloadDataArrayList);
        downloadList.setAdapter(Adapter);
        GetDownloadUrls(videoIdA);

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DownloadAllActivity.this, ShowVideoActivity.class);
                i.putExtra("videoId", videoIdA);
                i.putExtra("title", titleA);
                i.putExtra("description", descriptionA);
                startActivityForResult(i,1);
            }
        });


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
                                downloadview.setVisibility(View.VISIBLE);
                                DownloadProgressBar.setMax(((int) totalBytes));
                                DownloadProgressBar.setProgress(((int) bytesDownloaded));
                                progresstv.setText(size((int)bytesDownloaded));
                            }
                        }).startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        downloadview.setVisibility(View.GONE);
                        ShowThisToast("Download Completed");
                        ShowThisToast("You will find the Video in (downloads DownEm App) Folder");

                    }
                    @Override
                    public void onError(ANError anError) {
                        downloadview.setVisibility(View.GONE);
                    }
                });


            }
        });

        mp3downloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AndroidNetworking.cancelAll();
//                DownloadMp3File(videoIdA);

                new getMp3().execute();

            }
        });
        stopbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    AndroidNetworking.cancelAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


    public void DownloadNew(String videoid){
        String url = "http://youtube-mp3.to/convert.php?file=mp3&quality=max&apiEntry="+videoid;
        try {

            org.jsoup.nodes.Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");
            String ss = links.get(1).absUrl("href");
            ShowThisToast(ss);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private class getMp3 extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String url = "http://youtube-mp3.to/convert.php?file=mp3&quality=max&apiEntry="+videoIdA;
                org.jsoup.nodes.Document document = Jsoup.connect(url).get();
                Elements links = document.select("a[href]");
                 JsoupUrl = links.get(1).absUrl("href");

                DownloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
                AndroidNetworking.download(JsoupUrl,DownloadPath,titleA.concat(".mp3"))
                        .setTag("Mp3Download")
                        .setPriority(Priority.HIGH)
                        .build()
                        .setDownloadProgressListener(new DownloadProgressListener() {
                            @Override
                            public void onProgress(long bytesDownloaded, long totalBytes) {
                                downloadview.setVisibility(View.VISIBLE);
                                DownloadProgressBar.setMax(((int) totalBytes));
                                DownloadProgressBar.setProgress(((int) bytesDownloaded));
                                progresstv.setText(size((int)bytesDownloaded));
                            }
                        })
                        .startDownload(new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                File Music = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                                File mp3 = new File(Music,titleA.concat(".mp3"));
                                if (mp3.exists()){
                                    if (mp3.length()<100000){

                                        downloadview.setVisibility(View.GONE);
                                        ShowThisToast("Error in Youtube file! ");

                                    }else {
                                        downloadview.setVisibility(View.GONE);
                                        ShowThisToast("Download Completed Your File is in Music");
                                    }
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                downloadview.setVisibility(View.GONE);
                            }
                        });


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        ShowThisToast("Download Started");
        }
    }


    public void DownloadMp3File(String videoid){
        String FirstLink="https://www.convertmp3.io/fetch/?video=https://www.youtube.com/watch?v=";

        String downUrl = FirstLink+videoid ;
        DownloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
        AndroidNetworking.download(downUrl,DownloadPath,titleA.concat(".mp3"))
                .setTag("Mp3Download")
                .setPriority(Priority.HIGH)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        downloadview.setVisibility(View.VISIBLE);
                        DownloadProgressBar.setMax(((int) totalBytes));
                        DownloadProgressBar.setProgress(((int) bytesDownloaded));
                        progresstv.setText(size((int)bytesDownloaded));
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        File Music = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                        File mp3 = new File(Music,titleA.concat(".mp3"));
                        if (mp3.exists()){
                            if (mp3.length()<100000){

                                downloadview.setVisibility(View.GONE);
                                ShowThisToast("Error in Youtube file! ");

                            }else {
                                downloadview.setVisibility(View.GONE);
                                ShowThisToast("Download Completed Your File is in Music");
                            }
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        downloadview.setVisibility(View.GONE);
                    }
                });

    }

    public void getMp3File(String url){


    }
    public void GetDownloadUrls(String videoId){
        progressDialog.setMessage("Just a second ...");
        progressDialog.show();
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
                                DownloadDataArrayList.add(new DownloadData(id,label));
                                Adapter = new DownloadListAdapter(DownloadAllActivity.this, R.layout.my_row_layout2, DownloadDataArrayList);
                                // display the list.
                                Adapter.notifyDataSetChanged();
                                downloadList.invalidateViews();
                                progressDialog.cancel();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.cancel();
                            ShowThisToast("J : Sorry Try Again! ");
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ShowThisToast("V : Sorry Try Again!");
                        progressDialog.cancel();
                    }
                });

        jsonObjectRequest.setShouldCache(false);
        queue.cancelAll(true);
        queue.add(jsonObjectRequest);

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

    }

    public String size(int size){
        String hrSize = "";
        double m = size/1024.0/1024.0;
        DecimalFormat dec = new DecimalFormat("0.00");

        if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else {
            hrSize = dec.format(size).concat(" KB");
        }
        return hrSize;
    }


}
