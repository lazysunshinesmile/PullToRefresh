package com.xiangsun.pullToRefreshSun.Util;

import java.io.IOException;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpConnect {

    private final static OkHttpClient client = new OkHttpClient();

    /**
     * 增加头部
     * @param headers
     * @return
     */
    public static Headers addHeader(Map<String, String> headers) {
        return Headers.of(headers);

    }

    /**
     * 发送get请求
     *
     * @param url   get请求URL地址
     * @param headers   头部信息
     * @return
     * @throws IOException
     */
    public static Response getResp(String url, Map<String, String> headers) throws IOException {
        Request request = null;
        if(headers != null)
            request = new Request.Builder()
                    .url(url)
                    .headers(addHeader(headers))
                    .build();
        else {
            request = new Request.Builder()
                    .url(url)
                    .build();
        }

        Response response = client.newCall(request).execute();
        return response;
    }
}
