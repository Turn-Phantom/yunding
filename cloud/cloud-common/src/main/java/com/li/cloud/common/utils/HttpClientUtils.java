package com.li.cloud.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @desc
 * @date 2020-04-04
 */
public class HttpClientUtils {

    private static final int TIMEOUT = 10000;
    // 创建默认的httpClient实例.
    private static CloseableHttpClient httpclient = HttpClients.createDefault();

    private static CloseableHttpClient visitRecordClient;
    // 创建httppost
    private static HttpPost visitRecordPost = new HttpPost();

    static {
        // 连接池管理
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(3000); // 最大连接数
        connectionManager.setDefaultMaxPerRoute(500);//例如默认每路由最高50并发，具体依据业务来定
        // 请求超时配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT).setSocketTimeout(TIMEOUT).build();
        // 保持连接策略
        ConnectionKeepAliveStrategy myStrategy = (response, context) -> {
            HeaderElementIterator it = new BasicHeaderElementIterator
                    (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && param.equalsIgnoreCase
                        ("timeout")) {
                    return Long.parseLong(value) * 1000;
                }
            }
            return 60 * 1000;//如果没有约定，则默认定义时长为60s
        };
        visitRecordClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(myStrategy)
                .setDefaultRequestConfig(requestConfig)
                .build();
        new HttpClientUtils().scheduleHttpClientPool(connectionManager);
    }

    public void scheduleHttpClientPool(PoolingHttpClientConnectionManager connectionManager){
        IdleConnectionMonitorThread idleConnectionMonitor = new IdleConnectionMonitorThread(connectionManager);
        idleConnectionMonitor.start();
    }

    /** 连接池监控线程，定时扫描关闭空闲线程 */
    private final class IdleConnectionMonitorThread extends Thread{
        private final HttpClientConnectionManager connMgr;
        private volatile boolean shutdown;
        private static final int MONITOR_INTERVAL_MS = 2000;
        private static final int IDLE_ALIVE_MS = 5000;

        public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
            this.shutdown = false;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(MONITOR_INTERVAL_MS);
                        // 关闭无效的连接
                        connMgr.closeExpiredConnections();
                        // 关闭空闲时间超过IDLE_ALIVE_MS的连接
                        connMgr.closeIdleConnections(IDLE_ALIVE_MS, TimeUnit.MILLISECONDS);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }

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
        CloseableHttpResponse execute = httpclient.execute(httpPost);
        execute.close();
        httpPost.releaseConnection();
        return execute;
    }

    /** post请求 */
    public static int httpPost(String reqUrl, Object params) throws IOException {
        HttpPost httpPost = new HttpPost(reqUrl);
        // 设置请求参数
        StringEntity  stringEntity = new StringEntity(JSONObject.toJSONString(params), "UTF-8");
        stringEntity.setContentEncoding("utf-8");
        stringEntity.setContentType("application/json;charset=utf-8");
        httpPost.setEntity(stringEntity);
        // 发送
        CloseableHttpResponse execute = httpclient.execute(httpPost);
        int statusCode = execute.getStatusLine().getStatusCode();
        httpPost.releaseConnection();
        execute.close();
        System.out.println(params);
        return statusCode;
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
