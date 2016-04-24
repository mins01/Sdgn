package com.mins01.app001;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * UnitCardHolder by mins01 on 2016-03-01.
 */
public class UnitCardHolder {
    NetworkImageView list_row_imageView;
    TextView txtview_unit_name;
    TextView txtview_unit_properties;
    TextView txtview_unit_rank;
    ArrayList<ImageView> imageview_avg_stars = new ArrayList<>();
    TextView textview_unit_is_weapon_change;
    TextView textview_unit_is_transform;

    public void setMemberVar(View convertView) {
        this.list_row_imageView = (NetworkImageView) convertView.findViewById(R.id.list_row_imageView);
        this.txtview_unit_name = (TextView) convertView.findViewById(R.id.txtview_unit_name);
        this.txtview_unit_properties = (TextView) convertView.findViewById(R.id.txtview_unit_properties);
        this.txtview_unit_rank = (TextView) convertView.findViewById(R.id.txtview_unit_rank);
        this.imageview_avg_stars.add((ImageView) convertView.findViewById(R.id.imageview_avg_star_0));
        this.imageview_avg_stars.add((ImageView) convertView.findViewById(R.id.imageview_avg_star_1));
        this.imageview_avg_stars.add((ImageView) convertView.findViewById(R.id.imageview_avg_star_2));
        this.imageview_avg_stars.add((ImageView) convertView.findViewById(R.id.imageview_avg_star_3));
        this.imageview_avg_stars.add((ImageView) convertView.findViewById(R.id.imageview_avg_star_4));

        this.textview_unit_is_weapon_change = (TextView) convertView.findViewById(R.id.textview_unit_is_weapon_change);
        this.textview_unit_is_transform = (TextView) convertView.findViewById(R.id.textview_unit_is_transform);
    }

    public void setValues(JSONObject row, View convertView) {
        final Context context = convertView.getContext();
        try {
            txtview_unit_name.setText(row.getString("unit_name"));
            txtview_unit_rank.setText(row.getString("unit_rank"));
            int tmp_color = 0;
            switch (row.getString("unit_rank")) {
                case "C":
                    tmp_color = ContextCompat.getColor(context, R.color.colorUnitRankC);
                    break;
                case "B":
                    tmp_color = ContextCompat.getColor(context, R.color.colorUnitRankB);
                    break;
                case "A":
                    tmp_color = ContextCompat.getColor(context, R.color.colorUnitRankA);
                    break;
                case "S":
                    tmp_color = ContextCompat.getColor(context, R.color.colorUnitRankS);
                    break;
            }
            txtview_unit_rank.setTextColor(tmp_color);
            boolean[] temp_arr = {false, false, false, false, false};

            switch ((int) Math.round(row.getDouble("avg_star"))) {
                case 5:
                    temp_arr[4] = true;
                case 4:
                    temp_arr[3] = true;
                case 3:
                    temp_arr[2] = true;
                case 2:
                    temp_arr[1] = true;
                case 1:
                    temp_arr[0] = true;
                case 0:
                    break;
                default:
                    break;
            }
            for (int i = 0, m = temp_arr.length; i < m; i++) {
                imageview_avg_stars.get(i).setBackgroundResource(temp_arr[i] ? android.R.drawable.star_on : android.R.drawable.star_off);
            }


            txtview_unit_properties.setText(row.getString("unit_properties"));
            switch (row.getInt("unit_properties_num")) {
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
            if(row.getInt("unit_is_weapon_change")==1){
                this.textview_unit_is_weapon_change.setVisibility(View.VISIBLE);
            }else{
                this.textview_unit_is_weapon_change.setVisibility(View.GONE);
            }
            if(row.getInt("unit_is_transform")==1){
                this.textview_unit_is_transform.setVisibility(View.VISIBLE);
            }else{
                this.textview_unit_is_transform.setVisibility(View.GONE);
            }



            list_row_imageView.setImageUrl(row.getString("unit_img"), MySingleton.getInstance(context).getImageLoader());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public View createView(ViewGroup parent) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.layout_unit_card, parent, false);
    }

}
