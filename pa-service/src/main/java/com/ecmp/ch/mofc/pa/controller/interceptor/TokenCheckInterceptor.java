package com.ecmp.ch.mofc.pa.controller.interceptor;

import com.ecmp.annotation.IgnoreAuthentication;
import com.ecmp.context.ContextUtil;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.SessionUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Base64;

/**
 * @description: 登录用户拦截器
 * @author: wangdayin
 * @create: 2018/8/24.
 */
@Component
public class TokenCheckInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(TokenCheckInterceptor.class);
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        SessionUser sessionUser = null;
        if (handler instanceof HandlerMethod) {
            //如果有IgnoreCheckAuth注解的不做登录用户
            Annotation[] annotations = ((HandlerMethod) handler).getBeanType().getAnnotations();
            if (!ObjectUtils.isEmpty(annotations)) {
                for (int i = 0; i < annotations.length; i++) {
                    if (annotations[i].annotationType().getName().equalsIgnoreCase(IgnoreAuthentication.class.getName())) {
                        //定时任务调用其他服务接口时,需要模拟一个用户调用
                        sessionUser = ContextUtil.mockUser();
                        return true;
                    }
                }
            }
            String token = request.getHeader(ContextUtil.AUTHORIZATION_KEY);
            String userAccount = request.getParameter("userAccount");
            if (!StringUtils.isEmpty(token)) {
                sessionUser = ContextUtil.getSessionUser(token);
            } else if (!StringUtils.isEmpty(userAccount)) {
                //导出
                sessionUser = ContextUtil.setSessionUser("10005", userAccount);
            } else if (ObjectUtils.isEmpty(sessionUser)) {
                //如果请求包含completeTask用cookie获取当前登录用户
                Cookie[] cookies = request.getCookies();
                String tokenIncode = null;
                if (!ObjectUtils.isEmpty(cookies)) {
                    for (int i = 0; i < cookies.length; i++) {
                        if ("_s".equalsIgnoreCase(cookies[i].getName())) {
                            tokenIncode = cookies[i].getValue();
                        }
                    }
                    if (StringUtils.isEmpty(tokenIncode)) {
                        return checkIsLocal();
                    }
                    //转码
                    byte[] decode = Base64.getDecoder().decode(tokenIncode);
                    String tokenDecoder = new String(decode, 0, 0, decode.length);
                    String userToken = redisTemplate.opsForValue().get("jwt:" + tokenDecoder);
                    if (StringUtils.isEmpty(userToken)) {
                        return checkIsLocal();
                    }
                    sessionUser = ContextUtil.getSessionUser(userToken);
                } else {
                    checkIsLocal();
                }
            } else {
                throw new RuntimeException("获取当前登录失败，请登录！");
            }
        }
        logger.info("请求地址[" + path + "],当前登录用户======>" + JsonUtils.toJson(sessionUser));
        return true;
    }

    /**
     * 本地加载配置允许模拟
     * @return
     */
    private boolean checkIsLocal() {
        //本地加载配置允许模拟
        if (ContextUtil.isLocalConfig()) {
            SessionUser sessionUser = ContextUtil.mockUser();
            logger.info("当前模拟登录用户======>" + JsonUtils.toJson(sessionUser));
            return true;
        } else {
            throw new RuntimeException("获取当前登录失败，请登录！");
        }
    }
}
