package com.zhsj.open.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lcg on 16/12/31.
 */
public class MtConfig {


    private static Properties properties;
    static {
        properties = new Properties();
        try {
            InputStream stream = MtConfig.class.getClassLoader().getResourceAsStream("config.properties");
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key,String defaultValue){
        return properties.getProperty(key,defaultValue);
    }

    public static void main(String[] args){
        System.out.println(MtConfig.getProperty("weChat_appId", ""));
    }
}
