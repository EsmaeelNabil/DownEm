package com.example.esmaeel.downem;

import android.widget.ArrayAdapter;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.ArrayList;

public class searchListAdapter extends ArrayAdapter<VideoData> {
    // declaring our ArrayList of items

    private ArrayList<VideoData> objects;
    private ImageLoader imageLoader;

    public searchListAdapter (Context context, int textViewResourceId, ArrayList<VideoData> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.my_row_layout, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */

        VideoData i = objects.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            NetworkImageView mm = (NetworkImageView)v.findViewById(R.id.profilepic);
            TextView tt = (TextView) v.findViewById(R.id.txUsernametx);
            TextView title = (TextView) v.findViewById(R.id.txUsername);
            TextView mt = (TextView) v.findViewById(R.id.txGendertx);
            TextView videoid = (TextView) v.findViewById(R.id.txGender);
            TextView bt = (TextView) v.findViewById(R.id.txStatetx);
            TextView Description = (TextView) v.findViewById(R.id.txState);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (tt != null){
                tt.setText("Title : ");
            }
            if (title != null){
                title.setText(i.getTitle());
            }
            if (mt != null){
                mt.setText("video id : ");
            }
            if (videoid != null){
                videoid.setText(i.getVideoId());
            }
            if (bt != null){
                bt.setText("description: ");
            }
            if (Description != null){
                Description.setText(i.getDescription());
            }

            String url = i.getImageUrl() ;
            imageLoader = CustomVolleyRequest.getInstance(this.getContext())
                    .getImageLoader();
            imageLoader.get(url, ImageLoader.getImageListener(mm,
                    R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round));
            mm.setImageUrl(url, imageLoader);

        }

        searchListAdapter.this.notifyDataSetChanged();

        // the view must be returned to our activity
        return v;

    }
}