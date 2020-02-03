package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 客户归属市场关系表
 * This file was generated on 2020-01-09 18:02:32.
 */
@Table(name = "`firm_info`")
@Getter
@Setter
public class FirmInfo extends BaseDomain {
    /**
     * ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 归属组织
     */
    @Column(name = "`firm_id`")
    private String firmId;

    /**
     * 归属部门##内部创建归属到创建员工的部门
     */
    @Column(name = "`department_id`")
    private Long departmentId;

    /**
     * 客户id
     */
    @Column(name = "`customer_id`")
    private Long customerId;

    /**
     * 所有者
     */
    @Column(name = "`owner_id`")
    private Long ownerId;

    /**
     * 客户别名
     */
    @Column(name = "`alias`")
    private String alias;

    /**
     * 客户类型##采购、销售、代买等##{provider:"dataDictionaryValueProvider",queryParams:{dd_id:4}}
     */
    @Column(name = "`type`")
    private String type;

    /**
     * 联系电话
     */
    @Column(name = "`phone`")
    private String phone;

    /**
     * 备注信息
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * 修改人id
     */
    @Column(name = "`modifier_id`")
    private Long modifierId;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

}