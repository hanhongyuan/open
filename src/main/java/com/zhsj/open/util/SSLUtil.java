package com.zhsj.open.util;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lcg on 16/12/16.
 */
public class SSLUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SSLUtil.class);

    /**
     * SSL 的 GET请求
     *
     * @param url
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getSSL(String url) throws KeyStoreException, NoSuchAlgorithmException, URISyntaxException, IOException {
        URL urll = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) urll.openConnection();
        connection.setDoOutput(true); // true for POST, false for GET
        connection.setDoInput(true);
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setConnectTimeout(180000);// 连接超时时间
        connection.setReadTimeout(180000);
        OutputStream outputStream = null;
        StringBuilder respData = new StringBuilder();
        try {
            connection.connect();

            // //提交数据完成后，收取数据
            if (connection.getResponseCode() == 200) {
                // 读取post之后的返回值
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    respData.append(line + "\r\n");
                }
                in.close();
            } else {
                LOG.error("#SSLUtil.httpsPost# post ResponseCode={},ResponseMessage={}" ,connection.getResponseCode(),connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                connection.disconnect(); // 断开连接
            } catch (Exception e) {
                throw e;
            }
        }
        return respData.toString();
    }

    public static String postSSL(String url,String json) throws KeyStoreException, NoSuchAlgorithmException, URISyntaxException, IOException {
        URL urll = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) urll.openConnection();
        connection.setDoOutput(true); // true for POST, false for GET
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setConnectTimeout(180000);// 连接超时时间
        connection.setReadTimeout(180000);
        OutputStream outputStream = null;
        StringBuilder respData = new StringBuilder();
        try {
            outputStream = connection.getOutputStream();
            outputStream.write(json.getBytes("utf-8"));
            outputStream.flush();

            // //提交数据完成后，收取数据
            if (connection.getResponseCode() == 200) {
                // 读取post之后的返回值
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    respData.append(line + "\r\n");
                }
                in.close();
            } else {
                LOG.error("#SSLUtil.httpsPost# post ResponseCode={},ResponseMessage={}" ,connection.getResponseCode(),connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                connection.disconnect(); // 断开连接
            } catch (Exception e) {
                throw e;
            }
        }
        return respData.toString();
    }

    
}
