package com.dili.customer.domain.vo;

import com.dili.customer.sdk.domain.CustomerMarket;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <B></B>
 * <B>Copyright:本软件源代码版权归农丰时代科技有限公司及其研发团队所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/6/19 14:51
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CustomerMarketVo extends CustomerMarket {

    /**
     * 所有者名称
     */
    private String ownerName;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 在本市场的初始创建人姓名
     */
    private String creatorName;
}
