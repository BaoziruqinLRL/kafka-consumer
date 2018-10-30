package com.laidantech.serializer;

import com.alibaba.fastjson.JSON;
import com.laidantech.data.KfkBusinessData;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 自定义默认消息序列化器
 * @Author: lirl
 * @Create: 2018-10-23 23:07
 */
@Slf4j
public class DefaultDeserializer extends AbstractDeserializer<KfkBusinessData> { ;

    @Override
    protected KfkBusinessData des(String topic, byte[] data, String encoding) {
        try {
            if (data == null) {
                return null;
            } else {
                String dataStr = new String(data,encoding);
                return JSON.parseObject(dataStr, KfkBusinessData.class);
            }
        } catch (Exception e) {
            log.error("Error when deserializing byte[] {} to KfkBusinessData due to unsupported encoding {}.",data,encoding);
            return null;
        }
    }
}
