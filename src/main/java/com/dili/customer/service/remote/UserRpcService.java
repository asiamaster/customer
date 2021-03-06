package com.dili.customer.service.remote;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.rpc.UserRpc;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/3 15:57
 */
@Service
public class UserRpcService {

    @Autowired
    private UserRpc userRpc;

    @Autowired
    private FirmRpcService firmRpcService;

    /**
     * 根据条件查询用户信息
     * @param userQuery
     * @return
     */
    public List<User> listByExample(UserQuery userQuery) {
        BaseOutput<List<User>> baseOutput = userRpc.listByExample(userQuery);
        return baseOutput.isSuccess() ? baseOutput.getData() : Collections.emptyList();
    }

    /**
     * 根据姓名模糊获取当前用户有权限的市场中的对应用户
     * @param realName 真实姓名
     * @return
     */
    public List<User> getCurrentAuthMarketUsers(String realName){
        List<Firm> firms = firmRpcService.getCurrentUserFirms();
        if (CollectionUtil.isNotEmpty(firms)){
            UserQuery condition = DTOUtils.newInstance(UserQuery.class);
            condition.setFirmCodes(firms.stream().distinct().map(Firm::getCode).collect(Collectors.toList()));
            if (StrUtil.isNotBlank(realName)) {
                condition.setRealName(realName);
            }
            return listByExample(condition);
        }
        return Collections.emptyList();
    }

    /**
     * 根据真实姓名模糊获取当前市场的用户信息
     * @param realName 真实姓名
     * @return
     */
    public List<User> getCurrentMarketUser(String realName) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (null == userTicket) {
            return Collections.emptyList();
        }
        UserQuery condition = DTOUtils.newInstance(UserQuery.class);
        condition.setFirmCode(userTicket.getFirmCode());
        if (StrUtil.isNotBlank(realName)) {
            condition.setRealName(realName);
        }
        return listByExample(condition);
    }

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return
     */
    public Optional<User> getUserById(Long userId) {
        if (Objects.isNull(userId)) {
            return Optional.empty();
        }
        BaseOutput<User> out = userRpc.get(userId);
        if (out.isSuccess()) {
            User user = out.getData();
            return Optional.ofNullable(user);
        }
        return Optional.empty();
    }

    /**
     * 根据用户ID集批量获取用户信息
     * @param ids id集合
     * @return
     */
    public List<User> listUserByIds(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.EMPTY_LIST;
        }
        BaseOutput<List<User>> baseOutput = userRpc.listUserByIds(ids);
        return baseOutput.isSuccess() ? baseOutput.getData() : Collections.emptyList();
    }
}
