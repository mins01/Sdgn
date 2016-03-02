package com.mins01.app001;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class SdgnDetailActivity extends AppCompatActivity {
    private JSONObject row;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate", "START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdgn_detail);

        try {
            row = new JSONObject(getIntent().getStringExtra("row_jsonString"));
            this.setTitle(row.getString("unit_name")+" - 유닛 상세");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        firstAction();
        Log.i("onCreate", "END");
    }
    @Override
    public void onStart() {
        Log.i("onStart", "START");
        super.onStart();
        Log.i("onStart", "END");
    }
    @Override
    public void onRestart() {
        //MySingleton.getInstance(this).start();
        Log.i("onRestart", "START");
        super.onRestart();

        Log.i("onRestart", "END");
    }
    @Override
    public void onResume(){
        Log.i("onResume", "START");
        super.onResume();
        Log.i("onResume", "END");
    }
    @Override
    public void onPause(){
        Log.i("onPause", "START");
        super.onPause();
        Log.i("onPause", "END");
    }
    @Override
    protected void onStop(){
        super.onStop();

    }
    @Override
    public void onDestroy(){
        Log.i("onDestroy", "START");
        super.onDestroy();
        //MySingleton.getInstance(this).stop();
        Log.i("onDestroy", "END");
    }
    private void initUI(){
        final SdgnDetailActivity thisC = this;
        final Context context = this.getApplicationContext();
        UnitCardHolder unitCardHolder = new UnitCardHolder();
        View convertView = this.findViewById(R.id.activity_sdgn_detail_top);
        unitCardHolder.setMemberVar(convertView);
        unitCardHolder.setValues(row,convertView);
        try{
            //-- 상단 부분
            NetworkImageView imageView_unit_anime_img =(NetworkImageView) this.findViewById(R.id.imageView_unit_anime_img);
            imageView_unit_anime_img.setImageUrl(row.getString("unit_anime_img"), MySingleton.getInstance(context).getImageLoader());
            ((TextView) this.findViewById(R.id.textView_unit_anime)).setText(row.getString("unit_anime"));
            ((TextView) this.findViewById(R.id.textView_unit_txt)).setText(row.getString("unit_txt"));


            //-- 내용 부분
            String tmp;
            LinearLayout imgs,texts;

            imgs = (LinearLayout)this.findViewById(R.id.group_unit_weapon_img);
            texts = (LinearLayout)this.findViewById(R.id.group_unit_weapon);
            for(int i=0,m=3;i<m;i++){
                tmp = row.getString("unit_weapon"+(i+1)+"_img");
                if(tmp!=null && tmp.length()>1){
                    ((NetworkImageView)imgs.getChildAt(i)).setImageUrl(tmp, MySingleton.getInstance(context).getImageLoader());
                    ((TextView)texts.getChildAt(i)).setText(row.getString("unit_weapon" + (i + 1)));
                }else{
                    ((NetworkImageView)imgs.getChildAt(i)).setImageResource(android.R.color.transparent);//초기화
                    ((TextView)texts.getChildAt(i)).setText("");
                }
            }
             imgs = (LinearLayout)this.findViewById(R.id.group_unit_skill_img);
             texts = (LinearLayout)this.findViewById(R.id.group_unit_skill);
            for(int i=0,m=3;i<m;i++){
                tmp = row.getString("unit_skill"+(i+1)+"_img");
                if(tmp!=null && tmp.length()>1){
                    ((NetworkImageView)imgs.getChildAt(i)).setImageUrl(tmp, MySingleton.getInstance(context).getImageLoader());
                    ((TextView)texts.getChildAt(i)).setText( "["+row.getString("unit_skill"+(i+1))+"]\n"+row.getString("unit_skill"+(i+1)+"_desc"));
                }else{
                    ((NetworkImageView)imgs.getChildAt(i)).setImageResource(android.R.color.transparent);//초기화
                    ((TextView)texts.getChildAt(i)).setText("");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void firstLoad(){
        initUI();
    }

    private void firstAction(){
        (new AsyncTask<SdgnDetailActivity, Void, SdgnDetailActivity>(){
            @Override
            protected SdgnDetailActivity doInBackground(SdgnDetailActivity... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(SdgnDetailActivity result) {
                //super.onPostExecute(result);
                //result.firstLoad();
                initUI();
            }

        }).execute(this);
    }

}
