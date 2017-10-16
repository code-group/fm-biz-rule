package com.leFinance.creditLoan.bizRule.web;

import com.alibaba.boot.dubbo.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @Author: zhulili1
 * @Date: 2017/10/11
 * @Description: Application启动自动配置类
 */
@EnableDubboConfiguration
@SpringBootApplication(scanBasePackages = {"com.leFinance.creditLoan.bizRule.service", "com.leFinance.creditLoan.bizRule.web"
        , "com.leFinance.creditLoan.bizRule.common"
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
