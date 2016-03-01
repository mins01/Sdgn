package com.mins01.sdgn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * First Activity
 */
public class SdgnListsActivity extends AppCompatActivity {
    static {
        com.android.volley.VolleyLog.DEBUG = true;
    }
    //private ArrayAdapter<String>  m_Adapter;
    private GridRowsAdapter m_Adapter;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000; //5초

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate", "START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdgn_lists);
        firstAction();
        MySingleton.getInstance(this).start();
        Log.i("onCreate", "END");
    }
    @Override
    public void onStart() {
        Log.i("onStart", "START");
        super.onStart();
        //initUI();
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
        final SdgnListsActivity thisC = this;
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

        if(m_Adapter==null) {
            m_Adapter = new GridRowsAdapter();

            GridView gridView = (GridView) this.findViewById(R.id.detail_gridView);
            gridView.setAdapter(m_Adapter);
        }
        //리로드 버튼
        Button main_btn_reload = (Button) findViewById(R.id.main_btn_reload);
        main_btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLoad();
            }
        });
    }
    private void firstLoad(){
        Log.i("firstLoad", "START");
        Toast.makeText(getApplicationContext(),"데이터 로딩",Toast.LENGTH_SHORT).show();
        String url1 = "http://www.mins01.com/sdgn/json/units";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    m_Adapter.clear();
                    JSONArray su_rows = response.getJSONArray("su_rows");
                    for(int i=0,m= su_rows.length();i<m;i++){
                        m_Adapter.add((JSONObject) su_rows.get(i));
                    }
                    Toast.makeText(getApplicationContext(),"데이터 로드 완료 : "+m_Adapter.getCount() ,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {

                    e.printStackTrace();
                }

                m_Adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"데이터 로드 에러 : " ,Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        Log.i("firstLoad", "END");
    }



    private void firstAction(){
        (new AsyncTask<SdgnListsActivity, Void, SdgnListsActivity>(){
            @Override
            protected SdgnListsActivity doInBackground(SdgnListsActivity... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(SdgnListsActivity result) {
                //super.onPostExecute(result);
                result.initUI();
                result.firstLoad();
            }

        }).execute(this);
    }

}
