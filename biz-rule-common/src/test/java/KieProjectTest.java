import com.leFinance.creditLoan.bizRule.common.utils.FileUtil;
import com.leFinance.creditLoan.bizRule.common.utils.KieProjectUtil;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

import java.io.StringReader;

/**
 * @author: zhulili1
 * @date: 2017/10/14
 * @description:
 */
public class KieProjectTest {

    public static void main(String[] args) {
        KieServices kieServices = KieServices.Factory.get();
        String drl = "package org.drools.test\n" +
                "rule R1 when\n" +
//                "   $m : Object()\n" +
                "then\n" +
                "System.out.println(111);\n" +
                "end\n";
//        Resource resource = kieServices.getResources().newReaderResource( new StringReader( drl), "UTF-8" );
        Resource resource = ResourceFactory.newFileResource("D:/doc/fm_my/rules/test.drl"); // 读本地文件
//        resource = ResourceFactory.newClassPathResource("rules/contract_rule.drl"); // 本地不可读
        resource.setTargetPath("org/drools/test/rules.drl");
        String kmodule = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kmodule xmlns=\"http://www.drools.org/xsd/kmodule\">\n" +
                "  <kbase name=\"testKbase\" packages=\"org.drools.test\">\n" +
                "    <ksession name=\"testKsession\"/>\n" +
                "  </kbase>\n" +
                "</kmodule>";
        kmodule = FileUtil.readFileToString("D:/doc/fm_my/rules/kmodule.xml");
        // Create an in-memory jar for version 1.0.0
        ReleaseId releaseId = kieServices.newReleaseId("org.kie", "test-delete", "1.0.0");
        Resource[] resources = new Resource[]{resource};
        KieProjectUtil.createAndDeployJar(kieServices, kmodule, releaseId, resources);

        KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        KieSession kieSession = kieContainer.newKieSession("testKsession");
        kieSession.insert(new TestFact());
        kieSession.fireAllRules();

        System.out.println("111end");

        drl = "package org.drools.test\n" +
                "rule R1 when\n" +
//                "   $m : Object()\n" +
                "then\n" +
                "System.out.println(222);\n" +
                "end\n";
        Resource resource2 = kieServices.getResources().newReaderResource( new StringReader( drl), "UTF-8" );
        resource2.setTargetPath("org/drools/test/rules2.drl");
        kmodule = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kmodule xmlns=\"http://www.drools.org/xsd/kmodule\">\n" +
                "  <kbase name=\"testKbase2\" packages=\"org.drools.test\">\n" +
                "    <ksession name=\"testKsession2\"/>\n" +
                "  </kbase>\n" +
                "</kmodule>";

        // Create an in-memory jar for version 1.0.1
        releaseId = kieServices.newReleaseId("org.kie", "test-delete", "1.0.1");
        KieProjectUtil.createAndDeployJar(kieServices, kmodule, releaseId, resource2);

        KieContainer kieContainer2 = kieServices.newKieContainer(releaseId);
        kieSession = kieContainer2.newKieSession("testKsession2");
        kieSession.insert(new TestFact());
        kieSession.fireAllRules();
        System.out.println("222end");
        // 再次获得session
        kieSession = kieContainer2.newKieSession("testKsession2");
        kieSession.insert(new TestFact());
        kieSession.fireAllRules();
        System.out.println("222end");

        // 重建Container1
        drl = "package org.drools.test\n" +
                "rule R1 when\n" +
//                "   $m : Object()\n" +
                "then\n" +
                "System.out.println(111+\"new\");\n" +
                "end\n";
        resource = kieServices.getResources().newReaderResource( new StringReader( drl), "UTF-8" );
        resource.setTargetPath("org/drools/test/rules.drl");
        kmodule = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kmodule xmlns=\"http://www.drools.org/xsd/kmodule\">\n" +
                "  <kbase name=\"testKbase\" packages=\"org.drools.test\">\n" +
                "    <ksession name=\"testKsession\"/>\n" +
                "  </kbase>\n" +
                "</kmodule>";

        // Create an in-memory jar for version 1.0.0
        releaseId = kieServices.newReleaseId("org.kie", "test-delete", "1.0.0");
        KieProjectUtil.createAndDeployJar(kieServices, kmodule, releaseId, resource);

        kieContainer = kieServices.newKieContainer(releaseId);
        kieSession = kieContainer.newKieSession("testKsession");
        kieSession.insert(new TestFact());
        kieSession.fireAllRules();
        System.out.println("111new end");
    }
}
