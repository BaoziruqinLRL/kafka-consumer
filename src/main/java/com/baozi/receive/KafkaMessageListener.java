package com.baozi.receive;

import com.baozi.executor.KafkaBusinessExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
public class KafkaMessageListener<T> {

    private Map<String, KafkaBusinessExecutor<T>> executorMap;

    public void setExecutorMap(Map<String, KafkaBusinessExecutor<T>> executorMap) {
        this.executorMap = executorMap;
    }

    @KafkaListener(topics = {"#{'${kafka.consumer.topic}'.split(',')}"},containerFactory = "batchContainerFactory")
    public void listen(List<ConsumerRecord<T,T>> datas, Acknowledgment ack){
        if (datas != null) {
            datas.forEach(data -> {
                try {
                    T x = data.value();
                    // 交由执行器处理消息
                    executorMap.get(data.topic()).exec(x);
                }catch (Exception ex) {
                    log.error("kafkaMessage process exception, check the executor by topic '{}'. Exception is {}",data.topic(),ex);
                }
            });
            // 无论处理是否成功都提交偏移量，避免重复异常或重复处理
            ack.acknowledge();
        }
    }
}
