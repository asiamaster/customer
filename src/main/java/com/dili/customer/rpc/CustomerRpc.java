package com.dili.customer.rpc;

import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.CustomerQuery;
import com.dili.customer.domain.dto.CustomerUpdateInput;
import com.dili.customer.domain.dto.EnterpriseCustomer;
import com.dili.customer.domain.dto.IndividualCustomer;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/1/21 14:09
 */
@FeignClient(name = "customer-service",contextId = "customerRpc")
public interface CustomerRpc {

    /**
     * 获取客户列表信息
     * @param customer
     * @return
     */
    @RequestMapping(value = "/api/customer/listPage", method = RequestMethod.POST)
    PageOutput<List<Customer>> listPage(CustomerQuery customer);

    /**
     * 获取客户列表信息
     * @param customer
     * @return
     */
    @RequestMapping(value = "/api/customer/list", method = RequestMethod.POST)
    BaseOutput<List<Customer>> list(CustomerQuery customer);

    /**
     * 企业用户注册
     * @param baseInfo
     * @return
     */
    @RequestMapping(value = "/api/customer/registerEnterprise", method = RequestMethod.POST)
    BaseOutput<Customer> registerEnterprise(EnterpriseCustomer baseInfo);

    /**
     * 个人用户注册
     * @param baseInfo
     * @return
     */
    @RequestMapping(value = "/api/customer/registerIndividual", method = RequestMethod.POST)
    BaseOutput<Customer> registerIndividual(IndividualCustomer baseInfo);


    /**
     * 更新客户基本信息
     * @param updateInput 客户更新数据
     * @return
     */
    @RequestMapping(value = "/api/customer/update", method = RequestMethod.POST)
    BaseOutput<Customer> update(CustomerUpdateInput updateInput);

    /**
     * 更新用户状态
     * @param customerId 客户ID
     * @param state      状态值
     * @return
     */
    @RequestMapping(value = "/api/customer/updateState", method = {RequestMethod.POST})
    BaseOutput<Customer> updateState(@RequestParam("customerId") Long customerId, @RequestParam("state") Integer state);

    /**
     * 根据证件号检测某个客户在某市场是否已存在
     * @param certificateNumber 客户证件号
     * @param marketId 市场ID
     * @return 如果客户在当前市场已存在，则返回错误(false)信息，如果不存在，则返回客户信息(若客户信息存在)
     */
    @RequestMapping(value = "/api/customer/checkExistByNoAndMarket", method = RequestMethod.POST)
    BaseOutput<Customer> checkExistByNoAndMarket(@RequestParam(value = "certificateNumber") String certificateNumber,@RequestParam(value = "marketId") Long marketId);

}
