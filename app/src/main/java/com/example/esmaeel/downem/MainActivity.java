package com.example.esmaeel.downem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button DButton;
    private TextView StatetextView;
    private EditText LinkEditText;
    private ProgressBar DprogressBar;
    private ListView listView ;
    private ArrayAdapter adapter;
    String[] Urls = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DButton = (Button)findViewById(R.id.getdatabtn);
        StatetextView = (TextView)findViewById(R.id.textView);
        LinkEditText = (EditText)findViewById(R.id.urleditText);
        DprogressBar  = (ProgressBar)findViewById(R.id.progressBar);
        DprogressBar.setVisibility(View.INVISIBLE);
        queue = Volley.newRequestQueue(this);

        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, Urls);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);



        DButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String Link = LinkEditText.getText().toString();
                    DprogressBar.setVisibility(View.VISIBLE);
                    getdata(Link);



            }
        });
    }

    public void getdata(String Link){
        String FirstLink="https://www.saveitoffline.com/process/?url=";
        String LastLink="&type=json";
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, FirstLink + Link + LastLink
                        , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           String title = response.getString("id");
                            JSONArray array = response.getJSONArray("urls");
                            for(int i=0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                String downloadURL = jsonObject.getString("id");
                                String downloadQuality = jsonObject.getString("label");

                                Log.d("Url :",title + "\n"+downloadURL + "\n" + downloadQuality );
                                StatetextView.append(title + "\n"+downloadURL + "\n" + downloadQuality+ "\n"+ "\n");

                                DprogressBar.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(jsonObjectRequest);

    }
}
