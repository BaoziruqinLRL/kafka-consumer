package com.laidantech.executor;

/**
 * @Description: kafka业务执行接口
 * @Author: lirl
 * @Create: 2018-10-23 14:46
 */
public interface KafkaBusinessExecutor<T> {

    /**
     * 业务执行接口，由子类实现
     * @param msg kafka接收到的消息
     */
    void exec(T msg);
}
