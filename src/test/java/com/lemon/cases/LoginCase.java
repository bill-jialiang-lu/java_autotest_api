package com.lemon.cases;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackData;
import com.lemon.utils.Constants;
import com.lemon.utils.ExcelUtils;
import com.lemon.utils.HttpUtils;
import com.lemon.utils.UserData;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class LoginCase extends BaseCase{

    @Test(dataProvider = "testParam")
    public void test(CaseInfo caseInfo) {

        paramsReplace(caseInfo);

        String body = HttpUtils.call(caseInfo,UserData.DEFAULT_HEADERS);
        getParamsUserData(body, "$.data.token_info.token", "${token}");
        getParamsUserData(body, "$.data.id", "${member_id}");
        System.out.println("VARS: "+UserData.VARS);

        boolean reponseAssertFlag = reponseAssert(caseInfo.getExpectedResult(), body);

        addWriteBackData(sheetIndex, caseInfo.getCaseId(), Constants.REPONSE_CELL_NUM, body);

        String assertResult = reponseAssertFlag ? Constants.ASSERT_SUCCESS : Constants.ASSERT_FAILED;
        addWriteBackData(sheetIndex, caseInfo.getCaseId(), Constants.ASSERT_CELL_NUM, assertResult);

        Assert.assertEquals(assertResult, Constants.ASSERT_SUCCESS);

    }



    @DataProvider
    public Object[] testParam() {

        List<CaseInfo> caseInfoList = ExcelUtils.read(sheetIndex,sheetNum,clazz);

        return caseInfoList.toArray();
    }
}
