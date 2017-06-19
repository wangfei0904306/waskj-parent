package com.waskj.base.consumer.core.advice;

import com.waskj.base.consumer.core.result.JsonResult;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 全局 验证 异常 处理
 */
@ControllerAdvice(annotations = {RestController.class})
public class GlobalControllerAdvice {

    private Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public JsonResult handleValidationException(ConstraintViolationException e){
        log.error("内部错误",e);
        JsonResult result = provideErrorResult();
        for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
            String field = ((PathImpl)cv.getPropertyPath()).getLeafNode().asString();
            String message = cv.getMessage();
            result.addFieldError(field,message);
        }
        return result;
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public JsonResult handleBindException(BindException e){
        log.error("内部错误",e);
        JsonResult result = provideErrorResult();
        for (FieldError error : e.getFieldErrors()) {
            result.addFieldError(error.getField(),error.getDefaultMessage());
        }
        return result;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public JsonResult handleIllegalArgumentException(IllegalArgumentException e){
        log.error("内部错误",e);
        return JsonResult.resultError("参数格式不正确!");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public JsonResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        log.error("内部错误",e);
        return JsonResult.resultError("缺少参数: " + e.getParameterName());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult handleException(Exception e){
        log.error("内部错误",e);
        return JsonResult.resultError("参数格式不正确!");
    }

    // 没有权限
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedException(){
        log.debug("there is no permission to do action !");
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .header("Access-Control-Allow-Methods","POST","GET","PUT","OPTIONS","DELETE")
                .header("Access-Control-Allow-Credentials","true")
                .header("Access-Control-Expose-Headers","auth_token")
                .header("Access-Control-Allow-Origin","*")
                .build();
    }

    // 没有登录
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity handleUnauthenticatedException(){
        log.debug("not login!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Access-Control-Allow-Methods","POST","GET","PUT","OPTIONS","DELETE")
                .header("Access-Control-Allow-Credentials","true")
                .header("Access-Control-Expose-Headers","auth_token")
                .header("Access-Control-Allow-Origin","*")
                .build();
    }

    private JsonResult provideErrorResult(){
        return JsonResult.resultError("参数错误");
    }


}
