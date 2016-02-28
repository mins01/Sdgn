package com.mins01.sdgn;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * LIST용
 */
public class ListRowsAdapter extends BaseAdapter {
    private ArrayList<JSONObject> rows;
    RequestQueue queue;
    public ListRowsAdapter(){
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();
        //queue = MySingleton.getInstance(context).getRequestQueue();

        NetworkImageView list_row_imageView;
        TextView txtview_unit_name;
        TextView txtview_unit_properties;
        TextView txtview_unit_rank;
        ListRowsHolder listRowsHolder;

        JSONObject row = rows.get(position);

        if (convertView == null) {
            Log.i("getView", Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row, parent, false);

            listRowsHolder = new ListRowsHolder();
            listRowsHolder.list_row_imageView = (NetworkImageView) convertView.findViewById(R.id.list_row_imageView);
            listRowsHolder.txtview_unit_name = (TextView) convertView.findViewById(R.id.txtview_unit_name);
            listRowsHolder.txtview_unit_properties = (TextView) convertView.findViewById(R.id.txtview_unit_properties);
            listRowsHolder.txtview_unit_rank = (TextView) convertView.findViewById(R.id.txtview_unit_rank);
            listRowsHolder.imageview_avg_stars =  new ArrayList<>();
            listRowsHolder.imageview_avg_stars.add((ImageView) convertView.findViewById(R.id.imageview_avg_star_0));
            listRowsHolder.imageview_avg_stars.add((ImageView) convertView.findViewById(R.id.imageview_avg_star_1));
            listRowsHolder.imageview_avg_stars.add((ImageView) convertView.findViewById(R.id.imageview_avg_star_2));
            listRowsHolder.imageview_avg_stars.add((ImageView) convertView.findViewById(R.id.imageview_avg_star_3));
            listRowsHolder.imageview_avg_stars.add((ImageView) convertView.findViewById(R.id.imageview_avg_star_4));

            convertView.setTag(listRowsHolder);
        }else{
            listRowsHolder = (ListRowsHolder) convertView.getTag();
        }
        list_row_imageView = listRowsHolder.list_row_imageView;
        txtview_unit_name = listRowsHolder.txtview_unit_name;
        txtview_unit_properties = listRowsHolder.txtview_unit_properties;
        txtview_unit_rank = listRowsHolder.txtview_unit_rank;

        txtview_unit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "클릭");
                Toast.makeText(context, String.format("%02d", position), Toast.LENGTH_SHORT).show();
            }
        });
        //txtview_unit_name.setText(String.format("%02d", position));
        try {
            txtview_unit_name.setText(row.getString("unit_name"));
            txtview_unit_rank.setText(row.getString("unit_rank"));
            int tmp_color=0;
            switch(row.getString("unit_rank")) {
                case "C":
                     tmp_color = ContextCompat.getColor(context,R.color.colorUnitRankC);
                    break;
                case "B": tmp_color = ContextCompat.getColor(context,R.color.colorUnitRankB);break;
                case "A": tmp_color = ContextCompat.getColor(context,R.color.colorUnitRankA);break;
                case "S": tmp_color = ContextCompat.getColor(context,R.color.colorUnitRankS);break;
            }
            txtview_unit_rank.setTextColor(tmp_color);
            boolean[] temp_arr = {false,false,false,false,false};

            switch((int) Math.round(row.getDouble("avg_star"))) {
                case 5: temp_arr[4]=true;
                case 4: temp_arr[3]=true;
                case 3: temp_arr[2]=true;
                case 2: temp_arr[1]=true;
                case 1: temp_arr[0]=true;
                case 0:break;
                default:
                    break;
            }
            for(int i=0,m=temp_arr.length;i<m;i++){
                listRowsHolder.imageview_avg_stars.get(i).setBackgroundResource(temp_arr[i]?android.R.drawable.star_on:android.R.drawable.star_off);
            }


            txtview_unit_properties.setText(row.getString("unit_properties"));
            switch(row.getInt("unit_properties_num")){
                case 1:
                    txtview_unit_properties.setBackgroundResource(R.drawable.bg_unit_type_1);
                    break;
                case 2:
                    txtview_unit_properties.setBackgroundResource(R.drawable.bg_unit_type_2);
                    break;
                case 3:
                    txtview_unit_properties.setBackgroundResource(R.drawable.bg_unit_type_3);
                    break;
                default:
                    txtview_unit_properties.setBackgroundResource(R.drawable.bg_unit_type_1);
                    break;
            }
            list_row_imageView.setImageUrl(row.getString("unit_img"), MySingleton.getInstance(context).getImageLoader());
        } catch (JSONException e) {
            e.printStackTrace();
        }





        return convertView;
    }

    private class ListRowsHolder{
        NetworkImageView list_row_imageView;
        TextView txtview_unit_name;
        TextView txtview_unit_properties;
        TextView txtview_unit_rank;
        ArrayList<ImageView> imageview_avg_stars;
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
