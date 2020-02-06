package com.dili.customer.enums;

import lombok.Getter;

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
}
