package com.lemon.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class TestInfo {
    @Excel(name = "CaseId")
    private int caseId;
    @Excel(name = "Name")
    private String name;
    @Excel(name = "Url")
    private String url;
    @Excel(name = "Type")
    private String method;

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "TestInfo{" +
                "caseId=" + caseId +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
