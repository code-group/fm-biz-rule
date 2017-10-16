package com.leFinance.creditLoan.bizRule.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhulili1
 * @date: 2017/10/16
 * @description: KieContainer工具类
 */
@Slf4j
public class KieContainerUtil {

    public static ConcurrentHashMap<String, KieContainer> kieContainerMap;

    public static void setKieContainerMap(){
        kieContainerMap = new ConcurrentHashMap<>(8);
    }

    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 获取KieContainer
     * @param containerName
     * @return
     */
    public static KieContainer getKieContainer(String containerName){
        return kieContainerMap.get(containerName);
    }

    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 获取KieSession
     * @param containerName
     * @param ksessionName
     * @return
     */
    public static KieSession getKieSession(String containerName, String ksessionName){
        try{
            KieSession kieSession = kieContainerMap.get(containerName).newKieSession(ksessionName);
            return kieSession;
        } catch(Exception e){
            log.error("获取KieSession异常{}", e.getMessage(), e);
            throw new RuntimeException("获取KieSession异常" + e.getMessage());
        }
    }

}
