package com.example.domain;


import com.example.annotation.CharacterCase;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class User extends BaseInfo {
    @NotNull(message = "用户名不能为空")
    @Length(max = 5, message = "长度需要在0和5之间")
    private String username;

    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^1[3|4|5|7|8][0-9]\\d{8}$", message = "手机号格式不正确")
    private String phone;

    @Range(min = 1, max = 120, message = "请输入正确的年龄")
    private Integer age;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    @PastOrPresent(message = "请输入正确生日")
    private Date bithday;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "请输入正确的日期")
    private Date travelPlanDate;

    @Email(message = "请输入正确的邮箱")
    private String mail;

    @Pattern(regexp = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]", message = "请输入正确的邮箱链接")
    private String mailAddr;

    @DecimalMax(value = "200.00", message = "体重有些超标哦")
    @DecimalMin(value = "60.00", message = "多吃点饭吧")
    private BigDecimal weight;

    // integer 接受的最大数字个数， fraction 精度
    @Digits(message = "请输入正确的数字", integer = 1, fraction = 0)
    private Long distance;

    // integer 接受的最大数字个数， fraction 精度
    @Digits(message = "请输入正确的数字", integer = 2, fraction = 2)
    private Double money;

    @Size(min = 0, max = 10)
    private String travelAddress;

    @Size(min = 1)
    private List<String> phoneCount;

    @CharacterCase(message = "字符必须为大写", upper = true)
    private String likeColor;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Date getBithday() {
        return bithday;
    }

    public void setBithday(Date bithday) {
        this.bithday = bithday;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Date getTravelPlanDate() {
        return travelPlanDate;
    }

    public void setTravelPlanDate(Date travelPlanDate) {
        this.travelPlanDate = travelPlanDate;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getTravelAddress() {
        return travelAddress;
    }

    public void setTravelAddress(String travelAddress) {
        this.travelAddress = travelAddress;
    }

    public String getMailAddr() {
        return mailAddr;
    }

    public void setMailAddr(String mailAddr) {
        this.mailAddr = mailAddr;
    }

    public List<String> getPhoneCount() {
        return phoneCount;
    }

    public void setPhoneCount(List<String> phoneCount) {
        this.phoneCount = phoneCount;
    }

    public String getLikeColor() {
        return likeColor;
    }

    public void setLikeColor(String likeColor) {
        this.likeColor = likeColor;
    }
}
