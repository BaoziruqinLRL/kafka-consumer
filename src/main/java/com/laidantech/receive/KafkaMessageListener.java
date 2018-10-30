package com.laidantech.receive;

import com.laidantech.executor.KafkaBusinessExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description: kafka消息监听器
 * @Author: lirl
 * @Create: 2018-10-23 14:32
 */
@Component
@Slf4j
public class KafkaMessageListener {

    private Map<String, KafkaBusinessExecutor> executorMap;

    public void setExecutorMap(Map<String, KafkaBusinessExecutor> executorMap) {
        this.executorMap = executorMap;
    }

    @Value("${kafka.consumer.topic}")
    private String topic;

    @KafkaListener(topics = "${kafka.consumer.topic}",containerFactory = "batchContainerFactory")
    public void listen(List datas, Acknowledgment ack){
        try {
            datas.forEach(data -> {
                // 判断是否NULL
                if (data != null) {
                    // 交由执行器处理消息
                    executorMap.get(topic).exec(data);
                }
            });
        }catch (Exception ex) {
            log.error("kafkaMessage process exception, check the executor by {}.",topic);
        }finally {
            // 无论处理是否成功都提交偏移量，避免重复异常或重复处理
            ack.acknowledge();
        }
    }
}

