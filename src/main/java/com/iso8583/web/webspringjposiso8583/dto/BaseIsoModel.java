package com.iso8583.web.webspringjposiso8583.dto;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.Map;

@Repository
@RequestMapping("/")
@ResponseBody
public class BaseIsoModel implements Serializable {

    /**
     * bit 2
     */
    protected String atmcardnumber;

    /**
     * bit 3
     */
    protected String pcode;

    /**
     * bit 4
     */
    protected String amount;

    /**
     * bit 11
     */
    protected String trace;

    /**
     * bit 28
     */
    protected String adminCharge;

    /**
     * bit 37
     */
    protected String referenceNumber;

    /**
     * bit 39
     */
    protected String messageCode;

    /**
     * bit 41
     */
    protected String terminalId;

    /**
     * bit 48
     */
    protected String name;

    /**
     * bit 52
     */
    protected String otpPinEncrypted = " ";

    /**
     * bit 61
     */
    protected String infoBiller;

    /**
     * bit 102
     */
    protected String accountnumber;

    private Map<String, Object> detailPayment;

    public BaseIsoModel() {
        super();
    }

    public BaseIsoModel(String atmcardnumber, String accountnumber, String amount, String name) {
        this.atmcardnumber = atmcardnumber;
//        this.pcode = pcode;
        this.accountnumber = accountnumber;
        this.amount = amount;
        this.name = name;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getAtmcardnumber() {
        return atmcardnumber;
    }

    public void setAtmcardnumber(String atmcardnumber) {
        this.atmcardnumber = atmcardnumber;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getOtpPinEncrypted() {
        return otpPinEncrypted;
    }

    public void setOtpPinEncrypted(String otpPinEncrypted) {
        this.otpPinEncrypted = otpPinEncrypted;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getAmount() {
        return Long.valueOf(amount).toString();
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAdminCharge() {
        return adminCharge;
    }

    public void setAdminCharge(String adminCharge) {
        this.adminCharge = adminCharge;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getInfoBiller() {
        return infoBiller;
    }

    public void setInfoBiller(String infoBiller) {
        this.infoBiller = infoBiller;
    }

    public Map<String, Object> getDetailPayment() {
        return detailPayment;
    }

    public void setDetailPayment(Map<String, Object> detailPayment) {
        this.detailPayment = detailPayment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
