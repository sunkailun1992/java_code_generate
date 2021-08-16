package com.code.utils;

import java.io.*;
import java.util.Properties;

public class PropertiesUtil extends Properties {
    private static PropertiesUtil propertiesUtil = new PropertiesUtil();

    private PropertiesUtil() {
    }

    public static PropertiesUtil getMySqlProperties(String path) {
        try {
            propertiesUtil.load(PropertiesUtil.getInputStreamReader(path + "src/main/resources/templates/config/mysql.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertiesUtil;
    }

    public static PropertiesUtil getRedisProperties(String path) {
        try {
            propertiesUtil.load(PropertiesUtil.getInputStreamReader(path + "src/main/resources/templates/config/redis.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertiesUtil;
    }

    public static BufferedReader getInputStreamReader(String path) {
        try {
            // 使用BufferedReader流读取properties文件
            return new BufferedReader(new FileReader(path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

