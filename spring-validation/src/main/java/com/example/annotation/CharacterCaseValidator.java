package com.example.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CharacterCaseValidator implements ConstraintValidator<CharacterCase, String> {
    private boolean upper;
    @Override
    public void initialize(CharacterCase constraintAnnotation) {
        upper = constraintAnnotation.upper();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (upper) {
            if (value != null && value.equals(value.toUpperCase())) {
                return true;
            }
        } else {
            if (value != null && value.equals(value.toLowerCase())) {
                return true;
            }
        }
        String defaultConstraintMessageTemplate = context.getDefaultConstraintMessageTemplate();
        System.out.println("default message :" + defaultConstraintMessageTemplate);
        //禁用默认提示信息
        //context.disableDefaultConstraintViolation();
        //设置提示语
        //context.buildConstraintViolationWithTemplate("can not contains blank").addConstraintViolation();
        return false;
    }
}
