package com.lemon.demo;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;

public class GetDemo {
    public static void main(String[] args) throws Exception {
        /*
        *发送get请求
        * 1.创建请求对象
        * 2.设置请求方法
        * 3.设置接口Url地址
        * 4.设置请求头
        * 5.设置请求体（接口参数）
        * 6.点击发送
        * 7.获取响应对象
        * 8.格式化响应对象(响应状态码、响应头、响应体)
         */
        HttpGet get  = new HttpGet("http://api.lemonban.com/futureloan/loans");
        get.setHeader("X-Lemonban-Media-Type","lemonban.v1");
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(get);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);

        Header[] allHeaders = response.getAllHeaders();
        System.out.println(Arrays.toString(allHeaders));

        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));

    }
}
