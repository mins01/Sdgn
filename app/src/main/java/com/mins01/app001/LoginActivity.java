package com.mins01.app001;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    final public int MY_SOCKET_TIMEOUT_MS = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void submit(View v){
        Toast.makeText(getBaseContext(),"로그인을 동작합니다.",Toast.LENGTH_SHORT).show();
        EditText editText_m_id = (EditText) findViewById(R.id.editText_m_id);
        EditText editText_m_pass = (EditText) findViewById(R.id.editText_m_pass);
        String m_id = editText_m_id.getText().toString();
        String m_pass = editText_m_pass.getText().toString();
//        editText_m_id.setText("");
//        editText_m_pass.setText("");
        json_login(getBaseContext(),m_id,m_pass);
    }

    public void json_login(final Context context, final String m_id, final String m_pass){

        Log.i("firstLoad", "START");

        String url1 = "http://www.mins01.com/mh/member/json_login";

//        JSONObject postData = new JSONObject();
        Map postData = new HashMap<String,String>();
        postData.put("m_id",m_id);
        postData.put("m_pass",m_pass);

        Log.i("XX",postData.toString());
        MyHttpRequest jsonObjectRequest = new MyHttpRequest(Request.Method.POST, url1, postData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("YY",jsonObject.toString());
                Toast.makeText(context, "데이터 로드 성공 : "+jsonObject.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "데이터 로드 에러 : ", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, postData, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.i("YY",response.toString());
//                Toast.makeText(context, "데이터 로드 성공 : "+response.toString(), Toast.LENGTH_LONG).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "데이터 로드 에러 : ", Toast.LENGTH_SHORT).show();
//                error.printStackTrace();
//            }
//        });
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                MY_SOCKET_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
//        StringRequest postRequest = new StringRequest(Request.Method.POST, url1,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.d("Response", response);
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        //Log.d("Error.Response", response);
//                        error.printStackTrace();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("m_id", m_id);
//                params.put("m_pass", m_pass);
//
//                return params;
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(
//                MY_SOCKET_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        MySingleton.getInstance(context).addToRequestQueue(postRequest);
        Log.i("firstLoad", "END");
    }

}
