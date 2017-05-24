package com.example.esmaeel.downem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoListActivity extends AppCompatActivity {
    private Button playButton;
    private RequestQueue queue;
    private EditText searchEd;
    private ProgressBar progressBar;

    private LayoutInflater inflater;
    private View layout;
    private Toast toast;


    private ListView searchList;
    private searchListAdapter Adapter;
    private ArrayList<VideoData> videoDataArrayList = new ArrayList<VideoData>();
    String IMAGEURL= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
        queue = Volley.newRequestQueue(this);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        searchList = (ListView) findViewById(R.id.search_list2);
        Adapter = new searchListAdapter(this, R.layout.my_row_layout, videoDataArrayList);
        searchList.setAdapter(Adapter);

        searchEd = (EditText) findViewById(R.id.searcheditText2);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String videoId= ((TextView) view.findViewById(R.id.txGender)).getText().toString();
                String title= ((TextView) view.findViewById(R.id.txUsername)).getText().toString();
                String description= ((TextView) view.findViewById(R.id.txState)).getText().toString();
                Intent i = new Intent(VideoListActivity.this, ShowVideoActivity.class);
                i.putExtra("videoId", videoId);
                i.putExtra("title", title);
                i.putExtra("description", description);
                startActivityForResult(i,1);
            }
        });

        playButton = (Button) findViewById(R.id.Play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchEd.getText().toString().trim().replaceAll(" ", "%20");
                SearchThis(keyword);
                searchList.setSelectionAfterHeaderView();
                hideSoftKeyboard(VideoListActivity.this);

            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void SearchThis(String KeyWord){
        progressBar.setVisibility(View.VISIBLE);
        String FirstLink="https://www.googleapis.com/youtube/v3/search?part=snippet&q=";
        String LastLink="&key=AIzaSyDN3XsIXMV_vlHiDk2RMi4q0Ux6kcEg42Y";
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, FirstLink + KeyWord + LastLink
                        , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Adapter.setNotifyOnChange(false);
                            Adapter.clear();
                            videoDataArrayList.clear();
                            JSONArray array = response.getJSONArray("items");
                            for(int i=0;i<array.length();i++){

                                JSONObject jsonObject = array.getJSONObject(i);

                                JSONObject IdObject = jsonObject.getJSONObject("id");
                                String videoId = IdObject.getString("videoId");

                                JSONObject snippetObject = jsonObject.getJSONObject("snippet");
                                String title = snippetObject.getString("title");
                                String description = snippetObject.getString("description");

                                JSONObject thumbnailsObject = snippetObject.getJSONObject("thumbnails");
                                JSONObject DefaultObject = thumbnailsObject.getJSONObject("default");
                                String imageUrl = DefaultObject.getString("url");
                                IMAGEURL  = imageUrl ;

                                videoDataArrayList.add(new VideoData(title,videoId,description,imageUrl));
                                Adapter = new searchListAdapter(VideoListActivity.this, R.layout.my_row_layout, videoDataArrayList);
                                searchList.setSelectionAfterHeaderView();
                                Adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ShowThisToast("can't find any videos, Try another words!");
                            progressBar.setVisibility(View.INVISIBLE);
                            searchList.setSelectionAfterHeaderView();

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
    private void ShowThisToast(String ToastMessage) {
        //---------------------- Custome Toast ----------
        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.toast2,
                (ViewGroup) findViewById(R.id.toast_layout_root2));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(ToastMessage);

        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
        //-----------------------------------------------
    }
}
