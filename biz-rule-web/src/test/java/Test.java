
import com.leFinance.creditLoan.bizRule.fact.ContractFact;
import lecreditcontract.dto.CreateContractReqDto;
import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * @author: zhulili1
 * @date: 2017/10/13
 * @description:
 */
public class Test {

    public static void main(String[] args) {
        String rule = "package com.fei.drools\r\n";
        rule += "import com.leFinance.creditLoan.bizRule.fact.ContractFact;\r\n";
        rule += "dialect  \"mvel\" \r\n";
        rule += "rule \"rule1\"\r\n";
        rule += "\twhen\r\n";
        rule += "$b : ContractFact(feeValue != null)";
        rule += "\tthen\r\n";
        rule += "\t\tSystem.out.println(1);\r\n";
        rule += "end\r\n";

        ContractFact contractFact = new ContractFact();
//        contractFact.setFeeValue(new BigDecimal(1));

        StatefulKnowledgeSession kSession = null;
        try {
            KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
            //装入规则，可以装入多个
            kb.add(ResourceFactory.newByteArrayResource(rule.getBytes("utf-8")), ResourceType.DRL);

            KnowledgeBuilderErrors errors = kb.getErrors();
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error);
            }
            KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
            kBase.addKnowledgePackages(kb.getKnowledgePackages());

            kSession = kBase.newStatefulKnowledgeSession();

            kSession.insert(contractFact);
            kSession.fireAllRules();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if (kSession != null)
                kSession.dispose();
        }

    }
}
