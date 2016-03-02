package com.mins01.sdgn;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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
    private AdView adView;
    private GridRowsAdapter m_Adapter;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000; //5초
    private long backpress_time_ms = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        backpress_time_ms = System.currentTimeMillis();
        Log.i("onCreate", "START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdgn_lists);
        this.setTitle(R.string.app_name);
        adView = (AdView) findViewById(R.id.adView);
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
        adView.resume();
        Log.i("onResume", "END");
    }
    @Override
    public void onPause(){
        Log.i("onPause", "START");
        adView.pause();
        super.onPause();

        Log.i("onPause", "END");
    }
    @Override
    protected void onStop(){
        super.onStop();

    }
    @Override
    public void onDestroy() {
        Log.i("onDestroy", "START");
        adView.destroy();
        super.onDestroy();
        //MySingleton.getInstance(this).stop();

        Log.i("onDestroy", "END");
    }
    private void initAdMob(){

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
    private void initUI(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        //Toast.makeText(getApplicationContext(),"데이터 로딩",Toast.LENGTH_SHORT).show();
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
                    SdgnListsActivity.this.setTitle(SdgnListsActivity.this.getTitle()+" - 총 "+m_Adapter.getCount()+"유닛");
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
                result.initAdMob();
                result.firstLoad();
            }

        }).execute(this);
    }

    //-- 메뉴관련
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sdgn_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notice) {
            String msg = "유닛이나 스킬 등의 저작권은 sdgn.co.kr에 문의해주시기 바랍니다.\n" +
                    "이 앱은 SD건담넥스트에볼루션 팬앱일 뿐입니다.\n" +
                    "사용으로 인한 문제에 대해서는 책임지지 않습니다.\n" +
                    "즐겁게 게임을 즐깁시다.";
//            Toast.makeText(this,"유닛이나 스킬 등의 이미지 저작권은 sdgn.co.kr에 문의해주시기 바랍니다.\n" +
//                    "이 앱은 SD건담넥스트에볼루션 팬앱일 뿐입니다. \n" +
//                    "사용함으로 발생되는 불이익에 대해서는 책임지지 않습니다.\n" +
//                    "즐겁게 게임과 사이트를 즐깁시다.",Toast.LENGTH_LONG).show();
//            Snackbar.make(this.getWindow().getDecorView(), "유닛이나 스킬 등의 이미지 저작권은 sdgn.co.kr에 문의해주시기 바랍니다.\n" +
//                    "이 앱은 SD건담넥스트에볼루션 팬앱일 뿐입니다. \n" +
//                    "사용함으로 발생되는 불이익에 대해서는 책임지지 않습니다.\n" +
//                    "즐겁게 게임과 사이트를 즐깁시다.", Snackbar.LENGTH_LONG)
//                    //.setAction(R.string.snackbar_action, myOnClickListener)
//                    .show();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();     //닫기
                }
            });
            alert.setMessage(msg);
            alert.show();
            return true;
        }else if(id == R.id.developer){
            Toast.makeText(this,"앱개발자 홈페이지로 이동합니다.",Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("http://www.mins01.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }else if(id == R.id.game_site){
            Toast.makeText(this,"SD건담 넥스트 에볼루션 사이트로 이동합니다.\n같이 게임해요.",Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("http://sdgn.co.kr/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }else if(id == R.id.app_version){
            PackageInfo pInfo;
            try {
                pInfo = getPackageManager().getPackageInfo(
                        this.getPackageName(), 0);
                int versionCode = pInfo.versionCode;
                String versionName = pInfo.versionName;
                Toast.makeText(this,"버전 "+versionName+" ("+versionCode+")",Toast.LENGTH_SHORT).show();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        long this_time_ms = System.currentTimeMillis();
        if(this_time_ms-backpress_time_ms < 4000) {
            moveTaskToBack(true);
            finish();
        }else {
            backpress_time_ms = this_time_ms;
            Toast.makeText(this, "다시 누르시면 종료합니다.", Toast.LENGTH_SHORT).show();

        }
    }

}
