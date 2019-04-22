package com.iso8583.web.webspringjposiso8583.dto;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

//@Component
//@Service
public class PaymentIsoModel extends BaseIsoModel {
    @NotNull @NotEmpty
    private String uniquenumber;
    @NotNull @NotEmpty
    private String accountto;

    private String name;
    @Min(10000)
    private String amount;

    public PaymentIsoModel(String atmcardnumber, String accountnumber, @Min(10000) String amount, String name) {

        this.uniquenumber = atmcardnumber;
        this.accountto = accountnumber;
        this.amount = amount;
        this.name = name;

//        this.setBit48(bit48);
    }


    public String getUniquenumber() {
        return uniquenumber;
    }

    public void setUniquenumber(String uniquenumber) {
        this.uniquenumber = uniquenumber;
    }

    public String getAccountto() {
        return accountto;
    }

    public void setAccountto(String accountto) {
        this.accountto = accountto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
