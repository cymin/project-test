package com.example.handler;

import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class BindExceptionHanlder {
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public String handleBindException(HttpServletRequest request, BindException exception) {
        List<FieldError> allErrors = exception.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError errorMessage : allErrors) {
            sb.append(errorMessage.getField()).append(": ").append(errorMessage.getDefaultMessage()).append(", ");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException exception) {
        final Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> violation : constraintViolations) {
            final PathImpl propertyPath = (PathImpl) violation.getPropertyPath();
            final NodeImpl leafNode = propertyPath.getLeafNode();
            sb.append(leafNode.getName()).append(": ").append(violation.getMessage()).append(", ");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException exception) {
        StringBuilder sb = new StringBuilder();
        BindingResult bindingResult = exception.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        // getFieldError???????????????????????????????????????(P.S.????????????????????????????????????)
//        FieldError fieldError = bindingResult.getFieldError();
        for (FieldError errorMessage : fieldErrors) {
            sb.append(errorMessage.getField()).append(": ").append(errorMessage.getDefaultMessage()).append(", ");
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * ??????Validated????????????
     * <p>
     * ???: ?????????ConstraintViolationException????????? ?????????ValidationException??????
     *
     * @param e
     *         ??????????????????
     * @return ??????????????????data
     */
   /* @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public Map<String, Object> handleParameterVerificationException(Exception e) {
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put("code", "100001");
        String msg = null;
        /// BindException
        if (e instanceof BindException) {
            // getFieldError???????????????????????????????????????(P.S.????????????????????????????????????)
            FieldError fieldError = ((BindException) e).getFieldError();
            if (fieldError != null) {
                msg = fieldError.getDefaultMessage();
            }
            /// MethodArgumentNotValidException
        } else if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            // getFieldError???????????????????????????????????????(P.S.????????????????????????????????????)
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                msg = fieldError.getDefaultMessage();
            }
            /// ValidationException ???????????????ConstraintViolationException
        } else if (e instanceof ConstraintViolationException) {
            *//*
             * ConstraintViolationException???e.getMessage()??????
             *     {?????????}.{?????????}: {message}
             *  ???????????????????????????message??????
             *//*
            msg = e.getMessage();
            if (msg != null) {
                int lastIndex = msg.lastIndexOf(':');
                if (lastIndex >= 0) {
                    msg = msg.substring(lastIndex + 1).trim();
                }
            }
            /// ValidationException ?????????????????????
        } else {
            msg = "?????????????????????";
        }
        resultMap.put("msg", msg);
        return resultMap;
    }*/
}
