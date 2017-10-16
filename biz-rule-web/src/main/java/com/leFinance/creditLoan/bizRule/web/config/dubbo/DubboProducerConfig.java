package com.leFinance.creditLoan.bizRule.web.config.dubbo;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.leFinance.creditLoan.bizRule.interfaces.ContractBizRuleInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


@Configuration
public class DubboProducerConfig {

    @Resource
    private ContractBizRuleInterface contractBizRuleInterfaceImpl;


    @Bean
    public ServiceBean<ContractBizRuleInterface> contractBizRuleInterface() {
        ServiceBean<ContractBizRuleInterface> serviceBean = new ServiceBean<>();
        serviceBean.setInterface(ContractBizRuleInterface.class);
        serviceBean.setRef(contractBizRuleInterfaceImpl);
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
