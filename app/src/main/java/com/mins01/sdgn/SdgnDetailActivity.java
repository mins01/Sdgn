package com.mins01.sdgn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
            this.setTitle(row.getString("unit_name"));
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //리로드 버튼
        Button main_btn_reload = (Button) findViewById(R.id.main_btn_reload);
        main_btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLoad();
            }
        });
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
            }

        }).execute(this);
    }

}
