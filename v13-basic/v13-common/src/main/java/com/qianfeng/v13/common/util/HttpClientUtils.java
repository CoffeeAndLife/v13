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
