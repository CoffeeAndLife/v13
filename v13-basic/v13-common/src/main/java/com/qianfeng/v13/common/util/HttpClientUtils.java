package com.qianfeng.v13.common.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

/**
 * @author huangguizhao
 */
public class HttpClientUtils {

    private HttpClientUtils(){}

    //?username=123
    public static String doGet(String url,Map<String,String> params){
        //1.打开浏览器
        CloseableHttpClient client = HttpClients.createDefault();
        //
        try {
            //2.输入网址
            URIBuilder uriBuilder = new URIBuilder(url);
            if(params != null){
                Set<Map.Entry<String, String>> entries = params.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    uriBuilder.addParameter(entry.getKey(),entry.getValue());
                }
            }
            HttpGet getRequest = new HttpGet(uriBuilder.build());

            getRequest.setHeader("Cookie","user_cart=b27aced7-3942-4309-bd52-817842f8296f; user_token=eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoiYWRtaW4iLCJpYXQiOjE1NjE5NDU5NDEsImV4cCI6MTU2MTk0Nzc0MX0.l8PMH6e--yaLQrGfPg85Lo0PXLw-whH");
            CloseableHttpResponse response = null;
            //3.敲回车
            response = client.execute(getRequest);
            //4.解析服务器的响应信息
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200){
                return EntityUtils.toString(response.getEntity());
            }else{
                return statusCode+"";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "error";
        } finally {
            if(client != null){
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String doGet(String url){
        return doGet(url,null);
    }
}
