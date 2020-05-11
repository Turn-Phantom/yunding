package com.yunding.server.common.utils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @desc
 * @date 2020-04-04
 */
public class HttpClientUtils {
    // 创建默认的httpClient实例.
    private static CloseableHttpClient httpclient = HttpClients.createDefault();
    /**
     * @desc http请求
     * @param 
     * @return
     * @date 2020-04-04
     */
    public static HttpResponse httpPost(String reqUrl, Map<String, String> params, Header[] headers) throws IOException {
        // 创建httppost
        HttpPost httpPost = new HttpPost(reqUrl);
        if(headers != null){
            httpPost.setHeaders(headers);
        }
        // 创建参数队列
        List<NameValuePair> formParams = new ArrayList<>();
        if(params.size() > 0){
            for (Map.Entry<String, String> param : params.entrySet()) {
                formParams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
        }
        UrlEncodedFormEntity uefEntity;
        uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
        httpPost.setEntity(uefEntity);
        return httpclient.execute(httpPost);
    }

    /**
     * http get请求
     * @param reqUrl
     * @return
     * @throws IOException
     */
    public static HttpResponse httpGet(String reqUrl, Header[] headers) throws IOException {
        HttpGet httpGet = new HttpGet(reqUrl);
        if(headers != null){
            httpGet.setHeaders(headers);
        }
        return httpclient.execute(httpGet);
    }
}
