package com.mins01.app001;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

/**
 * 유닛 목록 플레그 멘트
 */
public class FragmentUnitsLists extends Fragment {
    static {
        com.android.volley.VolleyLog.DEBUG = true;
    }
    private String title;

    //private AdView adView;
    private GridRowsAdapter m_Adapter;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000; //5초
    private long backpress_time_ms = 0;

    public FragmentUnitsLists(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(this.getClass().getName(), "onCreateView");
        return inflater.inflate(R.layout.fragment_sdgn_lists, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(this.getClass().getName(), "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        firstAction();
    }

    @Override
    public void onStart() {
        Log.i(this.getClass().getName(), "onStart");
//        firstAction();
        super.onStart();
    }

    public void initUI() {
        Activity activity = getActivity();
        if (m_Adapter == null) {
            m_Adapter = new GridRowsAdapter();

            GridView gridView = (GridView) activity.findViewById(R.id.detail_gridView);
            gridView.setAdapter(m_Adapter);
        }

        if(title==null){
            title = getResources().getString(R.string.app_title) + " - 총 " + m_Adapter.getCount() + "유닛";
        }
        activity.setTitle(title);

        //리로드 버튼
        Button main_btn_reload = (Button) activity.findViewById(R.id.main_btn_reload);
        main_btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLoad();
            }
        });
    }

    private void firstLoad() {
        final Activity activity = getActivity();
        Log.i("firstLoad", "START");
        //Toast.makeText(getApplicationContext(),"데이터 로딩",Toast.LENGTH_SHORT).show();
        final UnitRows unitRows = UnitRows.getInstance();
        ArrayList su_rows = unitRows.getRows();
        if (su_rows != null) {
            m_Adapter.clear();
            for (int i = 0, m = su_rows.size(); i < m; i++) {
                m_Adapter.add((JSONObject) su_rows.get(i));
            }
            m_Adapter.notifyDataSetChanged();
            title = getResources().getString(R.string.app_title) + " - 총 " + m_Adapter.getCount() + "유닛";
            activity.setTitle(title);
        } else {
            String url1 = "http://www.mins01.com/sdgn/json/units";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        m_Adapter.clear();
                        JSONArray su_rows = response.getJSONArray("su_rows");
                        unitRows.setRows(su_rows);
                        for (int i = 0, m = su_rows.length(); i < m; i++) {
                            m_Adapter.add((JSONObject) su_rows.get(i));
                        }
                        Toast.makeText(activity.getBaseContext(), "데이터 로드 완료 : " + m_Adapter.getCount(), Toast.LENGTH_SHORT).show();
                        title = getResources().getString(R.string.app_title) + " - 총 " + m_Adapter.getCount() + "유닛";
                        activity.setTitle(title);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    m_Adapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity.getBaseContext(), "데이터 로드 에러 : ", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
        }
        Log.i("firstLoad", "END");
    }


    private void firstAction() {
        (new AsyncTask<FragmentUnitsLists, Void, FragmentUnitsLists>() {
            @Override
            protected FragmentUnitsLists doInBackground(FragmentUnitsLists... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(FragmentUnitsLists result) {
                //super.onPostExecute(result);
                result.initUI();
                //result.initAdMob();
                result.firstLoad();
            }

        }).execute(this);
    }
}
