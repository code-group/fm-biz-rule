package com.virgo.finance.loan.rule.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;

import java.util.function.Predicate;

/**
 * @author: zhulili1
 * date: 2017/10/14
 * description: 创建 kie jar 工具类
 */
@Slf4j
public class KieProjectUtil {


    /**
     * created by zhulili1, on 2017/10/20
     * description: 通过Resource创建KJar
     **/
    public static KieModule createAndDeployJar(KieServices ks, String kmoduleContent, ReleaseId releaseId, Resource... resources) {
        return createAndDeployJar(ks, kmoduleContent, o -> true, releaseId, resources);
    }

    private static KieModule createAndDeployJar(KieServices ks,
                                               String kmoduleContent,
                                               Predicate<String> classFilter,
                                               ReleaseId releaseId,
                                               Resource... resources) {
        byte[] jar = createJar(ks, kmoduleContent, classFilter, releaseId, resources);

        KieModule km = deployJarIntoRepository(ks, jar);
        return km;
    }

    private static byte[] createJar(KieServices ks, String kmoduleContent, Predicate<String> classFilter,
                                    ReleaseId releaseId, Resource... resources) {
        KieFileSystem kfs = ks.newKieFileSystem().generateAndWritePomXML(releaseId).writeKModuleXML(kmoduleContent);
        for (int i = 0; i < resources.length; i++) {
            if (resources[i] != null) {
                kfs.write(resources[i]);
            }
        }
        return buildKJar(ks, kfs, releaseId);
    }

    private static KieModule deployJarIntoRepository(KieServices ks, byte[] jar) {
        Resource jarRes = ks.getResources().newByteArrayResource(jar);
        KieModule km = ks.getRepository().addKieModule(jarRes);
        return km;
    }

    /**
     * created by zhulili1, on 2017/10/20
     * description: 通过字符串创建KJar
     *
     **/
    public static KieModule createAndDeployJar( KieServices ks,
                                                ReleaseId releaseId,
                                                String[] drls ) {
        byte[] jar = createKJar(ks, releaseId, drls);

        // Deploy jar into the repository
        KieModule km = deployJarIntoRepository(ks, jar);
        return km;
    }

    private static byte[] createKJar(KieServices ks,
                                    ReleaseId releaseId,
                                    String[] drls) {
        KieFileSystem kfs = ks.newKieFileSystem().generateAndWritePomXML(releaseId);
        for (int i = 0; i < drls.length; i++) {
            if (drls[i] != null) {
                kfs.write("src/main/resources/r" + i + ".drl", drls[i]);
            }
        }
        return buildKJar(ks, kfs, releaseId);
    }

    private static byte[] buildKJar(KieServices ks, KieFileSystem kfs, ReleaseId releaseId) {
        KieBuilder kb = ks.newKieBuilder(kfs).buildAll();
        if (kb.getResults().hasMessages(Message.Level.ERROR)) {
            for (Message result : kb.getResults().getMessages()) {
                log.info(result.getText());
            }
            throw new IllegalStateException(kb.getResults().getMessages(Message.Level.ERROR).toString());
        }
        InternalKieModule kieModule = (InternalKieModule) ks.getRepository()
                .getKieModule(releaseId);
        byte[] jar = kieModule.getBytes();
        return jar;
    }
}
