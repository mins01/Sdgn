package com.mins01.sdgn;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mins01 on 2016-02-22.
 */
public class ListRowsAdapter extends BaseAdapter {
    private ArrayList<JSONObject> lists;

    public ListRowsAdapter(){
        lists = new ArrayList<JSONObject>();
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        NetworkImageView list_row_imageView;
        TextView list_row_textView;
        ListRowsHolder listRowsHolder;

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
                Log.i("onClick","클릭");
                Toast.makeText(context,String.format("%02d",position),Toast.LENGTH_SHORT).show();
            }
        });
        list_row_textView.setText(String.format("%02d",position));
        return convertView;
    }

    private class ListRowsHolder{
        NetworkImageView list_row_imageView;
        TextView list_row_textView;
    }

    public void add(JSONObject jsonObject){
        lists.add(jsonObject);
    }
    public void remove(int position){
        lists.remove(position);
    }
    public void clear(){
        lists.clear();
    }
}
