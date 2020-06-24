package com.dili.customer.provider;

import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <B>Description</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/20 20:23
 */
@Component
@Scope("prototype")
public class GenderProvider implements ValueProvider {

    private static final List<ValuePair<?>> BUFFER;

    static {
        BUFFER = Lists.newArrayList();
        BUFFER.addAll(Stream.of(CustomerEnum.Gender.values())
                .map(e -> new ValuePairImpl<String>(e.getValue(), String.valueOf(e.getCode())))
                .collect(Collectors.toList()));
    }

    @Override
    public List<ValuePair<?>> getLookupList(Object obj, Map metaMap, FieldMeta fieldMeta) {
        return BUFFER;
    }

    @Override
    public String getDisplayText(Object obj, Map metaMap, FieldMeta fieldMeta) {
        if (obj == null || "".equals(obj)) {
            return null;
        }
        CustomerEnum.Gender instance = CustomerEnum.Gender.getInstance(Integer.valueOf(obj.toString()));
        if (Objects.nonNull(instance)) {
            return instance.getValue();
        }
        return null;
    }
}
