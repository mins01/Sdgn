package com.mins01.sdgn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    static {
        com.android.volley.VolleyLog.DEBUG = true;
    }
    //private ArrayAdapter<String>  m_Adapter;
    private ListRowsAdapter m_Adapter;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000; //5초


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate", "START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstAction();
        Log.i("onCreate", "END");
    }
    @Override
    public void onStart() {
        Log.i("onStart", "START");
        super.onStart();
        initUI();
        Log.i("onStart", "END");
    }
    @Override
    public void onRestart() {
        MySingleton.getInstance(this).start();
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
        MySingleton.getInstance(this).stop();
    }
    @Override
    public void onDestroy(){
        Log.i("onDestroy", "START");
        super.onDestroy();
        Log.i("onDestroy", "END");
    }
    private void initUI(){
        if(m_Adapter==null) {
            m_Adapter = new ListRowsAdapter();

            ListView main_listView = (ListView) this.findViewById(R.id.main_listView);
            main_listView.setAdapter(m_Adapter);
        }

        Button main_reload_btn = (Button) findViewById(R.id.main_reload_btn);
        main_reload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLoad();
            }
        });
    }
    private void firstLoad(){
        Log.i("firstLoad", "START");
        Toast.makeText(getApplicationContext(),"최초 로드 시작",Toast.LENGTH_SHORT).show();
        String url1 = "http://www.mins01.com/sdgn/json/units";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    m_Adapter.clear();
                    JSONArray su_rows = response.getJSONArray("su_rows");
                    for(int i=0,m= su_rows.length();i<m;i++){
                        m_Adapter.add((JSONObject) su_rows.get(i));
                    }
                    Toast.makeText(getApplicationContext(),"JSON 로드완료 : "+m_Adapter.getCount() ,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {

                    e.printStackTrace();
                }

                m_Adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"JSON 로드에러 : " ,Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        Log.i("firstLoad", "END");
    }



    private void firstAction(){
        (new AsyncTask <MainActivity, Void, MainActivity>(){
             @Override
            protected MainActivity doInBackground(MainActivity... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(MainActivity result) {
                //super.onPostExecute(result);
                result.firstLoad();
            }

        }).execute(this);
    }

}
