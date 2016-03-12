package com.mins01.app001;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mins01 on 2016-03-13.
 */
public class FragmentTest extends Fragment {
    static {
        com.android.volley.VolleyLog.DEBUG = true;
    }

    //private AdView adView;
    private GridRowsAdapter m_Adapter;
    public static final int MY_SOCKET_TIMEOUT_MS = 5000; //5초
    private long backpress_time_ms = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(this.getClass().getName(), "onCreateView");
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(this.getClass().getName(), "onActivityCreated");
        getActivity().setTitle("플레그먼트 테스트");
        super.onActivityCreated(savedInstanceState);
    }

}
