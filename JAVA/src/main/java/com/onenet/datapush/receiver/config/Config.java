package com.onenet.datapush.receiver.config;

import com.onenet.datapush.receiver.exception.NBStatus;
import com.onenet.datapush.receiver.exception.OnenetNBException;

import java.io.IOException;
import java.util.Properties;

public class Config {
    public static String domainName;
    public static String authorization;
    public static String imei;
    public static float maxValue;
    public static float minValue;

    static {
        Properties properties = new Properties();
        try {
            properties.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
            domainName = (String)properties.get("domainName");
            authorization = (String)properties.get("authorization");
            imei = (String)properties.get("imei");
            maxValue = (float)properties.get("maxValue");
            minValue = (float)properties.get("minValue");
        } catch (IOException e) {
            throw new OnenetNBException(NBStatus.LOAD_CONFIG_ERROR);
        }
    }

    public static String getDomainName() {
        return domainName;
    }

    public static String getAuthorization() {
        return authorization;
    }

    public static String getImei() {
        return imei;
    }

    public static float getMaxValue() {
        return maxValue;
    }

    public static float getMinValue() {
        return minValue;
    }
}
