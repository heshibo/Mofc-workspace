package com.ecmp.ch.mofc.pa.controller.config;

import com.ecmp.ch.mofc.pa.controller.interceptor.TokenCheckInterceptor;
import com.ecmp.log.util.LogUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @description: spring boot拦截器配置类
 * @author: wangdayin
 * @create: 2018/8/24.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private TokenCheckInterceptor tokenCheckInterceptor;

    /**
     * 添加当前用户拦截
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenCheckInterceptor).addPathPatterns("/**")
                .excludePathPatterns(Arrays.asList("/static/**", "/view/**"))
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html**","/error","/csrf");
    }

    /**
     * 解决跨域请求
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*");
    }

    @Bean
    public Converter<String, Date> addNewConvert() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = sdf.parse(source);
                } catch (ParseException e) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = simpleDateFormat.parse(source);
                    } catch (ParseException pe) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                        try {
                            date = format.parse(source);
                        } catch (ParseException e1) {
                            LogUtil.error("日期格式转换出错");
                        }
                    }
                }
                return date;
            }
        };
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
