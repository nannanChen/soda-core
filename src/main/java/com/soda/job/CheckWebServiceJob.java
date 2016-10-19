package com.soda.job;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by kcao on 2016/10/19.
 */
public class CheckWebServiceJob {

    private static Logger log = Logger.getLogger(CheckWebServiceJob.class);

    static String url="http://www.etmsaas.com:9999/soda-web/";
//    static String url="http://192.168.20.90:9999/soda-web/";

    public static void main(String[] args) {
       while(true){
           HttpRequestBase httpSendRequest = new HttpGet(url);
           RequestConfig requestConfig = RequestConfig.custom()
                   .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                   .setSocketTimeout(5000).build();
           httpSendRequest.setConfig(requestConfig);
           CloseableHttpClient httpClient = HttpClients.custom().build();
           try {
               CloseableHttpResponse response = httpClient.execute(httpSendRequest);
               StatusLine lineCode= response.getStatusLine();
               if(lineCode.getStatusCode()==200){
                   log.info("正常！"+lineCode);
               }else{
                   log.info("发邮件！");
                   sendMail(lineCode.toString());
               }
           } catch (IOException e) {
               log.error(e);
               e.printStackTrace();
               sendMail("Message:"+e.getMessage()+" Cause:"+e.getCause());
           }finally {
               try {
                   Thread.sleep(3*60*1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       }
    }

    private static void sendMail(String msg) {
        HtmlEmail email = new HtmlEmail();
        try {
            // 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
            email.setHostName("smtp.sohu.com");
            // 字符编码集的设置
            email.setCharset("UTF-8");
            // 收件人的邮箱
            email.addTo("1097165036@qq.com");
            email.addCc("2439213945@qq.com");
            email.addCc("648478969@qq.com");
            // 发送人的邮箱
            email.setFrom("soda_check@sohu.com", "soda_check@sohu.com");
            // 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
            email.setAuthentication("soda_check","soda_check_1");
            // 要发送的邮件主题
            email.setSubject("soda web服务预警");
            // 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
            email.setMsg("服务异常："+msg);
            // 发送
            String send=email.send();
            log.info("send:"+send+" 发送成功:"+msg);
        } catch (EmailException e) {
            log.error(e);
            e.printStackTrace();
        }
    }

}
