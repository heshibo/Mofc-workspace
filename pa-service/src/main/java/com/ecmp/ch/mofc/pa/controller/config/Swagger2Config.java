package com.ecmp.ch.mofc.pa.controller.config;


import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lifei
 * @date 2018/8/13
 */
@SpringBootConfiguration
@EnableSwagger2
public class Swagger2Config {

    /**
     * UI页面显示信息
     */
    private final String SWAGGER2_API_BASEPACKAGE = "com.ecmp.ch.mofc.pa";

    private final String SWAGGER2_API_TITLE = "MOFC-PA-API";

    private final String SWAGGER2_API_DESCRIPTION = "物流订单履约-订单接入API";

    private final String SWAGGER2_API_VERSION = "1.0.0";

    /**
     * createRestApi
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {

        ParameterBuilder ticketPar = new ParameterBuilder();
        ticketPar.name("Authorization").description("user token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build();

        List<Parameter> pars = new ArrayList<Parameter>();
        pars.add(ticketPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER2_API_BASEPACKAGE))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(pars);

    }

    /**
     * apiInfo
     * @return
     */
    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title(SWAGGER2_API_TITLE)
                .description(SWAGGER2_API_DESCRIPTION)
                .version(SWAGGER2_API_VERSION)
                .build();

    }

}

