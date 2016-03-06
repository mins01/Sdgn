package com.mins01.app001;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class DetailActivity extends AppCompatActivity {
    private GridRowsAdapter m_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate", "START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_old);


        //MySingleton.getInstance(this).start();
        Log.i("onCreate", "END");
    }

    @Override
    public void onStart() {
        Log.i("onStart", "START");
        super.onStart();
        initUI();
        loadJson();
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
    public void onResume() {
        Log.i("onResume", "START");
        super.onResume();
        Log.i("onResume", "END");
    }

    @Override
    public void onPause() {
        Log.i("onPause", "START");
        super.onPause();
        Log.i("onPause", "END");
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        Log.i("onDestroy", "START");
        super.onDestroy();
        //MySingleton.getInstance(this).stop();
        Log.i("onDestroy", "END");
    }

    private void initUI() {
        m_Adapter = new GridRowsAdapter();

        GridView gridView = (GridView) this.findViewById(R.id.detail_gridView);
        gridView.setAdapter(m_Adapter);
    }


    private void loadJson() {
        Log.i("firstLoad", "START");
        Toast.makeText(getApplicationContext(), "최초 로드 시작", Toast.LENGTH_SHORT).show();
        String url1 = "http://www.mins01.com/sdgn/json/units";

        m_Adapter.clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray su_rows = response.getJSONArray("su_rows");
                    for (int i = 0, m = su_rows.length(); i < m; i++) {
                        m_Adapter.add((JSONObject) su_rows.get(i));
                    }
                    m_Adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "JSON 로드완료 : " + su_rows.length(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "JSON 로드에러 : ", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        Log.i("firstLoad", "END");
    }
}
