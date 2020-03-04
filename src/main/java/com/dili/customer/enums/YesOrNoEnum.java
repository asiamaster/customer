package com.dili.customer.enums;

import lombok.Getter;

/**
 * <B>是否标识枚举类</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/3 18:51
 */
public enum YesOrNoEnum {

    YES(1, "是"),
    NO(0, "否"),
    ;

    @Getter
    private Integer code;
    @Getter
    private String value;

    YesOrNoEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * 获取某个枚举值实例信息
     *
     * @param code
     * @return
     */
    public static YesOrNoEnum getInstance(Integer code) {
        for (YesOrNoEnum anEnum : YesOrNoEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }
}
