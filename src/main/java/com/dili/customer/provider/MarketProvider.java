package com.dili.customer.provider;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONPath;
import com.dili.customer.rpc.MarketRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.provider.BatchDisplayTextProviderAdaptor;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.domain.dto.FirmDto;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/24 15:38
 */
@Component
@Scope("prototype")
public class MarketProvider extends BatchDisplayTextProviderAdaptor {

    @Autowired
    private MarketRpc marketRpc;

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        BaseOutput<List<Firm>> baseOutput = marketRpc.listByExample(DTOUtils.newInstance(FirmDto.class));
        List<ValuePair<?>> resultList = Lists.newArrayList();
        if (baseOutput.isSuccess() && CollectionUtil.isNotEmpty(baseOutput.getData())) {
            resultList.addAll(baseOutput.getData().stream().map(f -> {
                return (ValuePair<?>) new ValuePairImpl(f.getName(), f.getCode());
            }).collect(Collectors.toCollection(() -> new ArrayList<ValuePair<?>>())));
        }
        return resultList;
    }

    @Override
    protected List getFkList(List<String> relationIds, Map metaMap) {
        if (relationIds != null) {
            List<Long> idList = relationIds.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(t -> Long.valueOf(t))
                    .collect(Collectors.toList());
            if (!idList.isEmpty()) {
                FirmDto firmDto = DTOUtils.newDTO(FirmDto.class);
                firmDto.setIdList(idList);
                BaseOutput<List<Firm>> output = marketRpc.listByExample(firmDto);
                return output.isSuccess() ? output.getData() : null;
            }
        }
        return null;
    }

    @Override
    protected Map<String, String> getEscapeFileds(Map metaMap) {
        if(metaMap.get(ESCAPE_FILEDS_KEY) instanceof Map){
            return (Map)metaMap.get(ESCAPE_FILEDS_KEY);
        }else {
            Map<String, String> map = new HashMap<>();
            map.put(metaMap.get(FIELD_KEY).toString(), "name");
            return map;
        }
    }

    @Override
    protected boolean ignoreCaseToRef(Map metaMap) {
        return true;
    }

    /**
     * 关联(数据库)表的主键的字段名
     * 默认取id，子类可自行实现
     * @return
     */
    @Override
    protected String getRelationTablePkField(Map metaMap) {
        return "id";
    }
}
