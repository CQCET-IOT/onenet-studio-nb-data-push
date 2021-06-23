package com.onenet.datapush.receiver;

import com.onenet.datapush.receiver.utils.Util;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 数据接收程序接口类
 *
 * Created by Roy on 2017/5/17.
 * Updated by wjl on 2020/6/10.
 *
 */
@SpringBootApplication
public class ReceiverDemo {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ReceiverDemo.class, args);
    }
}
