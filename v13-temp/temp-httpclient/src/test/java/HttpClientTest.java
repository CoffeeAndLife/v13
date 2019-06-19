import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangguizhao
 */
public class HttpClientTest {

    @Test
    public void grapHTMLTest() throws IOException {
        //1.打开浏览器
        CloseableHttpClient client = HttpClients.createDefault();
        //2.输入网址
        String url = "http://localhost:9093/item/createHTMLById/1";
        HttpGet getRequest = new HttpGet(url);
        //3.敲回车
        CloseableHttpResponse response = client.execute(getRequest);
        //4.解析服务器的响应信息
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 200){
            //请求成功！
            //获取响应体信息
            /*HttpEntity entity = response.getEntity();
            //获取输入流，我们就可以读取远程服务器给我们反馈的信息
            InputStream inputStream = entity.getContent();
            //IO流
            byte[] bs = new byte[1024];
            int len;
            while((len=inputStream.read(bs))!=-1){
                System.out.println(new String(bs,0,len));
            }*/
            //如果只是获取内容，有简化的方式
            System.out.println(EntityUtils.toString(response.getEntity()));
        }else{
            System.out.println(statusCode);
        }
    }

    @Test
    public void utilsTest(){
        /*String response = HttpClientUtils.doGet("http://localhost:9093/item/createHTMLById/1");
        System.out.println(response);*/

        Map<String, String> map = new HashMap<>();
        map.put("username","123");
        map.put("password","123456");
        System.out.println(HttpClientUtils.doGet("http://localhost:9093/item/param", map));
    }

    //HttpClientUtils.doGet(url,param);
}
