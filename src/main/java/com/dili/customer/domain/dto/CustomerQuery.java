package com.dili.customer.domain.dto;

import com.dili.customer.domain.Customer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/5 14:14
 */
@Getter
@Setter
public class CustomerQuery extends Customer {

    /**
     * 创建时间区间查询-开始
     */
    private LocalDateTime createTimeStart;
    /**
     * 创建时间区间查询-结束
     */
    private LocalDateTime createTimeEnd;
}
