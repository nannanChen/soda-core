package com.soda.service;



import com.soda.dao.Subway;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/22.
 */
public class SubwayImpl implements Subway,Serializable {
    public static final String KEY_1 = "cg4sKjksgLn3svtst32DOpxd2raYipb8";
    public Map getlongitudeandlatitude(String address) {
        try {
            URL url = new URL("http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak="+KEY_1+"&qq-pf-to=pcqq.c2c");
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url.toString());
            // 在请求消息头中指定语言，保证服务器会返回中文数据
            httpGet.addHeader("Accept-Language", "zh-CN");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            Map<String, String> map = new HashMap<String, String>();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity, "utf-8");
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.get("status").toString().equals("0")){
                    String lng = jsonObject.getJSONObject("result").getJSONObject("location").get("lng").toString();
                    String lat = jsonObject.getJSONObject("result").getJSONObject("location").get("lat").toString();
                    map.put("lng", lng);
                    map.put("lat", lat);
                }
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
