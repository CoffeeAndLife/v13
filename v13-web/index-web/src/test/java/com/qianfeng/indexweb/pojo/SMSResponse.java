package com.qianfeng.indexweb.pojo;

import java.util.Arrays;

/**
 * @author huangguizhao
 */
public class SMSResponse {
    //{"respCode":"00000","respDesc":"请求成功。","failCount":"0","failList":[],"smsId":"9ba28fb753994b42aa6d09c4dbd19776"}
    private String respCode;
    private String respDesc;
    private String failCount;
    private String[] failList;
    private String smsId;

    @Override
    public String toString() {
        return "SMSResponse{" +
                "respCode='" + respCode + '\'' +
                ", respDesc='" + respDesc + '\'' +
                ", failCount='" + failCount + '\'' +
                ", failList=" + Arrays.toString(failList) +
                ", smsId='" + smsId + '\'' +
                '}';
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public String getFailCount() {
        return failCount;
    }

    public void setFailCount(String failCount) {
        this.failCount = failCount;
    }

    public String[] getFailList() {
        return failList;
    }

    public void setFailList(String[] failList) {
        this.failList = failList;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }
}
