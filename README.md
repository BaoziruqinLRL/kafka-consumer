# kafka-consumer
kafka消费者，默认连接localhost:9092,需要实现执行器和执行器配置
### 使用方式

通过如下方式实现配置，配置好之后即可正常使用
```java
// kafka监听器
@Resource
private KafkaMessageListener kafkaMessageListener;

// 全集配置
@Resource
private GlobalConfig globalConfig;

// 上传数据回调执行器，执行器都必须实现KafkaBusinessExecutor接口
@Resource(type = KafkaUploadExecutor.class)
private KafkaBusinessExecutor uploadExecutor;

// 通用数据执行器
@Resource(type = KafkaRestInfoNoticeExecutor.class)
private KafkaBusinessExecutor restInfoExecitor;

@SuppressWarnings("unchecked")
@PostConstruct
public void initStrategy(){
    // 设置kafka业务执行器，两个执行器分别映射自己的topic
    var kafkaBusinessExecutorMap = new HashMap<String, KafkaBusinessExecutor>(2);
    kafkaBusinessExecutorMap.put(globalConfig.getFeedBackTopic(), uploadExecutor);
    kafkaBusinessExecutorMap.put(globalConfig.getBaseInfoModifiedTopic(),restInfoExecitor);
    // 将执行器传入监听器中，当收到对应topic消息时自动调用执行器
    kafkaMessageListener.setExecutorMap(kafkaBusinessExecutorMap);
}
```
