package com.poho.stuup.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 *
 */
public class HttpJSONUtil {
    private HttpClient httpClient = null;
    private HttpPost method = null;

    /**
     * 接口地址
     *
     * @param url
     */
    public HttpJSONUtil(String url) {
        httpClient = new DefaultHttpClient();
        method = new HttpPost(url);
    }

    /**
     * 调用 API
     *
     * @param parameters
     * @return
     */
    public String post(String parameters, JSONObject headParam) {
        String body = null;
        if (method != null & parameters != null && !"".equals(parameters.trim())) {
            try {
                // 建立一个NameValuePair数组，用于存储欲传送的参数
                method.addHeader("Content-type", "application/json; charset=utf-8");
                method.setHeader("Accept", "application/json");
                method.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));

                if (headParam != null) {
                    for (String key : headParam.keySet()) {
                        method.addHeader(key, headParam.get(key).toString());
                    }
                }

                HttpResponse response = httpClient.execute(method);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                }
                String result = EntityUtils.toString(response.getEntity());
                body = JSONObject.parse(result).toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpClient != null) {
                    httpClient.getConnectionManager().shutdown();// 最后关掉链接。
                    httpClient = null;
                }
            }
        }
        return body;
    }
}