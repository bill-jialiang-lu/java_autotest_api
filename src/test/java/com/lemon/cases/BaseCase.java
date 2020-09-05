package com.lemon.cases;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackData;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;
import com.lemon.utils.UserData;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BaseCase {
    private static Logger logger = Logger.getLogger(BaseCase.class);
    public int sheetIndex;
    public int sheetNum;
    public Class clazz;

    @BeforeClass
    @Parameters({"sheetIndex","sheetNum","sheetValue"})
    public void beforeClass(int sheetIndex,int sheetNum,String className) {
        this.sheetIndex = sheetIndex;
        this.sheetNum = sheetNum;
        try {
            this.clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @AfterSuite
    public void finish() throws Exception {
        System.out.println("=====================finsh====================");
        ExcelUtils.batchWrite();
    }


    /**
     * 从responseBody通过jsonpath取出对应参数，存入到UserData.VARS中
     * @param responseBody                  接口响应json字符串
     * @param jsonPathExpression            jsonpath表达式
     * @param userDataKey                   VARS中的key
     */
    public void getParamsUserData(String responseBody, String jsonPathExpression, String userDataKey) {
        Object token = JSONPath.read(responseBody, jsonPathExpression);
        if(token != null) {
            UserData.VARS.put(userDataKey,token);
            System.out.println(token);
        }
    }

    /**
     *获取鉴权头 并且加上默认请求头，并返回
     * @return
     */
    public Map<String, String> getAuthorizationHeaders() {
        Object token = UserData.VARS.get("${token}");
        Map<String,String> headers = new HashMap<>();
        headers.putAll(UserData.DEFAULT_HEADERS);
        //token鉴权是本地携带一个固定的Header以及自动逸内容，将这个内容发送给服务端，服务端通过得到的数据
        //计算得到一个签名发送回本地，本地下次链接时携带这个签名，如果服务端再次计算匹配的话就是验证通过。
        headers.put("Authorization","Bearer " + token);
        return headers;
    }


    /**
     * 接口响应断言
     * @param expectedResult            断言期望值
     * @param reponseBody               接口响应内容
     * @return                          接口响应断言结果
     */
    public boolean reponseAssert(String expectedResult, String reponseBody) {
        Map<String, Object> map = JSONObject.parseObject(expectedResult, Map.class);
        Set<String> keySet = map.keySet();
        boolean reponseAssertFalg = true;
        for (String actualValueExpression : keySet) {
            Object expectedValue = map.get(actualValueExpression);
            Object actualValue = JSONPath.read(reponseBody, actualValueExpression);
            if (!expectedValue.equals(actualValue)) {
                reponseAssertFalg = false;
                break;
            }
        }
//        System.out.println("断言结果：" + reponseAssertFalg);
        logger.info("断言结果：" + reponseAssertFalg);
        return reponseAssertFalg;
    }

    /**
     * 参数化替换
     * @param caseInfo
     */
    public void paramsReplace(CaseInfo caseInfo) {
        Set<String> keySet = UserData.VARS.keySet();
        String params = caseInfo.getParams();
        String sql = caseInfo.getSql();
        String expectedResult = caseInfo.getExpectedResult();
        String url = caseInfo.getUrl();
        for (String placeHolder : keySet) {
            String value = UserData.VARS.get(placeHolder).toString();
            if (StringUtils.isNotBlank(params)) {
                params = params.replace(placeHolder,value);
                caseInfo.setParams(params);
            }
            if (StringUtils.isNotBlank(sql)) {
                sql = sql.replace(placeHolder,value);
                caseInfo.setSql(sql);
            }
            if (StringUtils.isNotBlank(expectedResult)) {
                expectedResult = expectedResult.replace(placeHolder,value);
                caseInfo.setExpectedResult(expectedResult);
            }
            if (StringUtils.isNotBlank(url)) {
                url = url.replace(placeHolder,value);
                caseInfo.setUrl(url);
            }
        }
        System.out.println("params: " + params);
        System.out.println("url: " + url);
        System.out.println("expectedResult: " + expectedResult);
        System.out.println("sql: " + sql);
    }


    /**
     * 添加回写对象到集合中
     * @param sheetIndex
     * @param rowNum
     * @param cellNum
     * @param body
     */
    public void addWriteBackData(int sheetIndex, int rowNum, int cellNum, String body) {
        WriteBackData wbd = new WriteBackData(sheetIndex, rowNum, cellNum, body);
        ExcelUtils.wbdList.add(wbd);
    }


}
