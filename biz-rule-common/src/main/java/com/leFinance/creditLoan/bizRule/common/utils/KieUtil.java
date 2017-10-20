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

    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 通过Resource创建KJar
     * @param groupId
     * @param artifactId
     * @param version
     * @param kmodule
     * @param resources
     * @return
     */
    public static void loadRuleByResource(String groupId, String artifactId, String version,
                                                        String kmodule, Resource[] resources){
        // 日志前缀
        final String logPrefix = "通过Resource创建KJar, ";
        try{
            KieServices kieServices = KieServices.Factory.get();
            // 创建 ReleaseId
            ReleaseId releaseId = kieServices.newReleaseId(groupId, artifactId, version);
            // 创建 in-memory Jar
            KieProjectUtil.createAndDeployJar(kieServices, kmodule, releaseId, resources);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常: " + e.getMessage());
        }
    }
    /**
     * created by zhulili1, on 2017/10/20
     * @Description: 通过字符串创建KJar
     * @param groupId
     * @param artifactId
     * @param version
     * @param drls
     */
    public static void loadRule(String groupId, String artifactId, String version, String[] drls){
        // 日志前缀
        final String logPrefix = "通过字符串创建KJar, ";
        try{
            KieServices kieServices = KieServices.Factory.get();
            // 创建 ReleaseId
            ReleaseId releaseId = kieServices.newReleaseId(groupId, artifactId, version);
            // 创建 in-memory Jar
            KieProjectUtil.createAndDeployJar(kieServices, releaseId, drls);
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
            throw new RuntimeException(logPrefix + "异常: " + e.getMessage());
        }

    }

    /**
     * created by zhulili1, on 2017/10/16
     * @Description: 获取KieSession
     * @param groupId
     * @param artifactId
     * @param version
     * @return
     */
    public static KieSession getKieSession(String groupId, String artifactId, String version){
        // 日志前缀
        final String logPrefix = "通过规则版本获取KieSession, ";

        try{
            KieServices kieServices = KieServices.Factory.get();
            ReleaseId releaseId = kieServices.newReleaseId(groupId, artifactId, version);
            KieSession kieSession = kieServices.newKieContainer(releaseId).newKieSession();
            return kieSession;
        } catch(Exception e){
            log.error("{}异常: {}", logPrefix, e.getMessage(), e);
        }

        return null;
    }

}
