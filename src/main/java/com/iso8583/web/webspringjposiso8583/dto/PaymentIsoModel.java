package com.iso8583.web.webspringjposiso8583.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PaymentIsoModel {
    @NotNull @NotEmpty
    private String uniquenumber;
    @NotNull @NotEmpty
    private String accountto;

    private String name;
    @Min(10000)
    private BigDecimal amount;


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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
