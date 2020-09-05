package com.lemon.cases;


import com.alibaba.fastjson.JSONObject;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.WriteBackData;
import com.lemon.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Class.*;

/**
 * 注册接口测试类型
 */
public class RegisterCase extends BaseCase{



    @Test(dataProvider = "testParam")
    public void test(CaseInfo caseInfo) {

        paramsReplace(caseInfo);

        Long beforeSQLResult = (Long)SQLUtils.getSingleResult(caseInfo.getSql());

        String body = HttpUtils.call(caseInfo, UserData.DEFAULT_HEADERS);
        boolean reponseAssertFlag = reponseAssert(caseInfo.getExpectedResult(), body);
        addWriteBackData(sheetIndex, caseInfo.getCaseId(), 8, body);

        Long afterSQLResult = (Long)SQLUtils.getSingleResult(caseInfo.getSql());
        boolean sqlAssertFlag = sqlAssert(caseInfo.getSql(), beforeSQLResult, afterSQLResult);

        String assertResult = reponseAssertFlag ? Constants.ASSERT_SUCCESS : Constants.ASSERT_FAILED;
        addWriteBackData(sheetIndex, caseInfo.getCaseId(), Constants.ASSERT_CELL_NUM, assertResult);

        Assert.assertEquals(assertResult, Constants.ASSERT_SUCCESS);

    }

    /**
     * 数据库断言
     * @param sql
     * @param beforeSQLResult
     * @param afterSQLResult
     * @return
     */
    private boolean sqlAssert(String sql, Long beforeSQLResult, Long afterSQLResult) {
        boolean sqlFlag = false;
        if (StringUtils.isNotBlank(sql)) {
            if (beforeSQLResult == 0 && afterSQLResult == 1) {
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
