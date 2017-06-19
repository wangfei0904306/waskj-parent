package com.waskj.base.consumer.core.advice;

import com.waskj.base.consumer.core.result.JsonResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 通用验证
 * Created by poet on 2016/9/26.
 */
@Aspect
@Component
public class ValidateAop {

    @Autowired
    private Validator validator;

    public Object around(ProceedingJoinPoint joinPoint){

        final Signature signature = joinPoint.getStaticPart().getSignature();
        if( signature instanceof MethodSignature){
            MethodSignature ms = (MethodSignature)signature;
            Parameter[] ps = ms.getMethod().getParameters();

            Set<ConstraintViolation<Object>> constraintViolations = validator.forExecutables().validateParameters(joinPoint.getTarget(), ms.getMethod(), joinPoint.getArgs());

            List errorMsg = new ArrayList();
            for (ConstraintViolation<Object> cv : constraintViolations) {
                errorMsg.add(cv.getMessage());
            }

            if(constraintViolations.size() > 0 ){
                return JsonResult.resultSuccess(errorMsg);
            }

        }

        try {
//            return JsonResult.resultSuccess(UtilMisc.toMap("fromAop","hehe"));
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    private HttpServletResponse getResponse(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
    }

}
