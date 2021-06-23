package com.onenet.datapush.receiver.domain;

import com.onenet.datapush.receiver.api.online.WriteOpe;
import com.onenet.datapush.receiver.config.Config;
import com.onenet.datapush.receiver.entity.Write;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Light {
    private static final Logger LOGGER = LoggerFactory.getLogger(Light.class);

    public void Switch(boolean command) {
        Integer objId = 3311;
        Integer objInstId = 0;
        Integer writeResId = 5850;
        Integer writeMode = 1;

        String authorization = Config.getAuthorization();
        String imei = Config.getImei();

        // Write Resource
        WriteOpe writeOpe = new WriteOpe(authorization);
        Write write = new Write(imei, objId, objInstId, writeMode);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("res_id", writeResId);
        jsonObject.put("val", command);
        jsonArray.put(jsonObject);
        JSONObject data = new JSONObject();
        data.put("data", jsonArray);
        LOGGER.info(writeOpe.operation(write, data).toString());
    }

    public void TurnOn() {
        Switch(true);
    }

    public void TurnOff() {
        Switch(false);
    }
}

