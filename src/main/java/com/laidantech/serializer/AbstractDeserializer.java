package com.laidantech.serializer;

import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * @Description:
 * @Author: lirl
 * @Create: 2018-10-23 23:36
 */
public abstract class AbstractDeserializer<T> implements Deserializer<T> {

    private String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String propertyName = isKey ? "key.deserializer.encoding" : "value.deserializer.encoding";
        Object encodingValue = configs.get(propertyName);
        if (encodingValue == null) {
            encodingValue = configs.get("deserializer.encoding");
        }
        if (encodingValue != null && encodingValue instanceof String) {
            encoding = (String) encodingValue;
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        return des(topic,data,encoding);
    }

    @Override
    public void close() {

    }

    /**
     * 将data转换为T
     * @param topic 消息主题
     * @param data 消息数据
     * @param encoding 编码方式，UTF-8
     * @return 返回解析后的数据
     */
    protected abstract T des(String topic, byte[] data, String encoding);
}
