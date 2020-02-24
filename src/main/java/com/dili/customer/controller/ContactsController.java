package com.dili.customer.controller;

import com.dili.customer.domain.Contacts;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.CustomerQuery;
import com.dili.customer.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/23 18:07
 */
@Controller
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private CustomerRpc customerRpc;

    /**
     * 查询客户联系人列表信息
     * @param customerId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String list(Long customerId) throws Exception {
        List results = Lists.newArrayList();
        if (null == customerId) {
            return new EasyuiPageOutput(results.size(), results).toString();
        }
        BaseOutput<List<Contacts>> output = customerRpc.listAllContacts(customerId, SessionContext.getSessionContext().getUserTicket().getFirmId());
        if (output.isSuccess()) {
            results = ValueProviderUtils.buildDataByProvider(new Contacts(), output.getData());
        }
        return new EasyuiPageOutput(results.size(), results).toString();
    }
}
