package com.mins01.app001;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

/**
 * 강제 캐싱 가능하도록 상속 받아서 수정.
 */
public class MyImageLoader extends ImageLoader {
    // 강제 캐싱 설정 관련. true면 강제로 캐싱한다.
    protected boolean forced_cache = false;
    /**
     * Constructs a new ImageLoader.
     *
     * @param queue      The RequestQueue to use for making image requests.
     * @param imageCache The cache to use as an L1 cache.
     */
    public MyImageLoader(RequestQueue queue, ImageCache imageCache, boolean _forced_cache) {
        super(queue, imageCache);
        this.forced_cache = _forced_cache;
    }
    public MyImageLoader(RequestQueue queue, ImageCache imageCache) {
        super(queue, imageCache);
    }
    @Override
    protected Request<Bitmap> makeImageRequest(String requestUrl, int maxWidth, int maxHeight,
                                               ImageView.ScaleType scaleType, final String cacheKey) {
        final MyImageRequest myImageRequest = new MyImageRequest(requestUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                onGetImageSuccess(cacheKey, response);
            }
        }, maxWidth, maxHeight, scaleType, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onGetImageError(cacheKey, error);
            }
        });
        myImageRequest.forced_cache = this.forced_cache; //강제 캐싱 설정.
        return myImageRequest;
    }
}
