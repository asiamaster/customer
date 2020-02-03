package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-01-02 16:18:39.
 */
@Table(name = "`contacts`")
@Getter
@Setter
public class Contacts extends BaseDomain {
    /**
     * ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属客户
     */
    @Column(name = "`customer_id`",updatable = false)
    @NotNull(message = "所属客户不能为空")
    private Long customerId;

    /**
     * 姓名
     */
    @Column(name = "`name`")
    @NotBlank(message = "联系人姓名不能为空")
    @Size(max = 20,message = "联系人姓名请保持在20个字符以内")
    private String name;

    /**
     * 性别男，女
     */
    @Column(name = "`gender`")
    private Integer gender;

    /**
     * 电话
     */
    @Column(name = "`phone`")
    @NotBlank(message = "联系人电话不能为空")
    @Size(max = 20,message = "联系人电话请保持在20个字符以内")
    private String phone;

    /**
     * 地址
     */
    @Column(name = "`address`")
    @Size(max = 250,message = "联系人地址请保持在250个字符以内")
    private String address;

    /**
     * 职务/关系
     */
    @Column(name = "`position`")
    @Size(max = 100,message = "联系人职务请保持在100个字符以内")
    private String position;

    /**
     * 出生日期
     */
    @Column(name = "`birthdate`")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    /**
     * 备注
     */
    @Column(name = "`notes`")
    @Size(max = 250,message = "联系人备注请保持在250个字符以内")
    private String notes;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`",updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /**
     * 创建人
     */
    @Column(name = "`creator_id`",updatable = false)
    private Long creatorId;

    /**
     * 修改人
     */
    @Column(name = "`modifier_id`")
    private Long modifierId;

}