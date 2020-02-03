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
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-01-09 17:46:13.
 */
@Table(name = "`address`")
@Getter
@Setter
public class Address extends BaseDomain {
    /**
     * ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户
     */
    @Column(name = "`customer_id`", updatable = false)
    @NotNull(message = "所属客户不能为空")
    private Long customerId;

    /**
     * 名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 地址
     */
    @Column(name = "`address`")
    @NotBlank(message = "联系地址不能为空")
    @Size(max = 250,message = "联系地址请保持在250个字符以内")
    private String address;

    /**
     * 所在城市
     */
    @Column(name = "`city_id`")
    @NotNull(message = "所属城市不能为空")
    private String cityId;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
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
    @NotNull(message = "操作人不能为空")
    private Long modifierId;

    @Column(name = "`lat`")
    private String lat;

    @Column(name = "`lng`")
    private String lng;

    @Column(name = "`is_default`")
    private Boolean isDefault;

}