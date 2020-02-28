package com.dili.customer.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <B>将枚举类属性字段对应转换成json格式</B>
 * <B>Copyright:本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.</B>
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @date 2020/2/28 14:53
 */
public class EnumUtil {

    /**
     * 枚举类型转换为json格式对象
     * @param enumClass
     * @return
     */
    public static Object toObject(Class<? extends Enum> enumClass) {
        try {
            Method methodValues = enumClass.getMethod("values");
            Object invoke = methodValues.invoke(null);
            int length = java.lang.reflect.Array.getLength(invoke);
            List<Object> values = new ArrayList<Object>();
            for (int i = 0; i < length; i++) {
                values.add(java.lang.reflect.Array.get(invoke, i));
            }
            SerializeConfig config = new SerializeConfig();
            config.configEnumAsJavaBean(enumClass);
            return JSON.parse(JSON.toJSONString(values, config));
        } catch (Exception e) {
            return null;
        }
    }
}
