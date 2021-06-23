package com.onenet.datapush.receiver.api.online;

import com.onenet.datapush.receiver.entity.CommonEntity;
import com.onenet.datapush.receiver.utils.HttpSendCenter;
import okhttp3.Callback;
import org.json.JSONObject;

public class WriteOpe extends BasicOpe{

    public WriteOpe(String authorization) {
        super(authorization);
    }

    @Override
    public JSONObject operation(CommonEntity commonEntity, JSONObject body) {
        return HttpSendCenter.post(authorization, commonEntity.toUrl(), body);
    }

    @Override
    public void operation(CommonEntity commonEntity, JSONObject body, Callback callback) {
        HttpSendCenter.postAsync(authorization, commonEntity.toUrl(), body, callback);
    }
}
