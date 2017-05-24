package com.example.esmaeel.downem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

public class DownloadListAdapter extends ArrayAdapter<DownloadData> {
    // declaring our ArrayList of items

    public ArrayList<DownloadData> objects;


    public DownloadListAdapter (Context context, int textViewResourceId, ArrayList<DownloadData> objects)
    {
        super(context,textViewResourceId, objects);
        this.objects = objects;
    }

    static class ViewHolder {
        public TextView text,text2,text3,text4;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.my_row_layout2, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) v.findViewById(R.id.txUsernametx);
            viewHolder.text2 = (TextView) v.findViewById(R.id.txUsername);
            viewHolder.text3 = (TextView) v.findViewById(R.id.txGendertx);
            viewHolder.text4 = (TextView) v.findViewById(R.id.txGender);
            v.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) v.getTag();
        DownloadData i = objects.get(position);
        holder.text.setText("id : ");
        holder.text2.setText(i.getId());
        holder.text3.setText("lable : ");
        holder.text4.setText(i.getLable());

        DownloadListAdapter.this.notifyDataSetChanged();

        // the view must be returned to our activity
        return v;

    }
}