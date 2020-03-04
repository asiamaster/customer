package com.dili.customer.domain.dto;

import com.dili.customer.domain.Contacts;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/27 22:21
 */
@Getter
@Setter
@ToString
public class CustomerUpdateInput implements Serializable {

    private static final long serialVersionUID = 5391152354038473151L;

    /**
     * 客户ID
     */
    @NotNull(message = "业务关键ID不能为空")
    private Long id;

    /**
     * 客户行业##水果批发/蔬菜批发/超市
     */
    private String profession;

    /**
     * 客户状态 0注销，1生效，2禁用，
     */
    @NotNull(message = "客户状态不能为空")
    private Integer state;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^(1[3456789]\\d{9})$", message = "请输入正确的联系方式")
    private String contactsPhone;

    /**
     * 客户归属人
     */
    @NotNull(message = "客户所属人不能为空")
    private Long ownerId;

    /**
     * 所属市场ID
     */
    private Long marketId;

    /**
     * 归属部门
     */
    private Long departmentId;

    /**
     * 证件信息
     */
    private CustomerCertificateInput customerCertificate;

    /**
     * 联系人信息
     */
    private List<Contacts> contactsList;

    /**
     * 操作人ID
     */
    private Long operatorId;
}
