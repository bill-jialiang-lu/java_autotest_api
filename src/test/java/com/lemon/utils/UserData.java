package com.lemon.utils;

import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;

import java.util.HashMap;
import java.util.Map;

public class UserData {



    public static Map<String,Object> VARS  = new HashMap<>();

    public static Map<String,String> DEFAULT_HEADERS  = new HashMap<>();

    static {
        DEFAULT_HEADERS.put("X-Lemonban-Media-Type","lemonban.v2");
        DEFAULT_HEADERS.put("Content-Type","application/json");

        //添加随机手机号码
        VARS.put("${register_mb}", ChineseMobileNumberGenerator.getInstance().generate());
//        VARS.put("${register_mb}", "15162221060");
        VARS.put("${register_pwd}","12345678");
        VARS.put("${amount}","5000");
    }
}
