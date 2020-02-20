package com.dili.customer.enums;

import lombok.Getter;

import javax.validation.constraints.Email;

/**
 * <B>客户信息相关的枚举定义</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/6 14:42
 */
public class CustomerEnum {

    public enum State{

        NORMAL(1, "正常"),
        DISABLED(2, "禁用"),
        ;
        @Getter
        private Integer code;
        @Getter
        private String value;

        State(Integer code, String value) {
            this.code = code;
            this.value = value;
        }


        /**
         * 获取某个枚举值实例信息
         * @param code
         * @return
         */
        public static State getInstance(Integer code){
            for (State state : State.values()) {
                if (state.getCode().equals(code)){
                    return state;
                }
            }
            return null;
        }
    }

    /**
     * 客户组织类型
     */
    public enum OrganizationType {
        INDIVIDUAL("individual", "个人"),
        ENTERPRISE("enterprise", "企业"),
        ;

        @Getter
        private String code;
        @Getter
        private String value;

        OrganizationType(String code, String value) {
            this.code = code;
            this.value = value;
        }

        /**
         * 获取某个枚举值实例信息
         *
         * @param code
         * @return
         */
        public static OrganizationType getInstance(String code) {
            for (OrganizationType type : OrganizationType.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * 客户等级
     */
    public enum Grade {
        GENERAL(1, "普通"),
        KAC(2, "重点"),
        ;

        @Getter
        private Integer code;
        @Getter
        private String value;

        Grade(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        /**
         * 获取某个枚举值实例信息
         *
         * @param code
         * @return
         */
        public static Grade getInstance(Integer code) {
            for (Grade grade : Grade.values()) {
                if (grade.getCode().equals(code)) {
                    return grade;
                }
            }
            return null;
        }
    }
}
