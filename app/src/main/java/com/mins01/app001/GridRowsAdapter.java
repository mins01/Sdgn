package com.mins01.app001;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * LIST용
 */
public class GridRowsAdapter extends BaseAdapter {
    private ArrayList<JSONObject> rows;
    RequestQueue queue;

    public GridRowsAdapter() {
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
        Intent intentx = new Intent(context, SdgnDetailTabActivity.class);
        try {
            intentx.putExtra("unit_idx", row.getInt("unit_idx"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        context.startActivity(intentx);

//        Intent intent = new Intent(context, SdgnDetailActivity.class);
//        //intent.putExtra("row_jsonString", row.toString());
//        try {
//            intent.putExtra("unit_idx", row.getInt("unit_idx"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        context.startActivity(intent);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Log.i("getView", String.valueOf(position));
        final Context context = parent.getContext();
        UnitCardHolder unitCardHolder;

        final JSONObject row = rows.get(position);
        if (convertView == null) {

            Log.i("getView", Context.LAYOUT_INFLATER_SERVICE);
            unitCardHolder = new UnitCardHolder();
            convertView = unitCardHolder.createView(parent);
            unitCardHolder.setMemberVar(convertView);

            convertView.setTag(unitCardHolder);
        } else {
            unitCardHolder = (UnitCardHolder) convertView.getTag();
        }
        unitCardHolder.setValues(row, convertView);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick(context, row);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    String unit_idx = row.getString("unit_idx");
                    String str = "http://www.mins01.com/sdgn/units?unit_idx=" + unit_idx;

                    Uri uri = Uri.parse(str);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
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
}
