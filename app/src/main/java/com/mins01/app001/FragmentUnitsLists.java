package com.mins01.app001;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * 유닛 목록 플레그 멘트
 */
public class FragmentUnitsLists extends Fragment {
    static {
        com.android.volley.VolleyLog.DEBUG = true;
    }
    private String title;
    private AlertDialog searchAlert;

    private String sh_unit_name="";
    private Boolean sh_unit_ranks_S = false;
    private Boolean sh_unit_ranks_A = false;
    private Boolean sh_unit_ranks_B = false;
    private Boolean sh_unit_ranks_C = false;
    private Boolean sh_unit_properties_1 = false;
    private Boolean sh_unit_properties_2 = false;
    private Boolean sh_unit_properties_3 = false;

    private int sh_cnt = 0;
    private int sh_cnt_all = 0;

    //private AdView adView;
    private GridRowsAdapter m_Adapter;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000; //5초
    private long backpress_time_ms = 0;

    public FragmentUnitsLists(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(this.getClass().getName(), "onCreateView");
        CoordinatorLayout cl =  (CoordinatorLayout) inflater.inflate(R.layout.fragment_sdgn_lists, container, false);
        initSearch();
        return cl;
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
            title = getResources().getString(R.string.app_title) + " - 로딩...";
        }
        activity.setTitle(title);

        //리로드 버튼
        Button main_btn_reload = (Button) activity.findViewById(R.id.main_btn_reload);
        main_btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLoad(true);
            }
        });

        //검색 버튼
        Button main_btn_search = (Button) activity.findViewById(R.id.main_btn_search);
        main_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearch();
            }
        });
    }
    private void showSearch() {
        Activity activity = getActivity();

        searchAlert.show();
        loadShQstr();
//        final EditText sh_unit_name = (EditText) searchAlert.findViewById(R.id.sh_unit_name);
//        //키보드 보이기
//        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        sh_unit_name.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                sh_unit_name.requestFocus();
//                imm.showSoftInput(sh_unit_name, 0);
//            }
//        }, 100);
//
//        sh_unit_name.requestFocus();
    }

    /**
     * 검색용 쿼리 스트링 가져오기
     * @return Qstr
     */
    private String getShQstr(){
        ArrayList<String> strs = new ArrayList<>();
        try {
            if(sh_unit_name.length()>0){
                strs.add("unit_name="+ URLEncoder.encode(sh_unit_name,"UTF-8"));
            }
            // 랭크
            if(sh_unit_ranks_S){
                strs.add(URLEncoder.encode("unit_ranks[]","UTF-8")+"=S");
            }
            if(sh_unit_ranks_A){
                strs.add(URLEncoder.encode("unit_ranks[]","UTF-8")+"=A");
            }
            if(sh_unit_ranks_B){
                strs.add(URLEncoder.encode("unit_ranks[]","UTF-8")+"=B");
            }
            if(sh_unit_ranks_C){
                strs.add(URLEncoder.encode("unit_ranks[]","UTF-8")+"=C");
            }
            if(sh_unit_properties_1){
                strs.add(URLEncoder.encode("unit_properties_nums[]","UTF-8")+"=1");
            }
            if(sh_unit_properties_2){
                strs.add(URLEncoder.encode("unit_properties_nums[]","UTF-8")+"=2");
            }
            if(sh_unit_properties_3){
                strs.add(URLEncoder.encode("unit_properties_nums[]","UTF-8")+"=3");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        for (String s : strs)
        {
            if(sb.length()>0){
                sb.append("&");
            }
            sb.append(s);
        }

        return sb.toString();
    }
    private void initSearch(){
        Activity activity = getActivity();

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.inc_filter, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptView);
        //키보드 제어용
//        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // setup a dialog window
        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("적용", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //resultText.setText("Hello, " + editText.getText());
//                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        saveShQstr();
                        firstLoad(true);
                        dialog.cancel();

                    }
                })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        searchAlert = alertDialogBuilder.create();

    }
    private void saveShQstr(){
        sh_unit_name = ((EditText)searchAlert.findViewById(R.id.sh_unit_name)).getText().toString();
        sh_unit_properties_1 = ((CheckBox)searchAlert.findViewById(R.id.sh_unit_properties_1)).isChecked();
        sh_unit_properties_2 = ((CheckBox)searchAlert.findViewById(R.id.sh_unit_properties_2)).isChecked();
        sh_unit_properties_3 = ((CheckBox)searchAlert.findViewById(R.id.sh_unit_properties_3)).isChecked();
        sh_unit_ranks_S = ((CheckBox)searchAlert.findViewById(R.id.sh_unit_ranks_S)).isChecked();
        sh_unit_ranks_A = ((CheckBox)searchAlert.findViewById(R.id.sh_unit_ranks_A)).isChecked();
        sh_unit_ranks_B = ((CheckBox)searchAlert.findViewById(R.id.sh_unit_ranks_B)).isChecked();
        sh_unit_ranks_C = ((CheckBox)searchAlert.findViewById(R.id.sh_unit_ranks_C)).isChecked();
    }
    private void loadShQstr(){
        ((EditText)searchAlert.findViewById(R.id.sh_unit_name)).setText(sh_unit_name);
        ((CheckBox)searchAlert.findViewById(R.id.sh_unit_properties_1)).setChecked(sh_unit_properties_1);
        ((CheckBox)searchAlert.findViewById(R.id.sh_unit_properties_2)).setChecked(sh_unit_properties_2);
        ((CheckBox)searchAlert.findViewById(R.id.sh_unit_properties_3)).setChecked(sh_unit_properties_3);

        ((CheckBox)searchAlert.findViewById(R.id.sh_unit_ranks_S)).setChecked(sh_unit_ranks_S);
        ((CheckBox)searchAlert.findViewById(R.id.sh_unit_ranks_A)).setChecked(sh_unit_ranks_A);
        ((CheckBox)searchAlert.findViewById(R.id.sh_unit_ranks_B)).setChecked(sh_unit_ranks_B);
        ((CheckBox)searchAlert.findViewById(R.id.sh_unit_ranks_C)).setChecked(sh_unit_ranks_C);
    }
    private void firstLoad() {
        this.firstLoad(false);
    }
    private void firstLoad(boolean force) {
        final Activity activity = getActivity();
        Log.i("firstLoad", "START");
        //Toast.makeText(getApplicationContext(),"데이터 로딩",Toast.LENGTH_SHORT).show();
        final UnitRows unitRows = UnitRows.getInstance();
        ArrayList su_rows = unitRows.getRows();
        m_Adapter.clear();
        if (!force && su_rows != null) {

            for (int i = 0, m = su_rows.size(); i < m; i++) {
                m_Adapter.add((JSONObject) su_rows.get(i));
            }
            m_Adapter.notifyDataSetChanged();

            title = getResources().getString(R.string.app_title) + " - " + unitRows.su_cnt + "유닛 (총 "+ unitRows.su_cnt_all+")";
            activity.setTitle(title);
        } else {
            String url1 = "http://www.mins01.com/sdgn/json/units";
            String qstr = getShQstr();
            if(qstr.length()>0){
                url1+="?"+qstr;
            }
            Log.e("URL", url1);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        unitRows.su_cnt = response.getInt("su_cnt");
                        unitRows.su_cnt_all = response.getInt("su_cnt_all");
                        JSONArray su_rows = response.getJSONArray("su_rows");
                        unitRows.setRows(su_rows);
                        for (int i = 0, m = su_rows.length(); i < m; i++) {
                            m_Adapter.add((JSONObject) su_rows.get(i));
                        }
                        Toast.makeText(activity.getBaseContext(), "데이터 로드 완료 : " + m_Adapter.getCount(), Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    m_Adapter.notifyDataSetChanged();

                    title = getResources().getString(R.string.app_title) + " - " + unitRows.su_cnt + "유닛 (총 "+ unitRows.su_cnt_all+")";
                    activity.setTitle(title);
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
                result.firstLoad(true);
            }

        }).execute(this);
    }
}
