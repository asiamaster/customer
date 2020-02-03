package com.dili.customer.domain.dto;

import com.dili.customer.domain.Address;
import com.dili.customer.validator.AddView;
import com.dili.customer.validator.UpdateView;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <B>企业客户基本信息</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/3 17:40
 */
@Getter
@Setter
public class EnterpriseCustomer implements Serializable {

    private static final long serialVersionUID = 4869002082778403248L;
    /**
     * 客户ID
     */
    @NotNull(message = "业务关键ID不能为空",groups = {UpdateView.class})
    private Long id;

    /**
     * 证件号
     */
    @NotBlank(message = "证件号码不能为空", groups = {AddView.class})
    @Size(min = 1, max = 20, message = "证件号码请保持在40个字以内", groups = {AddView.class})
    private String certificateNumber;

    /**
     * 证件类型
     */
    @NotBlank(message = "证件类型不能为空", groups = {AddView.class})
    private String certificateType;


    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空", groups = {AddView.class,UpdateView.class})
    @Size(min = 1, max = 40, message = "客户名称请保持在40个字以内", groups = {AddView.class,UpdateView.class})
    private String name;

    /**
     * 组织类型,个人/企业
     */
    @NotBlank(message = "组织类型不能为空", groups = {AddView.class})
    private String organizationType;

    /**
     * 来源系统##外部系统来源标识
     */
    @NotBlank(message = "客户来源不能为空", groups = {AddView.class})
    private String sourceSystem;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空", groups = {AddView.class,UpdateView.class})
    @Pattern(regexp = "^((0\\d{2,3}-\\d{7,8})|(1[3456789]\\d{9}))$", message = "请输入正确的联系方式", groups = {AddView.class,UpdateView.class})
    private String contactsPhone;

    /**
     * 联系人
     */
    @NotBlank(message = "联系人不能为空", groups = {AddView.class})
    @Size(max = 40, message = "联系人请保持在250个字以内", groups = {AddView.class})
    private String contactsName;

    /**
     * 企业法人
     */
    @NotBlank(message = "企业法人不能为空", groups = {AddView.class})
    @Size(max = 40, message = "法人请保持在40个字以内", groups = {AddView.class})
    private String corporationName;

    /**
     * 创建人
     */
    private Long operatorId;

    /**
     * 客户归属人
     */
    private Long ownerId;

    /**
     * 客户所属市场
     */
    private String firmId;

    /**
     * 客户状态
     */
    private Integer state;

    /**
     * 归属部门
     */
    private Long departmentId;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("certificateNumber", certificateNumber)
                .add("certificateType", certificateType)
                .add("name", name)
                .add("organizationType", organizationType)
                .add("sourceSystem", sourceSystem)
                .add("contactsPhone", contactsPhone)
                .add("contactsName", contactsName)
                .add("corporationName", corporationName)
                .add("operatorId", operatorId)
                .add("ownerId", ownerId)
                .add("firmId", firmId)
                .add("state", state)
                .add("departmentId", departmentId)
                .toString();
    }
}
