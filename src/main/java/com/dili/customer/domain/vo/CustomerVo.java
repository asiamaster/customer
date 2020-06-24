package com.dili.customer.domain.vo;

import com.dili.customer.sdk.domain.Customer;
import lombok.Getter;
import lombok.Setter;

/**
 * <B>客户信息显示层对象</B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/19 14:47
 */
@Setter
@Getter
public class CustomerVo extends Customer {

    /**
     * 客户来源渠道值
     */
    private String sourceChannelValue;
}
