package com.dili.customer.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.customer.enums.CustomerEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.dto.DepartmentDto;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/27 17:51
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentRpc departmentRpc;

    /**
     * 根据市场ID获取该市场中的部门
     * @param marketId 客户ID
     * @return BaseOutput
     */
    @RequestMapping(value = "/listByMarketId.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput listByMarketId(Long marketId) {
        if (Objects.isNull(marketId)) {
            return BaseOutput.failure("必要参数丢失");
        }
        DepartmentDto dto = DTOUtils.newInstance(DepartmentDto.class);
        dto.setFirmId(marketId);
        return departmentRpc.listByExample(dto);
    }
}
