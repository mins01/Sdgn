package com.mins01.app001;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SdgnDetailTabActivity extends AppCompatActivity {
    private JSONObject row;
    private JSONArray sw_rows;
    private int unit_idx;

    public static final int MY_SOCKET_TIMEOUT_MS = 5000; //5초
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdgn_detail);

        UnitRows unitrow = UnitRows.getInstance();
        unit_idx = getIntent().getIntExtra("unit_idx", 0);
        row = unitrow.getRowByUnitIdx(unit_idx);

        try {
            setTitle("유닛상세 - " + row.getString("unit_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        firstAction();
    }

    public void initSectionsPagerAdapter(){
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_sdgn_detail_tab, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        public static SdgnDetailTabActivity thisC;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_sdgn_detail_tab, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            View rootView = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = inflater.inflate(R.layout.activity_sdgn_detail_bc_rows, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.activity_sdgn_detail_content, container, false);
                    break;
            }
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                for (String key : savedInstanceState.keySet()) {
                    Log.i("savedInstanceState : ", key);
                }
            }
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    PlaceholderFragment.thisC.firstLoad(this.getView());
                    break;
                case 2:
                    PlaceholderFragment.thisC.initDetailContent(this.getView());
                    break;
            }
            super.onActivityCreated(savedInstanceState);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            PlaceholderFragment.thisC = SdgnDetailTabActivity.this; //귀찮다.
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "한마디";
                case 1:
                    return "유닛설명";
            }
            return null;
        }
    }

    //--------------------------
    public void initDetailTop() {
        UnitCardHolder unitCardHolder = new UnitCardHolder();
        View convertView = this.findViewById(R.id.activity_sdgn_detail_top);
        unitCardHolder.setMemberVar(convertView);
        unitCardHolder.setValues(row, convertView);
        final Context context = this.getApplicationContext();



        View unitCard = this.findViewById(R.id.layout_unit_card);

        unitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Toast.makeText(context,"유닛 상세 웹 페이지를 오픈합니다.",Toast.LENGTH_SHORT).show();

                    String unit_idx = row.getString("unit_idx");
                    String str = "http://www.mins01.com/sdgn/units?unit_idx=" + unit_idx;

                    Uri uri = Uri.parse(str);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        try {
            int unit_is_transform = row.getInt("unit_is_transform");
            int unit_is_weapon_change = row.getInt("unit_is_weapon_change");
            //-- 상단 부분
            NetworkImageView imageView_unit_anime_img = (NetworkImageView) this.findViewById(R.id.imageView_unit_anime_img);
            imageView_unit_anime_img.setImageUrl(row.getString("unit_anime_img"), MySingleton.getInstance(context).getImageLoader());
            ((TextView) this.findViewById(R.id.textView_unit_anime)).setText(row.getString("unit_anime"));
            ((TextView) this.findViewById(R.id.textView_unit_txt)).setText(row.getString("unit_txt"));
            ArrayList<String> x = new ArrayList<>();

            x.add(row.getString("unit_movetype"));
            if(unit_is_transform == 1){
                x.add("변신");
            }
            if(unit_is_weapon_change==1) {
                x.add("웨폰체인지");
            }

            ((TextView) this.findViewById(R.id.textView_bc_properties)).setText(android.text.TextUtils.join("/", x));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initDetailContent(View view) {
        Log.d("initDetailContent", "Start");
        final Context context = view.getContext();
        try {
            int unit_is_transform = row.getInt("unit_is_transform");
            //-- 내용 부분
            String tmp;
            LinearLayout imgs, texts;

            //-- 스킬
            NetworkImageView niv = null;
            final int[] niv_ids = {R.id.imageView_skill1_img, R.id.imageView_skill2_img, R.id.imageView_skill3_img, R.id.imageView_skill4_img};
            final int[] tv_ids = {R.id.imageView_skill1, R.id.imageView_skill2, R.id.imageView_skill3, R.id.imageView_skill4};
            for (int i = 0, m = 4; i < m; i++) {
                if (!row.isNull("unit_skill" + (i + 1) + "_img")) {
                    tmp = row.getString("unit_skill" + (i + 1) + "_img");

                    ((NetworkImageView) view.findViewById(niv_ids[i])).setImageUrl(tmp, MySingleton.getInstance(context).getImageLoader());
                    if (i == 3) {
                        if(unit_is_transform==1){
                            ((TextView) view.findViewById(tv_ids[i])).setText("(변신 후)\n[" + row.getString("unit_skill" + (i + 1)) + "]\n" + row.getString("unit_skill" + (i + 1) + "_desc"));
                        }else{
                            view.findViewById(tv_ids[i]).setVisibility(View.GONE);
                        }

                    } else {
                        ((TextView) view.findViewById(tv_ids[i])).setText("[" + row.getString("unit_skill" + (i + 1)) + "]\n" + row.getString("unit_skill" + (i + 1) + "_desc"));
                    }

                } else {
                    ((NetworkImageView) view.findViewById(niv_ids[i])).setImageResource(android.R.color.transparent);//초기화
                    ((TextView) view.findViewById(tv_ids[i])).setText("");
                }
            }
            //Log.e("무기",sw_rows.toString());
            LinearLayout tagetView = null;
            //무기 루프 돌리기

            View v = null;
            if(!sw_rows.isNull(0)){
                JSONArray sw_rows_0 = sw_rows.getJSONArray(0); //기본무기 배열
               // Log.e("기본 무기", "1");
                JSONArray sw_rows_tmp = sw_rows_0.getJSONArray(0); //기본무기-변신전 무기배열
                if(sw_rows_tmp.length()>0){
                    tagetView = (LinearLayout) this.findViewById(R.id.weapon_0_0);
                    for(int i=0,m=sw_rows_tmp.length();i<m;i++){
                        JSONObject sw_row = sw_rows_tmp.getJSONObject(i);
                        v = generateWeaponCard(context,sw_row);
                        tagetView.addView(v);
                    }

                }else{
                    ((LinearLayout)this.findViewById(R.id.box_weapon_0_0)).setVisibility(View.GONE);
                }
                sw_rows_tmp = sw_rows_0.getJSONArray(1); //기본무기-변신후 무기배열
                if(sw_rows_tmp.length()>0){
                    tagetView = (LinearLayout) this.findViewById(R.id.weapon_0_1);
                    for(int i=0,m=sw_rows_tmp.length();i<m;i++){
                        JSONObject sw_row = sw_rows_tmp.getJSONObject(i);
                        v = generateWeaponCard(context,sw_row);
                        tagetView.addView(v);
                    }

                }else{
                    ((LinearLayout) this.findViewById(R.id.box_weapon_0_1)).setVisibility(View.GONE);
                }

                JSONArray sw_rows_1 = sw_rows.getJSONArray(1); //추가 기본무기 배열
                sw_rows_tmp = sw_rows_1.getJSONArray(0); //추가무기-변신전 무기배열
                if(sw_rows_tmp.length()>0){
                    tagetView = (LinearLayout) this.findViewById(R.id.weapon_1_0);
                    for(int i=0,m=sw_rows_tmp.length();i<m;i++){
                        JSONObject sw_row = sw_rows_tmp.getJSONObject(i);
                        v = generateWeaponCard(context,sw_row);
                        tagetView.addView(v);
                    }

                }else{
                    ((LinearLayout)this.findViewById(R.id.box_weapon_1_0)).setVisibility(View.GONE);
                }
                sw_rows_tmp = sw_rows_1.getJSONArray(1); //추가무기-변신후 무기배열
                if(sw_rows_tmp.length()>0){
                    tagetView = (LinearLayout) this.findViewById(R.id.weapon_1_1);
                    for(int i=0,m=sw_rows_tmp.length();i<m;i++){
                        JSONObject sw_row = sw_rows_tmp.getJSONObject(i);
                        v = generateWeaponCard(context,sw_row);
                        tagetView.addView(v);
                    }

                }else{
                    ((LinearLayout) this.findViewById(R.id.box_weapon_1_1)).setVisibility(View.GONE);
                }

            }else{
                Log.e("기본 무기","0");
            }



//            //-- 무기 변신 전
//            imgs = (LinearLayout) view.findViewById(R.id.group_unit_weapon_img);
//            texts = (LinearLayout) view.findViewById(R.id.group_unit_weapon);
//            for (int i = 0, m = 3; i < m; i++) {
//                if (!row.isNull("unit_weapon" + (i + 1) + "_img")) {
//                    tmp = row.getString("unit_weapon" + (i + 1) + "_img");
//                    ((NetworkImageView) imgs.getChildAt(i)).setImageUrl(tmp, MySingleton.getInstance(context).getImageLoader());
//                    ((TextView) texts.getChildAt(i)).setText(row.getString("unit_weapon" + (i + 1)));
//                } else {
//                    ((NetworkImageView) imgs.getChildAt(i)).setImageResource(android.R.color.transparent);//초기화
//                    ((TextView) texts.getChildAt(i)).setText("");
//                }
//            }
//
//            //-- 무기 변신 후
//            LinearLayout group_unit_weapon_1 = (LinearLayout) view.findViewById(R.id.group_unit_weapons_1);
//
//            group_unit_weapon_1.setVisibility(unit_is_transform == 1 ? View.VISIBLE : View.GONE);
//            if (unit_is_transform == 1) {
//                imgs = (LinearLayout) view.findViewById(R.id.group_unit_weapon_img_1);
//                texts = (LinearLayout) view.findViewById(R.id.group_unit_weapon_1);
//                for (int i = 0, m = 3; i < m; i++) {
//                    if (!row.isNull("unit_weapon" + (i + 3 + 1) + "_img")) {
//                        tmp = row.getString("unit_weapon" + (i + 3 + 1) + "_img");
//                        ((NetworkImageView) imgs.getChildAt(i)).setImageUrl(tmp, MySingleton.getInstance(context).getImageLoader());
//                        ((TextView) texts.getChildAt(i)).setText(row.getString("unit_weapon" + (i + 3 + 1)));
//                    } else {
//                        ((NetworkImageView) imgs.getChildAt(i)).setImageResource(android.R.color.transparent);//초기화
//                        ((TextView) texts.getChildAt(i)).setText("");
//                    }
//                }
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public View generateWeaponCard(Context context,JSONObject sw_row) throws JSONException {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_weapon_card, null, false);

        //--기본정보
        ((NetworkImageView)v.findViewById(R.id.sw_img)).setImageUrl(sw_row.getString("sw_img"), MySingleton.getInstance(context).getImageLoader());
        ((TextView)v.findViewById(R.id.sw_name)).setText(sw_row.getString("sw_name"));

        //--추가정보, 없을 수 있다.
        String t = "";
        if(!sw_row.isNull("sw_range_type")){
            t+= sw_row.getString("sw_range_type");
        }
        if(!sw_row.isNull("sw_range")){
            t+= "("+sw_row.getString("sw_range")+"m)";
        }
        if(t.length()>0){
            ((TextView)v.findViewById(R.id.sw_range)).setText(t);
        }else{
            ((TextView)v.findViewById(R.id.sw_range)).setVisibility(View.GONE);
        }
        if(!sw_row.isNull("sw_cost")){
            ((TextView)v.findViewById(R.id.sw_cost)).setText("cost" + sw_row.getString("sw_cost"));
        }else{
            ((TextView)v.findViewById(R.id.sw_cost)).setVisibility(View.GONE);
        }
        if(!sw_row.isNull("sw_desc")){
            ((TextView)v.findViewById(R.id.sw_desc)).setText(sw_row.getString("sw_desc"));
        }else{
            ((TextView)v.findViewById(R.id.sw_desc)).setVisibility(View.GONE);
        }

        if(!sw_row.isNull("m_nick")){
            ((TextView)v.findViewById(R.id.m_nick)).setText("edit by : "+sw_row.getString("m_nick"));
        }else{
            ((TextView)v.findViewById(R.id.m_nick)).setVisibility(View.GONE);
        }

        return v;
    }

    public void add_bc_rows(ViewGroup parent,JSONArray bc_rows){
        try {
            Context context = parent.getContext();
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formater = new SimpleDateFormat("yy.M.d H:m");

//                    ViewGroup parent = (ViewGroup) SdgnDetailTabActivity.this.getWindow().getDecorView().getRootView();
            parent.removeAllViews();
            for (int i = 0, m = bc_rows.length(); i < m; i++) {
//                        m_Adapter.add((JSONObject) bc_rows.get(i));
                JSONObject bc_row = (JSONObject) bc_rows.get(i);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View convertView = inflater.inflate(R.layout.bc_row_sdgn_detail_comment, parent, false);
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


                parent.addView(convertView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void load_unit_detail(){
        String url1 = "http://www.mins01.com/sdgn/json/units?unit_idx="+this.unit_idx;
        final SdgnDetailTabActivity thisC = this;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // LinearLayout linearLayout_bc_rows = (LinearLayout) view.findViewById(R.id.linearLayout_bc_rows);
//                    m_Adapter.clear();
                    thisC.row = response.getJSONObject("su_row");
                    thisC.sw_rows = response.getJSONArray("sw_rows");
                    thisC.initSectionsPagerAdapter();
                    thisC.initDetailTop();
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                m_Adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "데이터 로드 에러 : 상세정보 가져오기", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        Log.i("load_unit_detail", "END");
    }
    public void firstLoad(final View view) {
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
                    LinearLayout linearLayout_bc_rows = (LinearLayout) view.findViewById(R.id.linearLayout_bc_rows);
//                    m_Adapter.clear();
                    JSONArray bc_rows = response.getJSONArray("bc_rows");
                    if (bc_rows != null) {
                        tabLayout.getTabAt(0).setText("한마디" + " (" + bc_rows.length() + ")");
                    }
                    add_bc_rows(linearLayout_bc_rows,bc_rows);
                    final ScrollView sv = (ScrollView) findViewById(R.id.scrollView_bc_rows);
                    sv.post(new Runnable() {
                        @Override
                        public void run() {
                            sv.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });


                } catch (Exception e) {
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
        (new AsyncTask<SdgnDetailTabActivity, Void, SdgnDetailTabActivity>() {
            @Override
            protected SdgnDetailTabActivity doInBackground(SdgnDetailTabActivity... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(SdgnDetailTabActivity result) {
                //super.onPostExecute(result);
                //result.firstLoad();
                //initUI();
                load_unit_detail();
                //initDetailTop();
                // firstLoad();

            }

        }).execute(this);
    }
    public void btnClick(View v){
        int id = v.getId();
        switch(id){
            case R.id.btn_write_comment_0:
            case R.id.btn_write_comment:
                showInputDialog();
                break;
        }

    }
    protected void showInputDialog() {
        UserSession usess = UserSession.getInstance(this);
        // get prompts.xml view
        String m_nick = usess.m_nick;
        if(m_nick.length()==0){
            Context context = getBaseContext();
            Toast.makeText(context,"로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            //bc_name.setText("로그인이 필요합니다.");
            return;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.activity_sdgn_detail_bc_rows_form, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final TextView bc_name = (TextView) promptView.findViewById(R.id.bc_name);
        bc_name.setText(m_nick);

        final EditText bc_comment = (EditText) promptView.findViewById(R.id.bc_comment);
        final RatingBar bc_number = (RatingBar) promptView.findViewById(R.id.bc_number);
        bc_number.setStepSize((float)1);

        //키보드 제어용
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("남기기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //resultText.setText("Hello, " + editText.getText());
                        json_bc_write("write",String.valueOf(unit_idx),bc_comment.getText().toString()
                                ,String.valueOf(Math.round(bc_number.getRating())));
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        bc_comment.requestFocus(); //포커스 이동
        //키보드 보이기

        // imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        // imm.showSoftInput(bc_comment, 0);
        bc_comment.postDelayed(new Runnable() {
            @Override
            public void run() {
                bc_comment.requestFocus();
                imm.showSoftInput(bc_comment, 0);
            }
        }, 100);
    }

    public void json_bc_write(String mode,String unit_idx,String bc_comment,String bc_number){
        final UserSession usess = UserSession.getInstance(this);

        final Context context = getBaseContext();

        Map<String,String> postData = new HashMap<>();
        postData.put("bc_idx", "");
        postData.put("enc_m_row",usess.enc_m_row);
        postData.put("bc_comment",bc_comment);
        postData.put("bc_number",bc_number);
        postData.put("mode",mode);

        String url1 = "http://www.mins01.com/mh/bbs_comment/sdgn_units/"+unit_idx;

        LinearLayout linearLayout_bc_rows = (LinearLayout) findViewById(R.id.linearLayout_bc_rows);

        MyHttpRequest jsonObjectRequest = new MyHttpRequest(Request.Method.POST, url1, postData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("onResponse",response);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    LinearLayout linearLayout_bc_rows = (LinearLayout) findViewById(R.id.linearLayout_bc_rows);
                    if(jsonObject != null && linearLayout_bc_rows!=null){
                        if(!jsonObject.isNull("bc_rows")){
                            add_bc_rows(linearLayout_bc_rows, jsonObject.getJSONArray("bc_rows"));
                            final ScrollView sv = (ScrollView) findViewById(R.id.scrollView_bc_rows);
                            sv.post(new Runnable() {
                                @Override
                                public void run() {
                                    sv.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        }
                    }
                    String bc_idx = jsonObject.getString("bc_idx");
                    if(bc_idx!=null){
                        Toast.makeText(context,"한마디 성공", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context, "한마디 실패 : 알 수 없는 에러", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "한마디 실패 : 데이터 로드 에러", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

        Log.i("firstLoad", "END");
    }
}
