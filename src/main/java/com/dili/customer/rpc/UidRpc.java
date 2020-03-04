package com.dili.customer.rpc;

import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/3/3 17:22
 */
@FeignClient(name = "dili-uid")
public interface UidRpc {

    /**
     * 根据业务类型获取业务号
     * @param type
     * @return
     */
    @RequestMapping(value = "/api/bizNumber", method = RequestMethod.POST)
    BaseOutput<String> bizNumber(@RequestParam(value = "type") String type);
}
