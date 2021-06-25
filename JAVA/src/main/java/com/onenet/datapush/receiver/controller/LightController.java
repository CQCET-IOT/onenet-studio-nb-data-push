package com.onenet.datapush.receiver.controller;

import com.onenet.datapush.receiver.ReceiverDemo;
import com.onenet.datapush.receiver.domain.Light;
import com.onenet.datapush.receiver.utils.Util;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class LightController {
    @Autowired
    Light light;

    @Value("${http-push.token}")
    private String token;
    @Value("${http-push.aesKey}")
    private String aeskey;

    private static Logger logger = LoggerFactory.getLogger(ReceiverDemo.class);
    /**
     * 功能描述：第三方平台数据接收。<p>
     * <ul>注:
     *   <li>1.OneNet平台为了保证数据不丢失，有重发机制，如果重复数据对业务有影响，数据接收端需要对重复数据进行排除重复处理。</li>
     *   <li>2.OneNet每一次post数据请求后，等待客户端的响应都设有时限，在规定时限内没有收到响应会认为发送失败。
     *   接收程序接收到数据时，尽量先缓存起来，再做业务逻辑处理。</li>
     * </ul>
     * @param body 数据消息
     * @return 任意字符串。OneNet平台接收到http 200的响应，才会认为数据推送成功，否则会重发。
     */
    @PostMapping("/receive")
    public String receive(@RequestBody String body) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        //logger.info("data receive:  body String --- " +body);

        /************************************************
         *  解析数据推送请求，非加密模式。
         *  如果是明文模式使用以下代码
         **************************************************/
        /*************明文模式  start****************/
        Util.BodyObj obj = Util.resolveBody(body);
        if (obj != null){
            boolean dataRight = Util.checkSignature(obj, token);
            if (dataRight){
                logger.info("data receive: " + obj.toString());

                /* 解析光照度，根据光照度的值调用LED控制API */
                try {
                    JSONObject object = new JSONObject(obj.toString());
                    JSONObject illumi = getIlluminance(object);
                    if (illumi != null) {
                        float value = illumi.getFloat("value");
                        logger.info("illuminance value: " + value);
                        if (value > light.getThresholdMax()) {
                            // 调用写资源API关闭LED灯
                            light.turnOff();
                        } else if (value < light.getThresholdMin()) {
                            // 调用写资源API打开LED灯
                            light.turnOn();
                        }
                    }
                }
                catch (Exception ex) {
                }
            } else {
                logger.info("data receive: signature error");
            }

        } else {
            logger.info("data receive: body empty error");
        }
        /*************明文模式  end****************/


        /********************************************************
         *  解析数据推送请求，加密模式
         *
         *  如果是加密模式使用以下代码
         ********************************************************/
        /*************加密模式  start****************/
//        Util.BodyObj obj1 = Util.resolveBody(body);
//        if (obj1 != null){
//            boolean dataRight1 = Util.checkSignature(obj1, token);
//            if (dataRight1){
//                String msg = Util.decryptMsg(obj1.getMsg(), aeskey.getBytes());
//                logger.info("data receive: content" + msg);
//            }else {
//                logger.info("data receive:  signature error " );
//            }
//        }else {
//            logger.info("data receive: body empty error" );
//        }
        /*************加密模式  end****************/
        return "ok";
    }

    /**
     * 功能说明： URL&Token验证接口。如果验证成功返回msg的值，否则返回其他值。
     * @param msg 验证消息
     * @param nonce 随机串
     * @param signature 签名
     * @return msg值
     */
    @GetMapping("/receive")
    public String check(@RequestParam(value = "msg") String msg,
                        @RequestParam(value = "nonce") String nonce,
                        @RequestParam(value = "signature") String signature) throws UnsupportedEncodingException {

        logger.info("url&token check: msg:{} nonce:{} signature:{}",msg,nonce,signature);
        logger.info("local token:{},aesKey:{}", token, aeskey);
        if (Util.checkToken(msg, nonce, signature, token)){
            return msg;
        } else {
            return "error";
        }
    }

    /**
     * 从推送消息中获取光照对象
     * @param object
     * @return
     */
    public JSONObject getIlluminance(JSONObject object) {
        try {
            JSONObject msg = object.getJSONObject("msg");
            if (msg != null) {
                JSONObject data = msg.getJSONObject("data");
                if (data != null) {
                    JSONObject params = data.getJSONObject("params");
                    if (params != null) {
                        JSONObject illumi = params.getJSONObject("3301_0_5700");
                        return  illumi;
                    }
                }
            }
        }
        catch (Exception ex) {
        }

        return null;
    }
}
