package com.dili.customer.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.customer.domain.Contacts;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.CustomerMarket;
import com.dili.customer.domain.dto.CustomerQuery;
import com.dili.customer.domain.dto.CustomerUpdateInput;
import com.dili.customer.domain.dto.EnterpriseCustomer;
import com.dili.customer.domain.dto.IndividualCustomer;
import com.dili.customer.enums.CustomerEnum;
import com.dili.customer.enums.CustomerEnum.OrganizationType;
import com.dili.customer.enums.NationalityEnum;
import com.dili.customer.rpc.ContactsRpc;
import com.dili.customer.rpc.CustomerMarketRpc;
import com.dili.customer.rpc.CustomerRpc;
import com.dili.customer.rpc.UidRpc;
import com.dili.customer.service.remote.FirmRpcService;
import com.dili.customer.service.remote.UserRpcService;
import com.dili.customer.utils.EnumUtil;
import com.dili.customer.validator.AddView;
import com.dili.logger.sdk.annotation.BusinessLogger;
import com.dili.logger.sdk.base.LoggerContext;
import com.dili.logger.sdk.glossary.LoggerConstant;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.ValueProviderUtils;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.redis.UserResourceRedis;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.dili.customer.enums.CustomerEnum.OrganizationType.*;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/3 17:01
 */
@Slf4j
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRpc customerRpc;
    @Autowired
    private CustomerMarketRpc customerMarketRpc;
    @Autowired
    private ContactsRpc contactsRpc;
    @Autowired
    private DataDictionaryRpc dataDictionaryRpc;
    @Autowired
    private UserRpcService userService;
    @Autowired
    private FirmRpcService firmRpc;
    @Autowired
    private DepartmentRpc departmentRpc;
    @Autowired
    private UserResourceRedis userResourceRedis;
    @Autowired
    private UidRpc uidRpc;
    @Autowired
    protected HttpServletRequest request;

    /**
     * 跳转到企业客户管理页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/enterprise/index.html", method = RequestMethod.GET)
    public String enterpriseIndex(ModelMap modelMap) {
        modelMap.put("organizationType", ENTERPRISE.getCode());
        return "customer/enterprise/list";
    }

    /**
     * 跳转到个人客户管理页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/individual/index.html", method = RequestMethod.GET)
    public String individualIndex(ModelMap modelMap) {
        modelMap.put("organizationType", INDIVIDUAL.getCode());
        return "customer/individual/list";
    }

    /**
     * 分页查询客户列表信息
     *
     * @param customer
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String listPage(CustomerQuery customer, HttpServletRequest request) throws Exception {
        if (Objects.isNull(customer.getMarketId())) {
            List<Firm> userFirms = firmRpc.getCurrentUserFirms();
            if (CollectionUtil.isEmpty(userFirms)) {
                return new EasyuiPageOutput(0, Collections.emptyList()).toString();
            } else {
                List<Long> idList = userFirms.stream().map(Firm::getId).distinct().collect(Collectors.toList());
                customer.setMarketIdList(idList);
            }
        }
        customer.setIsDelete(YesOrNoEnum.NO.getCode());
        //传入的查询时间处理，如传入的是2020-01-01 则默认加1天，不然当天的数据查询不到
        if (Objects.nonNull(customer.getMarketCreateTimeEnd())) {
            customer.setMarketCreateTimeEnd(customer.getMarketCreateTimeEnd().plusDays(1));
        }
        PageOutput<List<Customer>> listPage = customerRpc.listPage(customer);
        List results = true ? ValueProviderUtils.buildDataByProvider(customer, listPage.getData()) : listPage.getData();
        return new EasyuiPageOutput(listPage.getTotal(), results).toString();
    }

    /**
     * 企业客户注册功能
     *
     * @param customer
     * @return BaseOutput
     */
    @RequestMapping(value = "/registerEnterprise.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @BusinessLogger(businessType = "customer", content = "${userName!} 创建企业客户[${name!}] ${flag!}", operationType = "add", systemCode = "CUSTOMER")
    public BaseOutput registerEnterprise(@Validated({AddView.class}) EnterpriseCustomer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            setDefaultStorageValue(customer);
            BaseOutput<Customer> output = customerRpc.registerEnterprise(customer);
            if (output.isSuccess()) {
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, output.getData().getCode());
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, output.getData().getId());
                LoggerContext.put("name", customer.getName());
                LoggerContext.put("flag", "成功");
            } else {
                LoggerContext.put("flag", "失败");
            }
            UserTicket userTicket = getUserTicket();
            if (userTicket != null) {
                LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
                LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
                LoggerContext.put("userName", userTicket.getRealName());
            }
            return output;
        } catch (Exception e) {
            log.error("企业客户注册时异常," + e.getMessage(), e);
            return BaseOutput.failure("系统异常，请稍后再试");
        }
    }

    /**
     * 跳转到注册页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/register.action", method = RequestMethod.GET)
    public String register(String sourceSystem, String sourceChannel, String organizationType, ModelMap modelMap) {
        if (StrUtil.isBlank(sourceSystem) || StrUtil.isBlank(sourceChannel)) {
            modelMap.put("error", "客户来源不明确，不能操作此项");
        } else {
            modelMap.put("sourceSystem", sourceSystem);
            modelMap.put("sourceChannel", sourceChannel);
            UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
            //有创建哪些客户类型的权限
            Set organizationTypeRight = Sets.newHashSet();
            if (userResourceRedis.checkUserResourceRight(userTicket.getId(), "addEnterprise")) {
                organizationTypeRight.add(ENTERPRISE.getCode());
            }
            if (userResourceRedis.checkUserResourceRight(userTicket.getId(), "addIndividual")) {
                organizationTypeRight.add(INDIVIDUAL.getCode());
            }
            if (CollectionUtil.isEmpty(organizationTypeRight)) {
                modelMap.put("error", "你没有新建客户的权限，请联系管理员分配");
            } else {
                if (StrUtil.isNotBlank(organizationType)) {
                    if (organizationTypeRight.contains(organizationType)) {
                        modelMap.put("organizationType", organizationType);
                    } else {
                        modelMap.put("error", "你没有新建此类型客户的权限，请联系管理员分配");
                    }
                }
            }
        }
        return "customer/register";
    }

    /**
     * 跳转到更新页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/update.html", method = RequestMethod.GET)
    public String update(Long customerId, Long marketId, ModelMap modelMap) {
        getCustomerDetail(customerId, marketId, modelMap);
        String updatePath = "customer/enterprise/update";
        if (modelMap.containsKey("customer")) {
            Customer customer = (Customer) modelMap.get("customer");
            OrganizationType instance = getInstance(customer.getOrganizationType());
            updatePath = "customer/" + instance.getCode() + "/update";
            if (ENTERPRISE.equals(instance)) {
                BaseOutput<List<Contacts>> output = contactsRpc.listAllContacts(customerId,marketId);
                if (output.isSuccess() && CollectionUtil.isNotEmpty(output.getData())) {
                    modelMap.put("contactsList", output.getData());
                }
            }
        }
        return updatePath;
    }

    /**
     * 跳转到详情页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/detail.action", method = RequestMethod.GET)
    public String detail(@NotNull Long id, Long marketId, ModelMap modelMap) {
        getCustomerDetail(id, marketId, modelMap);
        String detail = "customer/enterprise/detail";
        if (modelMap.containsKey("customer")) {
            Customer customer = (Customer) modelMap.get("customer");
            detail = "customer/" + CustomerEnum.OrganizationType.getInstance(customer.getOrganizationType()).getCode() + "/detail";
        }
        return detail;
    }

    /**
     * 跳转到安全认证页面
     *
     * @param modelMap
     * @return String
     */
    @RequestMapping(value = "/enterprise/security.html", method = RequestMethod.GET)
    public String security(ModelMap modelMap) {
        return "customer/enterprise/security";
    }

    /**
     * 个人客户注册
     *
     * @param customer
     * @return BaseOutput
     */
    @RequestMapping(value = "/registerIndividual.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @BusinessLogger(businessType = "customer", content = "${userName!} 创建个人客户[${name!}] ${flag!}", operationType = "add", systemCode = "CUSTOMER")
    public BaseOutput registerIndividual(@Validated({AddView.class}) IndividualCustomer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseOutput.failure(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        try {
            setDefaultStorageValue(customer);
            BaseOutput<Customer> output = customerRpc.registerIndividual(customer);
            if (output.isSuccess()) {
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, output.getData().getCode());
                LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, output.getData().getId());
                LoggerContext.put("name", customer.getName());
                LoggerContext.put("flag", "成功");
            } else {
                LoggerContext.put("flag", "失败");
            }
            UserTicket userTicket = getUserTicket();
            if (userTicket != null) {
                LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
                LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
                LoggerContext.put("userName", userTicket.getRealName());
            }
            return output;
        } catch (Exception e) {
            log.error("个人客户注册时异常," + e.getMessage(), e);
            return BaseOutput.failure("系统异常，请稍后再试");
        }
    }

    /**
     * 根据客户类型获取对应的证件类型
     *
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
     *
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
     * 禁用/启用客户
     *
     * @param id     客户ID
     * @param enable 是否启用
     * @return BaseOutput
     */
    @RequestMapping(value = "/doEnable.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @BusinessLogger(businessType = "customer", content = "${operatorName!} [${operationTypeText!}] 客户 [${name!}] ${flag!}", systemCode = "CUSTOMER")
    public BaseOutput doEnable(Long id, Boolean enable,String reason) {
        if (Objects.isNull(id) || Objects.isNull(enable)) {
            return BaseOutput.failure("必要参数丢失");
        }
        CustomerEnum.State instance = null;
        if (enable) {
            instance = CustomerEnum.State.NORMAL;
            LoggerContext.put(LoggerConstant.LOG_OPERATION_TYPE_KEY, "enable");
        } else {
            instance = CustomerEnum.State.DISABLED;
            LoggerContext.put(LoggerConstant.LOG_OPERATION_TYPE_KEY, "disable");
        }
        BaseOutput<Customer> output = customerRpc.updateState(id, instance.getCode());
        if (output.isSuccess()) {
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_CODE_KEY, output.getData().getCode());
            LoggerContext.put(LoggerConstant.LOG_BUSINESS_ID_KEY, output.getData().getId());
            LoggerContext.put("name", output.getData().getName());
            LoggerContext.put("flag", "成功");
        } else {
            LoggerContext.put("flag", "失败");
        }
        UserTicket userTicket = getUserTicket();
        if (userTicket != null) {
            LoggerContext.put(LoggerConstant.LOG_OPERATOR_ID_KEY, userTicket.getId());
            LoggerContext.put(LoggerConstant.LOG_MARKET_ID_KEY, userTicket.getFirmId());
            LoggerContext.put(LoggerConstant.LOG_OPERATOR_NAME_KEY, userTicket.getRealName());
        }
        LoggerContext.put("notes", reason);
        return output;
    }

    /**
     * 处理客户信息更新保存操作
     *
     * @param input 更新数据
     * @return
     */
    @RequestMapping(value = "/doUpdate.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput doUpdate(@RequestBody CustomerUpdateInput input) {
        input.setOperatorId(SessionContext.getSessionContext().getUserTicket().getId());
        return customerRpc.update(input);
    }

    /**
     * 获取民族主数据
     *
     * @return BaseOutput
     */
    @RequestMapping(value = "/listNationality.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput listNationality() {
        return BaseOutput.success().setData(EnumUtil.toObject(NationalityEnum.class));
    }

    /**
     * 设置客户信息的默认存储数据
     *
     * @param customer
     */
    private void setDefaultStorageValue(IndividualCustomer customer) {
        UserTicket userTicket = getUserTicket();
        customer.setOperatorId(userTicket.getId());
        customer.setMarketId(userTicket.getFirmId());
        customer.setOwnerId(userTicket.getId());
        customer.setDepartmentId(userTicket.getDepartmentId());
        customer.setState(CustomerEnum.State.NORMAL.getCode());
        customer.setGrade(CustomerEnum.Grade.GENERAL.getCode());
        customer.setIsDelete(YesOrNoEnum.NO.getCode());
        customer.setCode(getCustomerCode(OrganizationType.getInstance(customer.getOrganizationType())));
        //设置证件是否长期有效
        if (StrUtil.isNotBlank(customer.getCertificateRange()) && "长期".equals(customer.getCertificateRange().trim())) {
            customer.setCertificateLongTerm(YesOrNoEnum.YES.getCode());
        }
    }

    /**
     * 获取客户的基本信息
     *
     * @param customerId 客户ID
     * @param modelMap
     */
    private void getCustomerDetail(Long customerId, Long marketId, ModelMap modelMap) {
        if (Objects.nonNull(customerId) && Objects.nonNull(marketId)) {
            modelMap.put("userTicket", getUserTicket());
            CustomerQuery query = new CustomerQuery();
            query.setId(customerId);
            query.setMarketId(marketId);
            //获取客户基本信息
            BaseOutput<List<Customer>> output = customerRpc.list(query);
            if (output.isSuccess() && CollectionUtil.isNotEmpty(output.getData())) {
                Customer customer = output.getData().stream().findFirst().orElse(new Customer());
                DataDictionaryValue dataDictionaryValue = DTOUtils.newInstance(DataDictionaryValue.class);
                dataDictionaryValue.setDdCode("source_channel");
                dataDictionaryValue.setCode(customer.getSourceChannel());
                BaseOutput<List<DataDictionaryValue>> listDataDictionaryValue = dataDictionaryRpc.listDataDictionaryValue(dataDictionaryValue);
                if (listDataDictionaryValue.isSuccess() && CollectionUtil.isNotEmpty(listDataDictionaryValue.getData())) {
                    customer.setSourceChannelValue(listDataDictionaryValue.getData().get(0).getName());
                }
                BaseOutput<CustomerMarket> marketOutput = customerMarketRpc.getByCustomerAndMarket(customerId, marketId);
                if (marketOutput.isSuccess() && Objects.nonNull(marketOutput.getData())) {
                    CustomerMarket customerMarket = marketOutput.getData();
                    customerMarket.setOwnerName(userService.getUserById(customerMarket.getOwnerId()).get().getRealName());
                    Optional<Firm> market = firmRpc.getFirmById(customerMarket.getMarketId());
                    customerMarket.setMarketName(market.get().getName());
                    if (Objects.nonNull(customerMarket.getDepartmentId())) {
                        BaseOutput<Department> departmentBaseOutput = departmentRpc.get(customerMarket.getDepartmentId());
                        if (departmentBaseOutput.isSuccess() && Objects.nonNull(departmentBaseOutput.getData())) {
                            customerMarket.setDepartmentName(departmentBaseOutput.getData().getName());
                        }
                    }
                    modelMap.put("customerMarket", customerMarket);
                }
                modelMap.put("customer", customer);
            }
        }
    }

    /**
     * 获取客户编码
     *
     * @param type
     * @return
     */
    private String getCustomerCode(OrganizationType type) {
        BaseOutput<String> bizNumberOutput = uidRpc.bizNumber(type.getUidType());
        return bizNumberOutput.getData();
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    private UserTicket getUserTicket() {
        return SessionContext.getSessionContext().getUserTicket();
    }
}
