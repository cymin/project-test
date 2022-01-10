package com.example.domain;

import com.example.annotation.UpdateGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BaseInfo {

    @NotNull(groups = UpdateGroup.class)
    private Integer id;

    @NotBlank(message = "家庭住址不能为空")
    @Length(max = 5, message = "长度需要在0和5之间")
    private String homeAddress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }
}
