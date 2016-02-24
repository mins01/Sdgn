package com.mins01.sdgn;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by mins01 on 2016-02-24.
 */
public class MySingleton {
    private static Context context;
    private static MySingleton instacne = null;
    private RequestQueue requestQueue =null;
    private ImageLoader imageLoader;


    private MySingleton(Context i_context){
        context = i_context;
        imageLoader = new ImageLoader(getRequestQueue(),new BitmapLruCache());

    }
    public static synchronized MySingleton getInstance(Context context){
        if(instacne==null){
            instacne = new MySingleton(context);
        }
        return instacne;
    }
    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            Cache cache = new DiskBasedCache(context.getCacheDir(),1024*1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache,network,2);
            requestQueue.start();
            //requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
    public ImageLoader getImageLoader(){
        return imageLoader;
    }


}
