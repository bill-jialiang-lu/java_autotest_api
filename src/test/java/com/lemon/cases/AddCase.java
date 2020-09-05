package com.lemon.cases;

import com.alibaba.fastjson.JSONPath;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackData;
import com.lemon.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCase extends BaseCase{



    @Test(dataProvider = "testParam")
    public void test(CaseInfo caseInfo) {
        paramsReplace(caseInfo);

        Map<String, String> headers = getAuthorizationHeaders();

        String body = HttpUtils.call(caseInfo, headers);
        getParamsUserData(body, "$.data.id", "${loan_id}");

        boolean reponseAssertFlag = reponseAssert(caseInfo.getExpectedResult(), body);
        addWriteBackData(sheetIndex, caseInfo.getCaseId(), 8, body);

        String assertResult = reponseAssertFlag ? Constants.ASSERT_SUCCESS : Constants.ASSERT_FAILED;
        addWriteBackData(sheetIndex, caseInfo.getCaseId(), Constants.ASSERT_CELL_NUM, assertResult);

        Assert.assertEquals(assertResult, Constants.ASSERT_SUCCESS);

    }

    /**
     * 数据库断言
     * @param caseInfo
     * @param beforeSQLResult
     * @param afterSQLResult
     * @return
     */
    public boolean sqlAssert(CaseInfo caseInfo, BigDecimal beforeSQLResult, BigDecimal afterSQLResult) {
        boolean sqlFlag = false;
        if (StringUtils.isNotBlank(caseInfo.getSql())) {
            String amountStr = JSONPath.read(caseInfo.getParams(), "$.amount").toString();
            BigDecimal amount = new BigDecimal(amountStr);
            if (afterSQLResult.subtract(beforeSQLResult).compareTo(amount) == 0) {
                System.out.println("数据库断言成功");
                sqlFlag = true;
            } else {
                System.out.println("数据库断言失败");
            }
        }
        return sqlFlag;
    }

    @DataProvider
    public Object[] testParam() {

        List<CaseInfo> caseInfoList = ExcelUtils.read(sheetIndex,sheetNum,clazz);

        return caseInfoList.toArray();
    }
}
