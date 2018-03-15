package com.virgo.finance.loan.rule.web.config.dubbo;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.virgo.finance.loan.rule.api.RuleConfigInterface;
import com.virgo.finance.loan.rule.api.RuleInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


@Configuration
public class DubboProducerConfig {

    @Resource
    private RuleInterface ruleInterfaceImpl;

    @Resource
    private RuleConfigInterface ruleConfigInterfaceImpl;


    @Bean
    public ServiceBean<RuleInterface> ruleInterface() {
        ServiceBean<RuleInterface> serviceBean = new ServiceBean<>();
        serviceBean.setInterface(RuleInterface.class);
        serviceBean.setRef(ruleInterfaceImpl);
        setPubProperties(serviceBean);
        return serviceBean;
    }

    @Bean
    public ServiceBean<RuleConfigInterface> ruleConfigInterface() {
        ServiceBean<RuleConfigInterface> serviceBean = new ServiceBean<>();
        serviceBean.setInterface(RuleConfigInterface.class);
        serviceBean.setRef(ruleConfigInterfaceImpl);
        setPubProperties(serviceBean);
        return serviceBean;
    }

    /**
     * 设置一些公共属性.
     *
     * @param serviceBean 服务bean
     */
    private void setPubProperties(ServiceBean<?> serviceBean) {
        serviceBean.setVersion("1.0.0");
        serviceBean.setRetries(0);
        serviceBean.setTimeout(3000);
    }
}
