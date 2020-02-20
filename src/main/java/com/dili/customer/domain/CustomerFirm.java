package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 客户归属市场关系表
 * This file was generated on 2020-01-09 18:02:32.
 */
@Getter
@Setter
public class CustomerFirm extends BaseDomain {
    /**
     * ID
     */
    private Long id;

    /**
     * 归属组织
     */
    private Long marketId;

    /**
     * 归属部门##内部创建归属到创建员工的部门
     */
    private Long departmentId;

    /**
     * 客户id
     */
    private Long customerId;

    /**
     * 所有者
     */
    private Long ownerId;

    /**
     * 客户别名
     */
    private String alias;

    /**
     * 客户类型##采购、销售、代买等##{provider:"dataDictionaryValueProvider",queryParams:{dd_id:4}}
     */
    private String type;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 备注信息
     */
    private String notes;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改人id
     */
    private Long modifierId;

    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

}