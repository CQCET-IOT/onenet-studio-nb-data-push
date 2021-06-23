package com.onenet.datapush.receiver.utils;

import com.onenet.datapush.receiver.exception.NBStatus;
import com.onenet.datapush.receiver.exception.OnenetNBException;
import okhttp3.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class HttpSendCenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSendCenter.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .build();
    //
    public static JSONObject get(String authorization, String url) {
        Request request = new Request.Builder()
                .header("authorization", authorization)
                .url(url)
                .build();
        return handleRequest(request);
    }

    public static void getAsync(String authorization, String url, Callback callback) {
        Request request = new Request.Builder()
                .header("authorization", authorization)
                .url(url)
                .build();
        handleAsyncRequest(request, callback);
    }

    public static JSONObject post(String authorization, String url, JSONObject body) {
        RequestBody requestBody = RequestBody.create(JSON, body.toString());
        Request request = new Request.Builder()
                .url(url)
                .header("authorization", authorization)
                .post(requestBody)
                .build();
        return handleRequest(request);
    }
    public static JSONObject postNotBody(String authorization, String url) {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, new JSONObject().toString()))
                .header("authorization", authorization)
                .build();
        return handleRequest(request);
    }

    public static void postAsync(String authorization, String url, JSONObject body, Callback callback) {
        RequestBody requestBody = RequestBody.create(JSON, body.toString());
        Request request = new Request.Builder()
                .url(url)
                .header("authorization", authorization)
                .post(requestBody)
                .build();
        handleAsyncRequest(request, callback);
    }
    public static void postNotBodyAsync(String authorization, String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, new JSONObject().toString()))
                .header("authorization", authorization)
                .build();
        handleAsyncRequest(request, callback);
    }

    public static JSONObject put(String authorization, String url) {

        return null;
    }

    public static void putAsync(String authorization, String url, JSONObject body, Callback callback) {

    }

    public static JSONObject delete(String authorization, String url) {

        return null;
    }

    public static void deleteAsync(String authorization, String url, Callback callback) {

    }

    private static void handleAsyncRequest(Request request, Callback callback) {
        httpClient.newCall(request).enqueue(callback);
    }

    private static JSONObject handleRequest(Request request) {
        try {
            Response response = httpClient.newCall(request).execute();
            if (response != null) {
                String st = new String(response.body().bytes(), "utf-8");
                return new JSONObject(st);
            }else {
                throw new OnenetNBException(NBStatus.HTTP_REQUEST_ERROR);
            }
        } catch (IOException e) {
            LOGGER.info("http request error::{}", e.getMessage());
            throw new OnenetNBException(NBStatus.HTTP_REQUEST_ERROR);
        }
    }
}

