package com.dili.customer.service;

import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/24 18:02
 */
public interface UserService {

    /**
     * 根据条件查询市场
     * @param user
     * @return
     */
    List<User> listByExample(User user);


    /**
     * 获取当前用户所属市场的所有用户
     * @param realName 真实名称 模糊搜索
     * @return
     */
    List<User> getCurrentMarketUser(String realName);

    /**
     * 通过ID获取用户信息
     * @param userId
     * @return
     */
    Optional<User> getUserById(Long userId);

    /**
     * 根据ID集合批量获取用户信息
     * @param ids 用户id集合
     * @return
     */
    List<User> listUserByIds(List<String> ids);
}
