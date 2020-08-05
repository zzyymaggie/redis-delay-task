# 简介
redis实现的定时任务，通过生产和消费模式实现。采用zset数据结构存储。key为op_id,score为当前时间戳。
通过定时任务将到期的任务取出来，通过回调注册的接口通知给订阅的消费者。

# 接入指导
### 1. 发布延时任务消息

通过HTTP接口来发布延时任务消息
POST /delay/produce

请求数据包格式规范:
```json
[
    {
        "callbackUrl": "string", //回调接口
        "callbackType": 1,  //回调类型 0:HTTP/1:tars rpc
        "taskinfo":{
            "interval": 0,    //单位，毫秒
            "topic":"String",   //可以理解为消息类型
            "msgBody": "string", //消息内容
            "expiredTime": 123456   // 如果为null或0, 用当前时间+interval
        }
    }
]
```
响应数据包格式规范
```json
[
  {
    "body": "{\"callbackType\":0,\"callbackUrl\":\"string\",\"taskInfo\":{\"expiredTime\":0,\"interval\":200,\"msgBody\":\"string\",\"topic\":\"string\"}}",
    "success": true,  //执行结果
    "message": null   //如果success=false，message显示相应的原因
  }
]
```
输出为json格式，文档编码格式UTF-8

### 示例：
- http回调示例：
```json
[
  {
    "callbackType": 0,
    "callbackUrl": "http://192.168.0.1:8093/delay/feedback",
    "taskInfo": {
      "expiredTime": 0,
      "interval": 100,
      "msgBody": "string",
      "topic": "string"
    }
  }
]
```
- tarsrpc回调示例：
```json
[
  {
    "callbackType": 1,
    "callbackUrl": "DemoApp.DemoServer.FeedbackService@tcp -h 127.0.0.1 -p 17602 -t 60000",
    "taskInfo": {
      "expiredTime": 0,
      "interval": 100,
      "msgBody": "string",
      "topic": "string"
    }
  }
]
```



### 2.调用方需要提供消息回调接口
> 当延时任务到期后会主动触发回调接口，支持HTTP、tarsrpc调用

#### 2.1 HTTP

```POST {callbackUrl}``` 
请求数据包格式规范:
```json
{
  "interval": 0,
  "msgBody": "string",
  "topic": "String",   
  "expiredTime": 123456 //到期时间
}
```
响应数据包格式无要求，判断状态码是否为200

#### 2.2 tarsrpc调用
feedback协议：
```
module robot
{
    struct TaskInfoReq
    {
        0 optional string msgBody;
        1 optional string topic;
        2 optional int interval;
        3 optional long expiredTime;
    };

    interface Feedback {
        int feedback(TaskInfoReq taskInfoReq, out string msg);
    };
};
```
支持直连调用和名字服务调用

# FAQ
Q1： 10ms的延时任务能否正常触发？
>  不一定。当前采用100ms轮询一次，最晚200ms触发一次延时任务。