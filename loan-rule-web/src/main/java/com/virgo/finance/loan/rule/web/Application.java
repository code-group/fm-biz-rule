package com.virgo.finance.loan.rule.web;

import com.alibaba.boot.dubbo.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author: zhulili1
 * date: 2017/10/11
 * description: Application启动自动配置类
 */
@EnableDubboConfiguration
@SpringBootApplication(scanBasePackages = {"com.virgo.finance.loan.rule.service",
        "com.virgo.finance.loan.rule.web"})
public class Application extends SpringBootServletInitializer {

//    static {
//        /*
//           解决dubbo和lockback集成后不打印日志的问题
//         */
//        try {
//            System.setProperty("dubbo.application.logger", "slf4j");
//        } catch (Throwable e) {
//            System.out.println("设置dubbo默认日志接口失败" + e.getMessage());
//        }
//        /*
//          解决java.io.IOException: Can not lock the registry cache file问题
//         */
//        try {
//            System.setProperty("dubbo.registry.file", "/home/dubbo/cache/dubbo-registry-loan-rule.cache");
//        } catch (Throwable e) {
//            System.out.println("设置dubbo缓存文件存放路径失败" + e.getMessage());
//        }
//
//    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
