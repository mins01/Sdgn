package com.mins01.app001;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * HttpHeaderParser 를 상속받아서 강제캐싱용 파서를 추가해서 사용.
 */
public class MyHttpHeaderParser extends HttpHeaderParser {
    public static long cacheHitButRefreshed_sec = 600000;
    public static long cacheExpired_sec = 86400000;
    /**
     * Extracts a {@link Cache.Entry} from a {@link NetworkResponse}.
     * Cache-control headers are ignored. SoftTtl == 10 mins, ttl == 24 hours.
     * @param response The network response to parse headers from
     * @return a cache entry for the given response, or null if the response is not cacheable.
     */
    public static Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response) {
        long now = System.currentTimeMillis();

        Map<String, String> headers = response.headers;
        long serverDate = 0;
        String serverEtag;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

//        final long cacheHitButRefreshed = 10 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
//        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
        final long cacheHitButRefreshed = MyHttpHeaderParser.cacheHitButRefreshed_sec; // in 3 minutes cache will be hit, but also refreshed on background
        final long cacheExpired = MyHttpHeaderParser.cacheExpired_sec; // in 24 hours this cache entry expires completely
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;

        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;
    }
}
