package com.onenet.datapush.receiver.domain;

import com.onenet.datapush.receiver.utils.HttpSendCenter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Light {
    @Value("${api.domain}")
    private String domain;
    @Value("${api.authorization}")
    private String authorization;
    @Value("${light.imei}")
    private String imei;
    @Value("${light.objId}")
    private Integer objId;
    @Value("${light.objInstId}")
    private Integer objInstId;
    @Value("${light.writeResId}")
    private Integer writeResId;
    @Value("${light.writeMode}")
    private Integer writeMode;
    @Value("${illumi-threshold.max}")
    private float thresholdMax;
    @Value("${illumi-threshold.min}")
    private float thresholdMin;

    private static final Logger LOGGER = LoggerFactory.getLogger(Light.class);

    /**
     * 开灯或者关灯
     * @param command 命令. true-开; false-关
     */
    public void switchLight(boolean command) {
        String url = getApiUrl();
        JSONObject body = getApiBody(command);
        HttpSendCenter.post(this.authorization, url, body);
    }

    /**
     * 开灯
     */
    public void turnOn() {
        switchLight(true);
    }

    /**
     * 关灯
     */
    public void turnOff() {
        switchLight(false);
    }

    /**
     * 构造"即时命令-写设备资源"的URL
     * https://open.iot.10086.cn/doc/iot_platform/book/api/LwM2M-IPSO/Real-API/5rt-write-device-resources.html
     * @return URL
     */
    public String getApiUrl() {
        StringBuilder url = new StringBuilder(this.domain);
        url.append("/lwm2m-online?action=write&imei=").append(this.imei);
        url.append("&obj_id=").append(this.objId);
        url.append("&obj_inst_id=").append(this.objInstId);
        url.append("&mode=").append(this.writeMode);
        return url.toString();
    }

    /**
     * 构造"即时命令-写设备资源"的Body
     * @param command 命令. true-开; false-关
     * @return Body
     */
    public JSONObject getApiBody(boolean command) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("res_id", writeResId);
        jsonObject.put("val", command);
        jsonArray.put(jsonObject);
        JSONObject body = new JSONObject();
        body.put("data", jsonArray);
        return body;
    }

    public float getThresholdMax() {
        return this.thresholdMax;
    }

    public float getThresholdMin() {
        return this.thresholdMin;
    }
}

