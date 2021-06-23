package com.onenet.datapush.receiver.api.online;

import com.onenet.datapush.receiver.entity.CommonEntity;
import org.json.JSONObject;
import okhttp3.Callback;

public abstract class BasicOpe {
    protected String authorization;

    public BasicOpe(String authorization) {
        this.authorization = authorization;
    }

    public abstract JSONObject operation(CommonEntity commonEntity, JSONObject body);

    public abstract void operation(CommonEntity commonEntity, JSONObject body, Callback callback);
}
