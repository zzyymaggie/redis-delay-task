# 简介
应用场景：用户登录3s后，给一个欢迎提示。

这里采用zset数据结构实现redis实现的定时任务调度。key为op_id,score为当前时间戳。
通过定时任务将到期的任务取出来，通知消费者。

# 接入指导
1. 初始化Bean

```java
    @Bean
    public DelayTaskConsumer delayTaskConsumer(RedisTemplate<String, String> redisTemplate, MessageListenerImpl messageListenerImpl) {
        DelayTaskConsumer delayTaskConsumer =  new DelayTaskConsumer(redisTemplate);
        delayTaskConsumer.setMessageListener(messageListenerImpl);
        return delayTaskConsumer;
    }

    @Bean
    public DelayTaskProducer delayTaskProducer(RedisTemplate<String, String> redisTemplate) {
        return new DelayTaskProducer(redisTemplate);
    }
```
DelayTaskConsumer参数解释：

- 默认消息名称：zzyymaggie:delay_task_queue，可以修改queue参数修改。
- 默认轮询频率：500ms，可以通过delayInMillSeconds参数修改。
- 默认从第0个开始消费1个元素返回，可以通过参数offset,limit参数修改。
- 这里必须传入RedisTemplate<String,String>，因为一般我们初始化的RedisTemplate<String,Object> value采用json序列化的，这里需要RedisString序列化。



2. 生产消息

```java
delayTaskProducer.produce(newsId, timeStamp);
```

- 第一个参数是op_id;
- 第二个参数为当前时间戳。

3.消费消息
在合适的时机开启消费即可，例如初始化后执行
```java
  @PostConstruct
    private void start() {
        delayTaskConsumer.start();
    }
```
当返回List不为空的时候就会触发MessageListener.consumeMessage事件。

# 支持版本
- jdk1.7+
- Spring4.1.6+, springboot1.x和springboot2.x均支持