package com.mins01.app001;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;

/**
 * JSON과 이미지 캐싱 가능 통신용
 */
public class MySingleton {
    private static Context context;
    private static MySingleton instacne = null;
    private RequestQueue requestQueue = null;
    private MyImageLoader imageLoader;


    private MySingleton(Context i_context) {
        context = i_context;
        //int maxMemorySize = MyBitmapLruCache.getMaxCacheSize();
//        int maxMemorySize = 2*1024*1024; //
//        int maxDiskSize = 5*1024*1024;
        //Log.i("getFilesDir()",context.getFilesDir().getAbsolutePath());
//        File cacheDir = context.getCacheDir();
        File cacheDir = context.getFilesDir();
//        imageLoader = new MyImageLoader(getRequestQueue(),new MyBitmapLruCache(maxMemorySize,cacheDir,maxDiskSize),true); //이미지 강제캐싱한다.
        //imageLoader = new MyImageLoader(getRequestQueue(),new DiskBitmapCache(cacheDir),true); //이미지 강제캐싱한다.
        imageLoader = new MyImageLoader(getRequestQueue(), new BitmapLruCache(), true); //이미지 강제캐싱한다.
        //-- 강제 캐싱용 설정값
        MyHttpHeaderParser.cacheExpired_sec = 30 * 60 * 1000; //강제 캐싱 msec
        MyHttpHeaderParser.cacheExpired_sec = 24 * 60 * 60 * 1000; //강제 캐싱 msec
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (instacne == null) {
            instacne = new MySingleton(context);
        }
        return instacne;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network, 2);
            requestQueue.start();
            //requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void stop() {
        Log.i("MySingleton", "stop");
        getRequestQueue().stop();
    }

    public void start() {
        getRequestQueue().start();
        Log.i("MySingleton", "start");
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
