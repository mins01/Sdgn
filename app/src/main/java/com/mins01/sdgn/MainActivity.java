package com.mins01.sdgn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
    //RequestQueue queue = Volley.newRequestQueue(this);
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //queue = Volley.newRequestQueue(this);

        //8MB
        //Cache cache = new DiskBasedCache(getCacheDir(),1024*1024);
        //Network network = new BasicNetwork(new HurlStack());
        //queue = new RequestQueue(cache,network,2);
        //queue.start();
        queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        Button main_reload_btn = (Button) findViewById(R.id.main_reload_btn);
        main_reload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLoad();
            }
        });


        m_Adapter = new ListRowsAdapter();

        ListView main_listView = (ListView) this.findViewById(R.id.main_listView);
        main_listView.setAdapter( m_Adapter);

//        m_Adapter.add(new JSONObject());
//        m_Adapter.add(new JSONObject());
//        m_Adapter.add(new JSONObject());
//
//        m_Adapter.notifyDataSetChanged();
        firstLoad();
    }

    private void firstLoad(){
        queue.cancelAll("firstLoad");

        Toast.makeText(getApplicationContext(),"최초 로드 시작",Toast.LENGTH_SHORT).show();
        String url1 = "http://www.mins01.com/sdgn/json/units";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray su_rows;
                try {
                    su_rows = response.getJSONArray("su_rows");
                    for(int i=0,m= su_rows.length();i<m;i++){
                        m_Adapter.add((JSONObject) su_rows.get(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                m_Adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
//        final StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("firstLoad",response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray su_rows = jsonObject.getJSONArray("su_rows");
//                            for(int i=0,m= su_rows.length();i<m;i++){
//                                m_Adapter.add((JSONObject) su_rows.get(i));
//
//                            }
//                            m_Adapter.notifyDataSetChanged();
//                        } catch (JSONException e) {
//                            Log.e("firstLoad ERROR",response);
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("volley","onErrorResponse");
//                        error.printStackTrace();
//                    }
//                }
//        );
//        stringRequest1.setTag("firstLoad");
//        queue.add(stringRequest1);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(queue != null){
            queue.cancelAll("firstLoad");
        }
    }
}
