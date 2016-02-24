package com.mins01.sdgn;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mins01 on 2016-02-22.
 */
public class ListRowsAdapter extends BaseAdapter {
    private ArrayList<JSONObject> rows;
    RequestQueue queue;
    public ListRowsAdapter(){
        rows = new ArrayList<JSONObject>();
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Object getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();
        //queue = MySingleton.getInstance(context).getRequestQueue();

        NetworkImageView list_row_imageView;
        TextView list_row_textView;
        ListRowsHolder listRowsHolder;
        JSONObject row = rows.get(position);

        if (convertView == null) {
            Log.i("getView", Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            list_row_imageView = (NetworkImageView) convertView.findViewById(R.id.list_row_imageView);
            list_row_textView = (TextView) convertView.findViewById(R.id.list_row_textView);
            listRowsHolder = new ListRowsHolder();
            listRowsHolder.list_row_imageView = list_row_imageView;
            listRowsHolder.list_row_textView = list_row_textView;
            convertView.setTag(listRowsHolder);
        }else{
            listRowsHolder = (ListRowsHolder) convertView.getTag();
            list_row_imageView = listRowsHolder.list_row_imageView;
            list_row_textView = listRowsHolder.list_row_textView;
        }
        list_row_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "클릭");
                Toast.makeText(context, String.format("%02d", position), Toast.LENGTH_SHORT).show();
            }
        });
        list_row_textView.setText(String.format("%02d", position));
        String unit_img = null;
        String unit_name = null;
        try {
            unit_img = row.getString("unit_img");
            unit_name = row.getString("unit_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        list_row_textView.setText(unit_name);

        list_row_imageView.setImageUrl(unit_img, MySingleton.getInstance(context).getImageLoader());


        return convertView;
    }

    private class ListRowsHolder{
        NetworkImageView list_row_imageView;
        TextView list_row_textView;
    }

    public void add(JSONObject jsonObject){
        rows.add(jsonObject);
    }
    public void remove(int position){
        rows.remove(position);
    }
    public void clear(){
        rows.clear();
    }
}
