package com.example.controller;

import com.example.annotation.UpdateGroup;
import com.example.annotation.SaveGroup;
import com.example.domain.ResultInfo;
import com.example.domain.User;
import com.example.domain.User2;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
public class ParamsController {

    @GetMapping("/v1")
    public String v1(@Valid @NotNull(message = "not null") String name) {
       return name;
    }

    @GetMapping("/v2")
    public ResultInfo v2(@Validated User user, BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        if (fieldErrors != null) {
            List<String> collect = fieldErrors.stream()
                    .map(o -> o.getField() + ":" + o.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResultInfo<>().success(400, "请求参数错误", collect);
        }
        return new ResultInfo<>().success(200, "success", user);
    }

    @RequestMapping("/a")
    public ResultInfo v3(@Validated({UpdateGroup.class}) @RequestBody User2 user, BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        if (fieldErrors != null && fieldErrors.size() > 0) {
            List<String> collect = fieldErrors.stream()
                    .map(o -> o.getField() + ":" + o.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResultInfo<>().success(400, "请求参数错误", collect);
        }
        return new ResultInfo<>().success(200, "success", user);
    }

    @RequestMapping("/b")
    public ResultInfo v4(@Validated({SaveGroup.class}) @RequestBody User2 user, BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        if (fieldErrors != null && fieldErrors.size() > 0) {
            List<String> collect = fieldErrors.stream()
                    .map(o -> o.getField() + ":" + o.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResultInfo<>().success(400, "请求参数错误", collect);
        }
        return new ResultInfo<>().success(200, "success", user);
    }

    @RequestMapping("/c")
    public ResultInfo c(@Length(max = 3) String a, @NotBlank String b) {
        return new ResultInfo<>().success(200, "success", a + " " + b);
    }
}
