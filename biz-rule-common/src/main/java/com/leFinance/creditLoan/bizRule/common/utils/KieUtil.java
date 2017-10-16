package com.leFinance.creditLoan.bizRule.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhulili1
 * @date: 2017/10/16
 * @description: KieUtil工具类
 */
@Slf4j
public class KieUtil {

    public static ConcurrentHashMap<String, KieContainer> kieContainerMap;

    public static void setKieContainerMap(){
        kieContainerMap = new ConcurrentHashMap<>(8);
    }

    public static KieContainer createAndGetKieContainer(String groupId, String artifactId, String version,
                                                        String kmodule, Resource[] resources){
        try{
            KieServices kieServices = KieServices.Factory.get();
            // 创建 ReleaseId
            ReleaseId releaseId = kieServices.newReleaseId(groupId, artifactId, version);
            // 创建 in-memory Jar
            KieProjectUtil.createAndDeployJar(kieServices, kmodule, releaseId, resources);
            return kieServices.newKieContainer(releaseId);
        } catch(Exception e){
            log.error("创建KieContainer异常{}", e.getMessage(), e);
            throw new RuntimeException("创建KieContainer异常" + e.getMessage());
        }
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
