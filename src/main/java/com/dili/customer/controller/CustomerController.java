package com.dili.customer.controller;

import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.CustomerQuery;
import com.dili.customer.domain.dto.EnterpriseCustomer;
import com.dili.customer.domain.dto.IndividualCustomer;
import com.dili.customer.enums.CustomerEnum;
import com.dili.customer.rpc.CustomerRpc;
import com.dili.customer.validator.AddView;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/3 17:01
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRpc customerRpc;

    /**
     * 跳转到Customer页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/enterprise/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "customer/enterprise/list";
    }


    /**
     * 分页查询客户列表信息
     * @param customer
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String listPage(CustomerQuery customer, HttpServletRequest request) throws Exception {
        PageOutput<List<Customer>> listPage = customerRpc.listPage(customer);
        List results = true ? ValueProviderUtils.buildDataByProvider(customer, listPage.getData()) : listPage.getData();
        return new EasyuiPageOutput(listPage.getTotal(), results).toString();
    }

    /**
     * 企业客户注册
     * @param customer
     * @return BaseOutput
     */
    @RequestMapping(value="/registerEnterprise.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput registerEnterprise(@Validated({AddView.class}) EnterpriseCustomer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

        customer.setState(CustomerEnum.State.NORMAL.getCode());
        return customerRpc.registerEnterprise(customer);
    }

    /**
     * 跳转到注册页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/enterprise/register.html", method = RequestMethod.GET)
    public String register(ModelMap modelMap) {
        return "customer/enterprise/register";
    }

    /**
     * 跳转到更新页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/enterprise/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap) {
        return "customer/enterprise/update";
    }

    /**
     * 跳转到详情页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value="/enterprise/detail.html", method = RequestMethod.GET)
    public String detail(ModelMap modelMap) {
        return "customer/enterprise/detail";
    }

    /**
     * 客人客户注册
     * @param customer
     * @return BaseOutput
     */
    @RequestMapping(value="/registerIndividual.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput registerIndividual(@Validated({AddView.class}) IndividualCustomer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        customer.setState(CustomerEnum.State.NORMAL.getCode());
        return customerRpc.registerIndividual(customer);
    }

}
