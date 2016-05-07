package com.mins01.app001;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
 * Created by mins01 on 2016-03-13.
 */
public class FragmentLastPlan extends Fragment {
    static {
        com.android.volley.VolleyLog.DEBUG = true;
    }

    //private AdView adView;
    private LvAdapter m_Adapter;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000; //5초
    private long backpress_time_ms = 0;
    private ListView lv = null;
    private Activity act = null;

    private JSONObject response = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(this.getClass().getName(), "onCreateView");
        RelativeLayout cl =  (RelativeLayout) inflater.inflate(R.layout.fragment_sdgn_last_plan, container, false);
        //initSearch();
        return cl;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(this.getClass().getName(), "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        this.act = getActivity();
        initUI();
    }

    @Override
    public void onStart() {
        Log.i(this.getClass().getName(), "onStart");
//        firstAction();
        super.onStart();
    }

    public void initUI() {
        this.lv = (ListView) act.findViewById(R.id.listView_b_rows);

        if (m_Adapter == null) {
            m_Adapter = new LvAdapter();
            lv.setAdapter(m_Adapter);
        }
        act.setTitle("최근 일정");
        firstLoad();
    }

    private void firstLoad() {
        this.firstLoad(false);
    }
    private void firstLoad(boolean force) {
        final Activity activity = getActivity();
        Log.i("firstLoad", "START");
        //Toast.makeText(getApplicationContext(),"데이터 로딩",Toast.LENGTH_SHORT).show();
        m_Adapter.clear();
        String url1 = "http://www.mins01.com/sdgn/json/last_plan";
        String qstr = "";
        if(qstr.length()>0){
            url1+="?"+qstr;
        }
        Log.e("URL", url1);
        if(this.response==null) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    FragmentLastPlan.this.response = response;
                    try {
                        ((TextView) act.findViewById(R.id.plan_dt_st)).setText(
                                "검색기간 : "+response.getString("plan_dt_st") + " ~ " + response.getString("plan_dt_ed")
                        );
                        JSONArray plan_b_rows = response.getJSONArray("plan_b_rows");
                        for (int i = 0, m = plan_b_rows.length(); i < m; i++) {
                            m_Adapter.add((JSONObject) plan_b_rows.get(i));
                        }
                        //Toast.makeText(activity.getBaseContext(), "데이터 로드 완료 : " + m_Adapter.getCount(), Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    m_Adapter.notifyDataSetChanged();

//                    title = getResources().getString(R.string.app_title) + " - " + unitRows.su_cnt + "유닛 (총 "+ unitRows.su_cnt_all+")";
//                    activity.setTitle(title);
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
        }else{
            try {
                ((TextView) act.findViewById(R.id.plan_dt_st)).setText(
                        response.getString("plan_dt_st") + " ~ " + response.getString("plan_dt_ed")
                );
                JSONArray plan_b_rows = response.getJSONArray("plan_b_rows");
                for (int i = 0, m = plan_b_rows.length(); i < m; i++) {
                    m_Adapter.add((JSONObject) plan_b_rows.get(i));
                }
                Toast.makeText(activity.getBaseContext(), "데이터 로드 완료 : " + m_Adapter.getCount(), Toast.LENGTH_SHORT).show();


            } catch (JSONException e) {

                e.printStackTrace();
            }
            m_Adapter.notifyDataSetChanged();
        }
        Log.i("firstLoad", "END");
    }
    public class LvHolder{
        public String text_st_date = "";
        public String text_status = "";
        public String text_b_title = "";
    }
    public class LvAdapter extends BaseAdapter{
        private ArrayList<JSONObject> rows;

        public LvAdapter(){
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

        public void add(JSONObject row){
            rows.add(row);
        }

        public void clear(){
            rows.clear();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i("getView", String.valueOf(position));
            final Context context = parent.getContext();
            LvHolder lvHolder;

            final JSONObject row = rows.get(position);
            if (convertView == null) {


                Log.i("getView", Context.LAYOUT_INFLATER_SERVICE);
                lvHolder = new LvHolder();


                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.fragment_sdgn_last_plan_row, parent, false);
                try {
                    lvHolder.text_st_date = row.getString("b_etc_0")+" ~ "+row.getString("b_etc_1");
                    lvHolder.text_status = row.getString("plan_status");
                    String t = "";
                    t = row.getString("b_category");
                    if(t.length()>0){
                        t= "["+t+"] "+ row.getString("b_title");
                    }else{
                        t= row.getString("b_title");
                    }
                    lvHolder.text_b_title = t;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                convertView.setTag(lvHolder);
            } else {
                lvHolder = (LvHolder) convertView.getTag();
            }
            ((TextView) convertView.findViewById(R.id.text_st_date)).setText(lvHolder.text_st_date);
            ((TextView) convertView.findViewById(R.id.text_status)).setText(lvHolder.text_status);
            ((TextView) convertView.findViewById(R.id.text_b_title)).setText(lvHolder.text_b_title);
            //lvHolder.setValues(row, convertView);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                try {

                    Toast.makeText(context,"웹 페이지를 오픈합니다.",Toast.LENGTH_SHORT).show();

                    String b_idx = row.getString("b_idx");
                    String str = "http://www.mins01.com/sdgn/plan/read/" + b_idx;

                    Uri uri = Uri.parse(str);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }
            });

            return convertView;
        }
    }
}


