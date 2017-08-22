package com.example.esmaeel.downem;

import android.graphics.Typeface;
import android.widget.ArrayAdapter;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class searchListAdapter extends ArrayAdapter<VideoData> {
    // declaring our ArrayList of items

    private ArrayList<VideoData> objects;
    Context context;
    public searchListAdapter (Context context, int textViewResourceId, ArrayList<VideoData> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        this.context = context;
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
            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "desc_font.ttf");
            CircleImageView mm = (CircleImageView)v.findViewById(R.id.profilepic);
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
                //here th type face
                title.setTypeface(font);
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
                Description.setTypeface(font);
            }

            String url = i.getImageUrl() ;
            Picasso.with(context).load(url).into(mm);
        }

        searchListAdapter.this.notifyDataSetChanged();

        // the view must be returned to our activity
        return v;

    }
}