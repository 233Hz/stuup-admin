package com.poho.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @Author: wupeng
 * @Description: HttpClient请求类
 * @Date: Created in 23:38 2018/7/20
 * @Modified By:
 */
public class HttpClientUtil {
    /**
     * 调用 API
     *
     * @param url
     * @return
     */
    public JSONObject get(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        JSONObject body = null;
        try {
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            httpGet.addHeader("Content-type", "application/json; charset=utf-8");
            httpGet.setHeader("Accept", "application/json");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_ACCEPTED) {
                String result = EntityUtils.toString(response.getEntity());
                body = JSONObject.parseObject(result);
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }

    /**
     * 调用 API
     *
     * @param parameters
     * @return
     */
    public String post(String url, String parameters, JSONObject headParam) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        String body = null;
        try {
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));

            if (headParam != null) {
                for (String key : headParam.keySet()) {
                    httpPost.addHeader(key, headParam.get(key).toString());
                }
            }

            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_ACCEPTED) {
                String result = EntityUtils.toString(response.getEntity());
                body = JSONObject.parse(result).toString();
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body;
    }
}