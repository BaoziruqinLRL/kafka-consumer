package com.baozi.serializer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 自定义默认消息序列化器
 * @Author: lirl
 * @Create: 2018-10-23 23:07
 */
@Slf4j
public class DefaultDeserializer<T> extends AbstractDeserializer<T> { ;

    private Class<T> businessClazz;

    public DefaultDeserializer(Class<T> businessClazz){
        this.businessClazz = businessClazz;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T des(String topic, byte[] data, String encoding) {
        try {
            if (data == null) {
                return null;
            } else {
                String dataStr = new String(data,encoding);
                return JSON.parseObject(dataStr,businessClazz);
            }
        } catch (Exception e) {
            log.error("Error when deserializing byte[] {} to source T due to unsupported encoding {}.",data,encoding);
            return null;
        }
    }
}
