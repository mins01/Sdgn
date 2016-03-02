package com.mins01.app001;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;

/**
 * MyBitmapLruCache
 * LruCache + DiskBasdeCache
 */
public class MyBitmapLruCache extends LruCache<String, Bitmap>
        implements ImageLoader.ImageCache {
    int appVersion = 100;
    private DiskBitmapCache diskBitmapCache;
    private File cacheDir;

    static int getMaxCacheSize() {
        int maxMemory = (int) ((Runtime.getRuntime().maxMemory())/1024);
        return maxMemory / 8;
    }

    public MyBitmapLruCache(File cacheDir, int maxDiskSize) {
        super(getMaxCacheSize());
        initDiskCache(cacheDir, maxDiskSize);
    }
    public MyBitmapLruCache(int maxMemorySize, File cacheDir,int maxDiskSize ) {
        super(maxMemorySize);
        initDiskCache(cacheDir, maxDiskSize);
    }

    private void initDiskCache(File cacheDir,int maxDiskSize){
        this.cacheDir = cacheDir;
        diskBitmapCache = new DiskBitmapCache(cacheDir, maxDiskSize);
        logFilesFromDiskCache();
    }
    private void logFilesFromDiskCache(){
        File[] files = cacheDir.listFiles();
        Log.w("bitmaplogFiles", String.valueOf(files.length));
//        if(files != null){
//            for (File file : files) {
//                Log.w("logFilesFromDiskCache", file.getName());
//            }
//        }
    }


    @Override
    protected int sizeOf(String key, Bitmap value) {
        return getSizeInBytes(value)/1024;
    }

    public static int getSizeInBytes(Bitmap bitmap) {
        if (android.os.Build.VERSION.SDK_INT>=12) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }


    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = get(url);
        if(bitmap == null ){
            Log.i("getBitmap:메모리캐시에 없음", url);
            bitmap = diskBitmapCache.getBitmap(url);

            if(bitmap==null){
                Log.i("getBitmap:디스크케시에 없음", url);
            }else{
                Log.i("getBitmap:디스크->메모리", url);
                this.put(url, bitmap);
            }
        }else{
            Log.i("getBitmap:메모리캐시에 있음", url);
        }
        return bitmap;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
        Log.i("putBitmap:메모리케시에 저장", url);
        diskBitmapCache.putBitmap(url, bitmap);
        Log.i("putBitmap:디스크케시에 저장", url);
        logFilesFromDiskCache();
    }
}