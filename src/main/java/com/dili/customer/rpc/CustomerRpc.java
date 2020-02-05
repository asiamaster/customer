package com.dili.customer.rpc;

import com.dili.customer.domain.Address;
import com.dili.customer.domain.Contacts;
import com.dili.customer.domain.Customer;
import com.dili.customer.domain.dto.CustomerQuery;
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
@FeignClient(name = "customer-service")
public interface CustomerRpc {

    /**
     * 获取客户列表信息
     * @param customer
     * @return
     */
    @RequestMapping(value = "/api/customer/listPage.action", method = RequestMethod.POST)
    PageOutput<List<Customer>> listPage(CustomerQuery customer);

    /**
     * 获取客户列表信息
     * @param customer
     * @return
     */
    @RequestMapping(value = "/api/customer/list.action", method = RequestMethod.POST)
    BaseOutput<List<Customer>> list(CustomerQuery customer);

    /**
     * 企业用户注册
     * @param baseInfo
     * @return
     */
    @RequestMapping(value = "/api/customer/registerEnterprise.action", method = RequestMethod.POST)
    BaseOutput registerEnterprise(EnterpriseCustomer baseInfo);

    /**
     * 个人用户注册
     * @param baseInfo
     * @return
     */
    @RequestMapping(value = "/api/customer/registerIndividual.action", method = RequestMethod.POST)
    BaseOutput registerIndividual(IndividualCustomer baseInfo);


    /**
     * 保存客户基本信息
     * @param baseInfo 客户基本信息
     * @return
     */
    @RequestMapping(value = "/api/customer/saveBaseInfo.action", method = RequestMethod.POST)
    BaseOutput saveBaseInfo(Customer baseInfo);

    /**
     * 保存客户证件相关
     * @param certificateInfo 客户证件相关信息
     * @return
     */
    @RequestMapping(value = "/api/customer/saveCertificateInfo.action", method = RequestMethod.POST)
    BaseOutput saveCertificateInfo(Customer certificateInfo);

    /**
     * 保存客户联系人信息
     * @param customerContacts 客户联系人
     * @return
     */
    @RequestMapping(value = "/api/contacts/saveContacts.action", method = RequestMethod.POST)
    List<Contacts> saveContacts(Contacts customerContacts);

    /**
     * 根据客户ID查询该客户的联系人信息
     * @param customerId 客户ID
     * @return
     */
    @RequestMapping(value = "/api/contacts/listAllContacts.action", method = RequestMethod.POST)
    List<Contacts> listAllContacts(@RequestParam("customerId") Long customerId);

    /**
     * 保存客户地址信息
     * @param customerAddress 客户地址
     * @return
     */
    @RequestMapping(value = "/api/address/saveAddress.action", method = RequestMethod.POST)
    List<Contacts> saveAddress(Address customerAddress);

    /**
     * 根据客户ID查询该客户的地址信息
     * @param customerId 客户ID
     * @return
     */
    @RequestMapping(value = "/api/address/listAllAddress.action", method = RequestMethod.POST)
    List<Address> listAllAddress(@RequestParam("customerId") Long customerId);
}
