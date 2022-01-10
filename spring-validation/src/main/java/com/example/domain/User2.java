package com.example.domain;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class User2 extends BaseInfo {
    @NotNull(message = "用户名不能为空")
    @Length(max = 5, message = "长度需要在0和5之间")
    private String username;

    @Digits(integer = 2, fraction = 0)
    private BigDecimal price;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Length(max = 2, message = "长度需要在0和2之间")
    public String getHomeAddress() {
        return super.getHomeAddress();
    }
}
