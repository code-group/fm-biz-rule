package com.leFinance.creditLoan.bizRule.web.config.dubbo;

import com.alibaba.dubbo.config.spring.AnnotationBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DubboCommonConfig {

    @Bean
    public AnnotationBean annotationBean() {
        AnnotationBean annotationBean = new AnnotationBean();
        annotationBean.setPackage("com.leFinance.creditLoan.bizRule.interfaces.impl");
        return annotationBean;
    }
}
