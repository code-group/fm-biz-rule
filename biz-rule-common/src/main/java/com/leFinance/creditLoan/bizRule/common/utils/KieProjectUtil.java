package com.leFinance.creditLoan.bizRule.common.utils;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.kie.internal.builder.InternalKieBuilder;

import java.util.function.Predicate;

/**
 * @author: zhulili1
 * @date: 2017/10/14
 * @description:
 */
public class KieProjectUtil {

    public static KieModule createAndDeployJar(KieServices ks, String kmoduleContent, ReleaseId releaseId, Resource... resources) {
        return createAndDeployJar(ks, kmoduleContent, o -> true, releaseId, resources);
    }

    public static KieModule createAndDeployJar(KieServices ks,
                                               String kmoduleContent,
                                               Predicate<String> classFilter,
                                               ReleaseId releaseId,
                                               Resource... resources) {
        byte[] jar = createJar(ks, kmoduleContent, classFilter, releaseId, resources);

        KieModule km = deployJarIntoRepository(ks, jar);
        return km;
    }

    public static byte[] createJar(KieServices ks, String kmoduleContent, Predicate<String> classFilter, ReleaseId releaseId, Resource... resources) {
        KieFileSystem kfs = ks.newKieFileSystem().generateAndWritePomXML(releaseId).writeKModuleXML(kmoduleContent);
        for (int i = 0; i < resources.length; i++) {
            if (resources[i] != null) {
                kfs.write(resources[i]);
            }
        }
        KieBuilder kieBuilder = ks.newKieBuilder(kfs);
        ((InternalKieBuilder) kieBuilder).buildAll(classFilter);
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException(results.getMessages(Message.Level.ERROR).toString());
        }
        InternalKieModule kieModule = (InternalKieModule) ks.getRepository()
                .getKieModule(releaseId);
        byte[] jar = kieModule.getBytes();
        return jar;
    }

    private static KieModule deployJarIntoRepository(KieServices ks, byte[] jar) {
        Resource jarRes = ks.getResources().newByteArrayResource(jar);
        KieModule km = ks.getRepository().addKieModule(jarRes);
        return km;
    }
}
