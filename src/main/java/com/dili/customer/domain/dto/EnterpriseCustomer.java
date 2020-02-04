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
public class EnterpriseCustomer extends IndividualCustomer implements Serializable {

    private static final long serialVersionUID = 4869002082778403248L;

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("contactsName", contactsName)
                .add("corporationName", corporationName)
                .toString();
    }

}
