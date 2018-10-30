package com.laidantech.bean;

import com.laidantech.serializer.DefaultDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: lirl
 * @Create: 2018-10-23 22:13
 */
@Component
public class KafkaDefaultConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String host;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.listener.concurrency}")
    private Integer concurrency;

    private Class keyDeserializerClass;

    private Class valueDeserializerClass;

    @Resource
    private SpringContext context;

    public void setKeyDeserializerClass(Class keyDeserializerClass) {
        this.keyDeserializerClass = keyDeserializerClass;
    }

    public void setValueDeserializerClass(Class valueDeserializerClass) {
        this.valueDeserializerClass = valueDeserializerClass;
        // 设置了序列化类后将bean进行更新，更新其序列化器
        ConcurrentKafkaListenerContainerFactory container = (ConcurrentKafkaListenerContainerFactory) context.getBean("batchContainerFactory");
        container.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerProps()));
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>(5);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, (keyDeserializerClass==null)? DefaultDeserializer.class:keyDeserializerClass);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, (valueDeserializerClass==null)? DefaultDeserializer.class:valueDeserializerClass);
        return props;
    }

    @Bean("batchContainerFactory")
    public ConcurrentKafkaListenerContainerFactory listenerContainer() {
        ConcurrentKafkaListenerContainerFactory container = new ConcurrentKafkaListenerContainerFactory();
        // 设置ack模式为MANUAL_IMMEDIATE
        container.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);
        //设置并发量，小于或等于Topic的分区数
        container.setConcurrency(concurrency);
        //设置为批量监听
        container.setBatchListener(true);
        container.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerProps()));
        return container;
    }

}
