package com.mins01.app001;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SdgnDetailTabActivity extends AppCompatActivity {
    private JSONObject row;
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
        unit_idx = getIntent().getIntExtra("unit_idx",0);
        row = unitrow.getRowByUnitIdx(unit_idx);

        try {
            setTitle("유닛상세 - "+row.getString("unit_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        firstAction();
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
        public static SdgnDetailTabActivity thisC ;

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
            switch(getArguments().getInt(ARG_SECTION_NUMBER)){
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
        public void onActivityCreated(Bundle savedInstanceState){
            if(savedInstanceState != null) {
                for(String key : savedInstanceState.keySet()) {
                    Log.i("savedInstanceState : ", key);
                }
            }
            switch(getArguments().getInt(ARG_SECTION_NUMBER)){
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
    public void initDetailTop(){
        UnitCardHolder unitCardHolder = new UnitCardHolder();
        View convertView = this.findViewById(R.id.activity_sdgn_detail_top);
        unitCardHolder.setMemberVar(convertView);
        unitCardHolder.setValues(row, convertView);
        Context context = this.getApplicationContext();

        try {
            int unit_is_transform= row.getInt("unit_is_transform");
            //-- 상단 부분
            NetworkImageView imageView_unit_anime_img = (NetworkImageView) this.findViewById(R.id.imageView_unit_anime_img);
            imageView_unit_anime_img.setImageUrl(row.getString("unit_anime_img"), MySingleton.getInstance(context).getImageLoader());
            ((TextView) this.findViewById(R.id.textView_unit_anime)).setText(row.getString("unit_anime"));
            ((TextView) this.findViewById(R.id.textView_unit_txt)).setText(row.getString("unit_txt"));
            String[] x = new String[]{row.getString("unit_movetype"),unit_is_transform==1?"변신가능":"변신불가"};

            ((TextView) this.findViewById(R.id.textView_bc_properties)).setText(android.text.TextUtils.join("/", x));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initDetailContent(View view) {
        Log.d("initDetailContent", "Start");
        final Context context = view.getContext();
        try {
            int unit_is_transform= row.getInt("unit_is_transform");
            //-- 내용 부분
            String tmp;
            LinearLayout imgs, texts;

            //-- 스킬
            NetworkImageView niv = null;
            TextView tv = null;
            final int[] niv_ids = {R.id.imageView_skill1_img,R.id.imageView_skill2_img,R.id.imageView_skill3_img,R.id.imageView_skill4_img};
            final int[] tv_ids = {R.id.imageView_skill1,R.id.imageView_skill2,R.id.imageView_skill3,R.id.imageView_skill4};
            for (int i = 0, m = 4; i < m; i++) {
                if (!row.isNull("unit_skill" + (i + 1) + "_img")) {
                    tmp = row.getString("unit_skill" + (i + 1) + "_img");

                    ((NetworkImageView) view.findViewById(niv_ids[i])).setImageUrl(tmp, MySingleton.getInstance(context).getImageLoader());
                    if(i==3){
                        ((TextView) view.findViewById(tv_ids[i])).setText("(변신 후)\n[" + row.getString("unit_skill" + (i + 1)) + "]\n" + row.getString("unit_skill" + (i + 1) + "_desc"));
                    }else{
                        ((TextView) view.findViewById(tv_ids[i])).setText("[" + row.getString("unit_skill" + (i + 1)) + "]\n" + row.getString("unit_skill" + (i + 1) + "_desc"));
                    }

                } else {
                    ((NetworkImageView) view.findViewById(niv_ids[i])).setImageResource(android.R.color.transparent);//초기화
                    ((TextView) view.findViewById(tv_ids[i])).setText("");
                }
            }


            //-- 무기 변신 전
            imgs = (LinearLayout) view.findViewById(R.id.group_unit_weapon_img);
            texts = (LinearLayout) view.findViewById(R.id.group_unit_weapon);
            for (int i = 0, m = 3; i < m; i++) {
                if (!row.isNull("unit_weapon" + (i + 1) + "_img")) {
                    tmp = row.getString("unit_weapon" + (i + 1) + "_img");
                    ((NetworkImageView) imgs.getChildAt(i)).setImageUrl(tmp, MySingleton.getInstance(context).getImageLoader());
                    ((TextView) texts.getChildAt(i)).setText(row.getString("unit_weapon" + (i + 1)));
                } else {
                    ((NetworkImageView) imgs.getChildAt(i)).setImageResource(android.R.color.transparent);//초기화
                    ((TextView) texts.getChildAt(i)).setText("");
                }
            }

            //-- 무기 변신 후
            LinearLayout group_unit_weapon_1 = (LinearLayout)  view.findViewById(R.id.group_unit_weapons_1);

            group_unit_weapon_1.setVisibility(unit_is_transform==1?View.VISIBLE:View.GONE);
            if(unit_is_transform==1){
                imgs = (LinearLayout) view.findViewById(R.id.group_unit_weapon_img_1);
                texts = (LinearLayout) view.findViewById(R.id.group_unit_weapon_1);
                for (int i = 0, m = 3; i < m; i++) {
                    if (!row.isNull("unit_weapon" + (i+3 + 1) + "_img")) {
                        tmp = row.getString("unit_weapon" + (i+3 + 1) + "_img");
                        ((NetworkImageView) imgs.getChildAt(i)).setImageUrl(tmp, MySingleton.getInstance(context).getImageLoader());
                        ((TextView) texts.getChildAt(i)).setText(row.getString("unit_weapon" + (i+3 + 1)));
                    } else {
                        ((NetworkImageView) imgs.getChildAt(i)).setImageResource(android.R.color.transparent);//초기화
                        ((TextView) texts.getChildAt(i)).setText("");
                    }
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }





    public void firstLoad(final View view) {
        String url1 = "http://www.mins01.com/mh/bbs_comment/sdgn_units/";
        try{
            url1 +=row.getString("unit_idx");
        }catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    LinearLayout linearLayout_bc_rows = (LinearLayout) view.findViewById(R.id.linearLayout_bc_rows);
//                    m_Adapter.clear();
                    JSONArray bc_rows = response.getJSONArray("bc_rows");
                    if(bc_rows!=null){
                        tabLayout.getTabAt(0).setText("한마디"+" ("+bc_rows.length()+")");
                    }


//                    Context context = SdgnDetailTabActivity.this.getApplicationContext();
                    Context context = view.getContext();


                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat formater = new SimpleDateFormat("yy.M.d H:m");

//                    ViewGroup parent = (ViewGroup) SdgnDetailTabActivity.this.getWindow().getDecorView().getRootView();
                    for (int i = 0, m = bc_rows.length(); i < m; i++) {
//                        m_Adapter.add((JSONObject) bc_rows.get(i));
                        JSONObject bc_row = (JSONObject) bc_rows.get(i);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View convertView = inflater.inflate(R.layout.bc_row_sdgn_detail_comment, linearLayout_bc_rows, false);
                        ((TextView)convertView.findViewById(R.id.textView_bc_name)).setText(bc_row.getString("bc_name"));
                        Date d = null;
                        try {
                            d = parser.parse(bc_row.getString("bc_insert_date"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        ((TextView)convertView.findViewById(R.id.textView_bc_insert_date)).setText(formater.format(d));
                        ((TextView)convertView.findViewById(R.id.textView_bc_comment)).setText(bc_row.getString("bc_comment"));

                        int bc_number = bc_row.getInt("bc_number");
                        String bc_number_str = "";
                        switch(bc_number){
                            case 0:break;
                            case 1:bc_number_str="★☆☆☆☆";break;
                            case 2:bc_number_str="★★☆☆☆";break;
                            case 3:bc_number_str="★★★☆☆";break;
                            case 4:bc_number_str="★★★★☆";break;
                            case 5:bc_number_str="★★★★★";break;

                        }
                        TextView textView_bc_number = (TextView)convertView.findViewById(R.id.textView_bc_number);
                        if(bc_number==0){
                            textView_bc_number.setVisibility(View.GONE);
                        }else{
                            textView_bc_number.setVisibility(View.VISIBLE);
                            textView_bc_number.setText(bc_number_str);
                        }



                        linearLayout_bc_rows.addView(convertView);
                    }
//                    Toast.makeText(getApplicationContext(), "데이터 로드 완료 : " + bc_rows.length(), Toast.LENGTH_SHORT).show();
                    //SdgnListsActivity.this.setTitle(SdgnListsActivity.this.getTitle() + " - 총 " + m_Adapter.getCount() + "유닛");
//                    Log.i("JSON LOAD",response.toString());
                }  catch (Exception e){
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
                initDetailTop();
                // firstLoad();
            }

        }).execute(this);
    }
}
