package com.virgo.finance.loan.rule.common.enums;

/**
 * @author: zhulili1
 * date: 2017/10/26
 * description: 执行规则, 处理结果枚举
 */
public class RuleEnums {

    /**
     * 调用规则结果
     */
    public enum RuleCode {

        // 0-规则执行成功, 1-规则执行失败, 2-忽略的规则
        DEAL_SUCCESS(0, "规则执行成功"),
        DEAL_FAILED(1, "规则执行失败"),
        OMITTED_RULE(2, "忽略的规则");

        private int code;
        private String message;
        RuleCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public static String messageOf(int code) {
            for (RuleCode ruleCode : RuleCode.values()) {
                if (ruleCode.code == code) {
                    return ruleCode.getMessage();
                }
            }
            return null;
        }
    }

    /**
     * 版本号
     */
    public enum Version {
        WORKING_VERSION("working", "启用的版本"),
        INITIAL_VERSION("1", "初始版本");

        public String code;

        private String message;

        Version(String code, String message) {
            this.code = code;
            this.message = message;
        }

    }

    /**
     * 版本号
     */
    public enum Status {
        RULE_STATUS_FORBIDDING(0, "禁用"),
        RULE_STATUS_AVAILABLE(1, "启用");

        public int code;

        private String message;

        Status(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    /**
     * 来源
     */
    public enum Origin {
        RULE_FROM_STRING("str", "字符串"),
        RULE_FROM_FILE("file", "文件");

        public String type;
        private String message;

        Origin(String type, String message) {
            this.type = type;
            this.message = message;
        }

    }
    /**
     * 数据类型
     */
    public enum DataType {
        DATA_TYPE_STRING("1", "字符串"),
        DATA_TYPE_NUM("2", "数字");

        public String type;
        private String message;

        DataType(String type, String message) {
            this.type = type;
            this.message = message;
        }

    }

}
