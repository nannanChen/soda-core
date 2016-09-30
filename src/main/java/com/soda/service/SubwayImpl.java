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
  * Created by nan on 2016/9/26 16:42
  *
  */

public class SubwayImpl implements Subway,Serializable {
    //cg4sKjksgLn3svtst32DOpxd2raYipb8  702632E1add3d4953d0f105f27c294b9
    public static final String KEY_1 = "cg4sKjksgLn3svtst32DOpxd2raYipb8";

    /**
     * 根据地址从百度API得到经纬度
     * @param address
     * @return String
     */
    public String getlongitudeandlatitudeFromBaiDu(String address) {
        System.out.println("传入地址:"+address);
        String lnglat ="";
        try {
            URL url = new URL("http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak="+KEY_1+"&qq-pf-to=pcqq.c2c");
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url.toString());
            // 在请求消息头中指定语言，保证服务器会返回中文数据
            httpGet.addHeader("Accept-Language", "zh-CN");
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity, "utf-8");
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.get("status").toString().equals("0")){
                    String lng = jsonObject.getJSONObject("result").getJSONObject("location").get("lng").toString();
                    String lat =  jsonObject.getJSONObject("result").getJSONObject("location").get("lat").toString();
                    lnglat = lng +","+lat;
                }
                return lnglat;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-1"+","+"-1";
    }
}
