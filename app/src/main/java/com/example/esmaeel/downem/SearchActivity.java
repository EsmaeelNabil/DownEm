package com.example.esmaeel.downem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class SearchActivity extends AppCompatActivity {
    private Button searchButton;
    private EditText searchEd;
    private ListView searchList;
    private RequestQueue queue;
    private ProgressBar DprogressBar;

    private searchListAdapter Adapter;
    private ArrayList<VideoData> videoDataArrayList = new ArrayList<VideoData>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchButton = (Button)findViewById(R.id.getdatabtn);

        searchList = (ListView)findViewById(R.id.search_list);
        Adapter = new searchListAdapter(this, R.layout.my_row_layout, videoDataArrayList);
        searchList.setAdapter(Adapter);

        searchEd = (EditText)findViewById(R.id.searcheditText);
        DprogressBar  = (ProgressBar)findViewById(R.id.searchprogressBar);
        DprogressBar.setVisibility(View.INVISIBLE);
        queue = Volley.newRequestQueue(this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Keyword = searchEd.getText().toString();
                SearchThis(Keyword);
                DprogressBar.setVisibility(View.VISIBLE);

            }
        });

    }

    public void SearchThis(String KeyWord){
        String FirstLink="https://www.googleapis.com/youtube/v3/search?part=snippet&q=";
        String LastLink="&key=AIzaSyDN3XsIXMV_vlHiDk2RMi4q0Ux6kcEg42Y";
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, FirstLink + KeyWord + LastLink
                        , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Adapter.clear();
                            videoDataArrayList.clear();
                            JSONArray array = response.getJSONArray("items");
                            for(int i=0;i<array.length();i++){

                                JSONObject jsonObject = array.getJSONObject(i);

                                JSONObject IdObject = jsonObject.getJSONObject("id");
                                String videoId = IdObject.getString("lable");

                                JSONObject snippetObject = jsonObject.getJSONObject("snippet");
                                String title = snippetObject.getString("id");
                                String description = snippetObject.getString("description");

                                JSONObject thumbnailsObject = snippetObject.getJSONObject("thumbnails");
                                JSONObject DefaultObject = thumbnailsObject.getJSONObject("default");
                                String imageUrl = DefaultObject.getString("url");



                                videoDataArrayList.add(new VideoData(title,videoId,description,imageUrl));
                                Adapter = new searchListAdapter(SearchActivity.this, R.layout.my_row_layout, videoDataArrayList);
                                // display the list.
                                Adapter.notifyDataSetChanged();
                                DprogressBar.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(jsonObjectRequest);

    }
}
