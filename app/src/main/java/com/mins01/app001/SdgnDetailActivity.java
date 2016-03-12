/**
 * 이건 더이상 안 쓴다. SdgnDetailTabActivity를 대신 쓴다.
 */
package com.mins01.app001;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SdgnDetailActivity extends AppCompatActivity {
    private JSONObject row;
    private int unit_idx;
    //    private BcRowsAdapter m_Adapter;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000; //5초

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate", "START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdgn_detail);
        UnitRows unitrow = UnitRows.getInstance();

        unit_idx = getIntent().getIntExtra("unit_idx", 0);
        row = unitrow.getRowByUnitIdx(unit_idx);

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
        final SdgnDetailActivity thisC = this;
        final Context context = this.getApplicationContext();
        UnitCardHolder unitCardHolder = new UnitCardHolder();
        View convertView = this.findViewById(R.id.activity_sdgn_detail_top);
        unitCardHolder.setMemberVar(convertView);
        unitCardHolder.setValues(row, convertView);
        try {
            //-- 상단 부분
            NetworkImageView imageView_unit_anime_img = (NetworkImageView) this.findViewById(R.id.imageView_unit_anime_img);
            imageView_unit_anime_img.setImageUrl(row.getString("unit_anime_img"), MySingleton.getInstance(context).getImageLoader());
            ((TextView) this.findViewById(R.id.textView_unit_anime)).setText(row.getString("unit_anime"));
            ((TextView) this.findViewById(R.id.textView_unit_txt)).setText(row.getString("unit_txt"));


            //-- 내용 부분
            String tmp;
            LinearLayout imgs, texts;

            imgs = (LinearLayout) this.findViewById(R.id.group_unit_weapon_img);
            texts = (LinearLayout) this.findViewById(R.id.group_unit_weapon);
            for (int i = 0, m = 3; i < m; i++) {
                tmp = row.getString("unit_weapon" + (i + 1) + "_img");
                if (tmp != null && tmp.length() > 1) {
                    ((NetworkImageView) imgs.getChildAt(i)).setImageUrl(tmp, MySingleton.getInstance(context).getImageLoader());
                    ((TextView) texts.getChildAt(i)).setText(row.getString("unit_weapon" + (i + 1)));
                } else {
                    ((NetworkImageView) imgs.getChildAt(i)).setImageResource(android.R.color.transparent);//초기화
                    ((TextView) texts.getChildAt(i)).setText("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void firstLoad() {
//        if (m_Adapter == null) {
//            m_Adapter = new BcRowsAdapter();
//
//            ListView listView = (ListView) this.findViewById(R.id.listView_bc_rows);
//            listView.setAdapter(m_Adapter);
//        }

//        String url1 = "http://www.mins01.com/sdgn/json/units";

        String url1 = "http://www.mins01.com/mh/bbs_comment/sdgn_units/";
        try {
            url1 += row.getString("unit_idx");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    LinearLayout linearLayout_bc_rows = (LinearLayout) findViewById(R.id.linearLayout_bc_rows);
//                    m_Adapter.clear();
                    JSONArray bc_rows = response.getJSONArray("bc_rows");
                    Context context = SdgnDetailActivity.this.getApplicationContext();

                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat formater = new SimpleDateFormat("yy.M.d h:m");

//                    ViewGroup parent = (ViewGroup) SdgnDetailActivity.this.getWindow().getDecorView().getRootView();
                    for (int i = 0, m = bc_rows.length(); i < m; i++) {
//                        m_Adapter.add((JSONObject) bc_rows.get(i));
                        JSONObject bc_row = (JSONObject) bc_rows.get(i);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View convertView = inflater.inflate(R.layout.bc_row_sdgn_detail_comment, linearLayout_bc_rows, false);
                        ((TextView) convertView.findViewById(R.id.textView_bc_name)).setText(bc_row.getString("bc_name"));
                        Date d = null;
                        try {
                            d = parser.parse(bc_row.getString("bc_insert_date"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ((TextView) convertView.findViewById(R.id.textView_bc_insert_date)).setText(formater.format(d));
                        ((TextView) convertView.findViewById(R.id.textView_bc_comment)).setText(bc_row.getString("bc_comment"));

                        int bc_number = bc_row.getInt("bc_number");
                        String bc_number_str = "";
                        switch (bc_number) {
                            case 0:
                                break;
                            case 1:
                                bc_number_str = "★☆☆☆☆";
                                break;
                            case 2:
                                bc_number_str = "★★☆☆☆";
                                break;
                            case 3:
                                bc_number_str = "★★★☆☆";
                                break;
                            case 4:
                                bc_number_str = "★★★★☆";
                                break;
                            case 5:
                                bc_number_str = "★★★★★";
                                break;

                        }
                        TextView textView_bc_number = (TextView) convertView.findViewById(R.id.textView_bc_number);
                        if (bc_number == 0) {
                            textView_bc_number.setVisibility(View.GONE);
                        } else {
                            textView_bc_number.setVisibility(View.VISIBLE);
                            textView_bc_number.setText(bc_number_str);
                        }


                        linearLayout_bc_rows.addView(convertView);
                    }
//                    Toast.makeText(getApplicationContext(), "데이터 로드 완료 : " + bc_rows.length(), Toast.LENGTH_SHORT).show();
                    //SdgnListsActivity.this.setTitle(SdgnListsActivity.this.getTitle() + " - 총 " + m_Adapter.getCount() + "유닛");
//                    Log.i("JSON LOAD",response.toString());
                } catch (JSONException e) {

                    e.printStackTrace();
                }

//                m_Adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "데이터 로드 에러 : ", Toast.LENGTH_SHORT).show();
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

    private void firstAction() {
        (new AsyncTask<SdgnDetailActivity, Void, SdgnDetailActivity>() {
            @Override
            protected SdgnDetailActivity doInBackground(SdgnDetailActivity... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(SdgnDetailActivity result) {
                //super.onPostExecute(result);
                //result.firstLoad();
                initUI();
                // firstLoad();
            }

        }).execute(this);
    }

}
