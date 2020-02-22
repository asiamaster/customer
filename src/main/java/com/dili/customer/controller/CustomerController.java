package com.dili.customer.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.CustomerQuery;
import com.dili.customer.domain.dto.EnterpriseCustomer;
import com.dili.customer.domain.dto.IndividualCustomer;
import com.dili.customer.enums.CustomerEnum;
import com.dili.customer.enums.CustomerEnum.OrganizationType;
import com.dili.customer.rpc.CustomerRpc;
import com.dili.customer.validator.AddView;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
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
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static com.dili.customer.enums.CustomerEnum.OrganizationType.ENTERPRISE;
import static com.dili.customer.enums.CustomerEnum.OrganizationType.getInstance;

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

    @Autowired
    private DataDictionaryRpc dataDictionaryRpc;

    /**
     * 跳转到Customer页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/enterprise/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        modelMap.put("organizationType", ENTERPRISE.getCode());
        return "customer/enterprise/list";
    }


    /**
     * 分页查询客户列表信息
     * @param customer
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String listPage(CustomerQuery customer, HttpServletRequest request) throws Exception {
        customer.setMarketId(SessionContext.getSessionContext().getUserTicket().getFirmId());
        //传入的查询时间处理，如传入的是2020-01-01 则默认加1天，不然当天的数据查询不到
        if (Objects.nonNull(customer.getCreateTimeEnd())) {
            customer.setCreateTimeEnd(customer.getCreateTimeEnd().plusDays(1));
        }
        PageOutput<List<Customer>> listPage = customerRpc.listPage(customer);
        List results = true ? ValueProviderUtils.buildDataByProvider(customer, listPage.getData()) : listPage.getData();
        return new EasyuiPageOutput(listPage.getTotal(), results).toString();
    }

    /**
     * 企业客户注册
     * @param customer
     * @return BaseOutput
     */
    @RequestMapping(value = "/registerEnterprise.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput registerEnterprise(@Validated({AddView.class}) EnterpriseCustomer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        setDefaultStorageValue(customer);
        return customerRpc.registerEnterprise(customer);
    }

    /**
     * 跳转到注册页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/enterprise/register.html", method = RequestMethod.GET)
    public String register(ModelMap modelMap) {
        modelMap.put("organizationType", ENTERPRISE.getCode());
        return "customer/register";
    }

    /**
     * 跳转到更新页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/enterprise/update.html", method = RequestMethod.GET)
    public String update(ModelMap modelMap) {
        return "customer/enterprise/update";
    }

    /**
     * 跳转到详情页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/enterprise/detail.html", method = RequestMethod.GET)
    public String detail(ModelMap modelMap) {
        return "customer/enterprise/detail";
    }

    /**
     * 跳转到安全认证页面
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/enterprise/security.html", method = RequestMethod.GET)
    public String security(ModelMap modelMap) {
        return "customer/enterprise/security";
    }

    /**
     * 个人客户注册
     * @param customer
     * @return BaseOutput
     */
    @RequestMapping(value = "/registerIndividual.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput registerIndividual(@Validated({AddView.class}) IndividualCustomer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
//        Validation.buildDefaultValidatorFactory().getValidator().validate()
        setDefaultStorageValue(customer);
        return customerRpc.registerIndividual(customer);
    }

    /**
     * 根据客户类型获取对应的证件类型
     * @param organizationType
     * @return BaseOutput
     */
    @RequestMapping(value = "/getCertificateType.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput getCertificateType(@NotNull String organizationType) {
        OrganizationType type = getInstance(organizationType);
        if (Objects.isNull(type)) {
            return BaseOutput.failure("未知的客户类型");
        }
        String ddCode = "";
        switch (type) {
            case ENTERPRISE:
                ddCode = "enterprise_certificate";
                break;
            case INDIVIDUAL:
                ddCode = "individual_certificate";
                break;
            default:
        }
        DataDictionaryValue condition = DTOUtils.newInstance(DataDictionaryValue.class);
        condition.setDdCode(ddCode);
        return dataDictionaryRpc.listDataDictionaryValue(condition);
    }

    /**
     * 根据证件号检测某个客户在某市场是否已存在
     * @param certificateNumber 客户证件号
     * @return 如果客户在当前市场已存在，则返回错误(false)信息，如果不存在，则返回客户信息(若客户信息存在)
     */
    @RequestMapping(value = "/queryCustomerByNumber.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<Customer> queryCustomerByNumber(String certificateNumber) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (StrUtil.isBlank(certificateNumber) || Objects.isNull(userTicket)) {
            return BaseOutput.failure("请登录后操作");
        }
        return customerRpc.checkExistByNoAndMarket(certificateNumber, userTicket.getFirmId());
    }

    /**
     * 设置客户信息的默认存储数据
     * @param customer
     */
    private void setDefaultStorageValue(IndividualCustomer customer) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        customer.setOperatorId(userTicket.getId());
        customer.setMarketId(userTicket.getFirmId());
        customer.setOwnerId(userTicket.getId());
        customer.setDepartmentId(userTicket.getDepartmentId());
        customer.setState(CustomerEnum.State.NORMAL.getCode());
        customer.setGrade(CustomerEnum.Grade.GENERAL.getCode());
        customer.setIsDelete(CustomerEnum.Deleted.NOT_DELETED.getCode());
    }

}
