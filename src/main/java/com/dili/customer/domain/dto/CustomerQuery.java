package com.dili.customer.domain.dto;

import com.dili.customer.domain.Customer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

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
@ToString(callSuper = true)
public class CustomerQuery extends Customer {

    /**
     * 创建时间区间查询-开始
     */
    private LocalDate createTimeStart;
    /**
     * 创建时间区间查询-结束
     */
    private LocalDate createTimeEnd;

    /**
     * 客户所在市场中的创建时间-开始
     */
    private LocalDate marketCreateTimeStart;

    /**
     * 客户所在市场中的创建时间-介绍
     */
    private LocalDate marketCreateTimeEnd;

    /**
     * 客户所属组织
     */
    private Long marketId;

    /**
     * 客户所属组织集
     */
    private List<Long> marketIdList;

    /**
     * 关键字查询，根据证件号匹配或名称模糊查询
     */
    private String keyword;

    /**
     * 客户所在市场的客户创建人
     */
    private Long marketCreatorId;

    /**
     * 当客户在多市场时，是否分组只返回一条客户主数据
     * 如果设置为true，则根据客户id分组
     */
    private Boolean isGroup;

}
