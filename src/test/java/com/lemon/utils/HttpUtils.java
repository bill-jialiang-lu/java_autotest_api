package com.lemon.utils;

import com.alibaba.fastjson.JSONObject;
import com.lemon.pojo.CaseInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpUtils {
    private static Logger logger = Logger.getLogger(HttpUtils.class);


    /**
     * 根据请求参数发送http请求
     * @param caseInfo
     */
    public static String call(CaseInfo caseInfo,Map<String,String> headers) {
        String body = "";
        try {
            String params = caseInfo.getParams();
            String url = caseInfo.getUrl();
            String method = caseInfo.getMethod();
            String contentType = caseInfo.getContentType();
            if("post".equalsIgnoreCase(caseInfo.getMethod())) {
                if("form".equalsIgnoreCase(contentType)) {
                    params = jsonStr2FormStr(params);
                    headers.put("Content-Type","application/x-www-urlencoded");
                }
                body = HttpUtils.post(url, params,headers);
            }else if("get".equalsIgnoreCase(method)) {
                body = HttpUtils.get(url,headers);
            }else if("patch".equalsIgnoreCase(method)) {
                body = HttpUtils.patch(url, params,headers);
            }
            return body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * json字符串转换成form字符串，key=value
     * @param params
     * @return
     */
    public static String jsonStr2FormStr(String params) {
        Map<String,String> map = JSONObject.parseObject(params, Map.class);
        Set<String> keySet = map.keySet();
        String formParams = "";
        for (String key : keySet) {
            formParams += key + "=" + map.get(key) + "&";
        }
        params = formParams.substring(0,formParams.length()-1);
        return params;
    }


    /**
     * 发送一个get请求
     * @param url 例如:http://api.lemonban.com/futureloan/loans
     * @throws Exception
     */
    public static String get(String url, Map<String, String> headers) throws Exception {
        String body;
        HttpGet get  = new HttpGet(url);

        setHeaders(headers, get);

        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(get);

        body = printResponse(response);
        return body;
    }

    /**
     * 发送一个Post请求
     * @param url    接口地址
     * @param param  接口参数
     * @throws Exception
     */
    public static String post(String url, String param, Map<String, String> headers) throws Exception {
        String entityBody;
        HttpPost post  = new HttpPost(url);

        setHeaders(headers, post);

        StringEntity body = new StringEntity(param,"utf-8");
        post.setEntity(body);

        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(post);

        entityBody = printResponse(response);
        return entityBody;
    }


    /**
     * 发送一个patch请求
     * @param url
     * @param param
     * @throws Exception
     */
    public static String patch(String url,String param, Map<String, String> headers) throws Exception {
        String entityBody;
        HttpPatch patch  = new HttpPatch(url);

        setHeaders(headers, patch);

        StringEntity body = new StringEntity(param,"utf-8");
        patch.setEntity(body);

        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(patch);

        entityBody = printResponse(response);
        return entityBody;
    }

    /**
     * 打印reponse
     * @param response
     * @return
     * @throws IOException
     */
    private static String printResponse(HttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
//        System.out.println(statusCode);
        logger.info(statusCode);

        Header[] allHeaders = response.getAllHeaders();
//        System.out.println(Arrays.toString(allHeaders));
        logger.info(Arrays.toString(allHeaders));

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
//        System.out.println(body);
//        System.out.println("======================================");
        logger.info(body);
        logger.info("======================================");
        return body;
    }

    /**
     * 设置请求头
     * @param headers
     * @param request
     */
    public static void setHeaders(Map<String, String> headers, HttpRequest request) {
        Set<String> keySet = headers.keySet();
        for (String key : keySet) {
            request.setHeader(key,headers.get(key));
        }
    }


}
