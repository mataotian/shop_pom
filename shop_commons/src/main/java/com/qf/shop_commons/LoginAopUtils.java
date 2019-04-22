package com.qf.shop_commons;

import com.qf.entity.User;
import com.sun.xml.internal.ws.client.RequestContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLEncoder;

@Aspect
public class LoginAopUtils {

    @Autowired
    private RedisTemplate redisTemplate;
    @Around("@annotation(IsLogin)")
    public Object loginAop(ProceedingJoinPoint joinPoint){

        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            Cookie[] cookies = request.getCookies();
            String loginToken=null;
            if(cookies!=null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("login_token")) {
                        loginToken = cookie.getValue();
                        break;
                    }
                }
            }
            User user=null;
            if(loginToken!=null){
                user = (User)redisTemplate.opsForValue().get(loginToken);
            }

            if(user==null){
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Method method = signature.getMethod();
                IsLogin annotation = method.getAnnotation(IsLogin.class);
                boolean b = annotation.mustLogin();
                if(b){
                    String requestURL = request.getRequestURL().toString();
                    String param = request.getQueryString();
                    String returnUrl=requestURL+"?"+param;
                    returnUrl= URLEncoder.encode(requestURL,"utf-8");

                    return "http://localhost:8084/sso/tologin?badUrl="+requestURL;
                }
            }

            Object[] args = joinPoint.getArgs();
            for (int i=0;i<args.length;i++) {
                if(args[i] != null && args[i].getClass()==User.class){
                    args[i]=user;
                    break;
                }
            }

            Object proceed = joinPoint.proceed(args);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
