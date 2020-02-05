package com.dili.customer.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-01-09 17:46:13.
 */
@Getter
@Setter
public class Address extends BaseDomain {
    /**
     * ID
     */
    private Long id;

    /**
     * 客户
     */
    @NotNull(message = "所属客户不能为空")
    private Long customerId;

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    @NotBlank(message = "联系地址不能为空")
    @Size(max = 250,message = "联系地址请保持在250个字符以内")
    private String address;

    /**
     * 所在城市
     */
    @NotNull(message = "所属城市不能为空")
    private String cityId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    /**
     * 创建人
     */
    private Long creatorId;

    /**
     * 修改人
     */
    @NotNull(message = "操作人不能为空")
    private Long modifierId;

    private String lat;

    private String lng;

    /**
     * 是否默认
     */
    private Integer isDefault;

}