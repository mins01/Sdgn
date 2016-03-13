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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long backpress_time_ms = 0;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true); //최초 메뉴를 기본 선택한다.
        showFragment(R.id.nav_units_lists);
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
    public void onResume() {
        Log.i("onResume", "START");
        super.onResume();
//        adView.resume();
        Log.i("onResume", "END");
    }

    @Override
    public void onPause() {
        Log.i("onPause", "START");
//        adView.pause();
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
//        adView.destroy();
        super.onDestroy();
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
        switch (id){
            case R.id.nav_login:
                Context context = this.getBaseContext();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            default:
                if (!showFragment(id)) {
                    return false;
                }
                break;
        }

//        if (id == R.id.nav_units_lists) {
//            // Handle the camera action
//        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean showFragment(int id) {
        Fragment fr;
        switch (id) {
            case R.id.nav_units_lists:
                fr = new FragmentUnitsLists();
                break;
            case R.id.nav_test:
                fr = new FragmentTest();
                break;
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

}
