package com.mins01.app001;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long backpress_time_ms = 0;
    private UserSession usess;
    private NavigationView navigationView;

    private AdView mAdView; //광고용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        backpress_time_ms = System.currentTimeMillis();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true); //최초 메뉴를 기본 선택한다.
        showFragment(R.id.nav_units_lists);

        usess = UserSession.getInstance(this.getApplicationContext());

        initAdMob();


    }

    @Override
    public void onStart() {
        Log.i("onStart", "START");
        super.onStart();
        //initUI();
        Log.i("onStart", "END");

        initEvent();
        syncUsess();

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
        mAdView.resume();
        Log.i("onResume", "END");



    }

    @Override
    public void onPause() {
        Log.i("onPause", "START");
//        adView.pause();
        super.onPause();
        mAdView.pause();
        Log.i("onPause", "END");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i("onDestroy", "START");
//        adView.destroy();
        super.onDestroy();
        mAdView.destroy();
        //MySingleton.getInstance(this).stop();
        Log.i("onDestroy", "END");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long this_time_ms = System.currentTimeMillis();
            if (this_time_ms - backpress_time_ms < 4000) {
                moveTaskToBack(true);
                finish();
            } else {
                backpress_time_ms = this_time_ms;
                Toast.makeText(this, "다시 누르시면 종료합니다.", Toast.LENGTH_SHORT).show();

            }
            //super.onBackPressed();
        }
    }

    private void initAdMob() {
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("2C251C7B8EEA8AF17FBA97F61D60D7C6")
                .build();
        mAdView.loadAd(adRequest);
    }

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
        } else if (id == R.id.developer) {
            Toast.makeText(this, "앱개발자 홈페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("http://www.mins01.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        } else if (id == R.id.game_site) {
            Toast.makeText(this, "SD건담 넥스트 에볼루션 사이트로 이동합니다.\n같이 게임해요.", Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("http://sdgn.co.kr/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        } else if (id == R.id.app_version) {
            PackageInfo pInfo;
            try {
                pInfo = getPackageManager().getPackageInfo(
                        this.getPackageName(), 0);
                int versionCode = pInfo.versionCode;
                String versionName = pInfo.versionName;
                Toast.makeText(this, "버전 " + versionName + " (" + versionCode + ")", Toast.LENGTH_SHORT).show();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean r = showAvtivity(id);
        if(!r){
            return false;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void btnClick(View v){
        int id = v.getId();
        showAvtivity(id);
    }
    public boolean showAvtivity(int id){
        Intent intent;
        switch (id){
            case R.id.linearLayout_login:
                if(usess.m_nick.length()>0){
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                            usess.removeUserData();
                            syncUsess();
                        }
                    });
                    alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("로그아웃 하시겠습니까?");
                    alert.show();
                    return true;
                }
            //case R.id.nav_login:
                Context context = this.getBaseContext();
                intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return false;
            case R.id.textView_mins01_com:
                Toast.makeText(MainActivity.this, "건넥한마디 사이트로 이동합니다.", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("http://www.mins01.com/sdgn");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return false;
            default:
                if (!showFragment(id)) {
                    return false;
                }
                break;
        }
        return true;
    }
    public boolean showFragment(int id) {
        Fragment fr;
        switch (id) {
            case R.id.nav_units_lists:
                fr = new FragmentUnitsLists();
                break;
//            case R.id.nav_test:
//                fr = new FragmentTest();
//                break;
            default:
                Log.i("플레그멘트보이기", "FALSE");
                return false;
        }
        Log.i("플레그멘트보이기", fr.getClass().getName());


        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.commit();
        return true;
    }
    //잡다 이벤트 정의
    public void initEvent(){
        navigationView.getHeaderView(0).findViewById(R.id.linearLayout_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClick(v);
            }
        });
    }
    public void syncUsess(){
        TextView textView_nav_m_nick = (TextView)navigationView.getHeaderView(0).findViewById(R.id.textView_nav_m_nick);
        if(usess.m_nick.length()>0){ //로그인된 사용자명
            textView_nav_m_nick.setText(usess.m_nick);
        }else{
            textView_nav_m_nick.setText("로그인이 필요합니다.\n사이트에서 회원가입이 가능합니다.");
        }
    }

}
