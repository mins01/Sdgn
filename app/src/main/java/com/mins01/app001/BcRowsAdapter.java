package com.mins01.app001;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 한마디용
 */
public class BcRowsAdapter extends BaseAdapter {
    private ArrayList<JSONObject> rows;
    RequestQueue queue;

    public BcRowsAdapter() {
        rows = new ArrayList<>();
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

    public void onclick(Context context, JSONObject row) {
//        try {
//            //Toast.makeText(context,"onclick 동작:"+row.getString("unit_name"),Toast.LENGTH_SHORT).show();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Intent intent = new Intent(context, SdgnDetailActivity.class);
        intent.putExtra("row_jsonString", row.toString());
        context.startActivity(intent);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Log.i("getView", String.valueOf(position));
        final Context context = parent.getContext();
        ViewHolder holder;

        final JSONObject row = rows.get(position);
        if (convertView == null) {

            Log.i("getView", Context.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bc_row_sdgn_detail_comment, parent, false);

            holder.textView_bc_name = (TextView) convertView.findViewById(R.id.textView_bc_name);
            holder.textView_bc_insert_date = (TextView) convertView.findViewById(R.id.textView_bc_insert_date);
            holder.textView_bc_comment = (TextView) convertView.findViewById(R.id.textView_bc_comment);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try{
            holder.textView_bc_name.setText(row.getString("bc_name"));
            holder.textView_bc_insert_date.setText(row.getString("bc_insert_date"));
            holder.textView_bc_comment.setText(row.getString("bc_comment"));
        }catch (Exception e){
            e.printStackTrace();
        }


        return convertView;
    }


    public void add(JSONObject jsonObject) {
        rows.add(jsonObject);
    }

    public void remove(int position) {
        rows.remove(position);
    }

    public void clear() {
        rows.clear();
    }

    class ViewHolder{
        TextView textView_bc_name;
        TextView textView_bc_insert_date;
        TextView textView_bc_comment;


    }
}
