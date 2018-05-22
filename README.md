# 分布式秒杀系统
[原项目地址](https://gitee.com/52itstyle/spring-boot-seckill)
[原项目博客](https://blog.52itstyle.com/archives/2853/)

## 开发环境

JDK1.7、Maven、Mysql、Eclipse、SpringBoot1.5.10、zookeeper3.4.6、kafka_2.11、redis-2.8.4、curator-2.10.0


#### 项目介绍

SpringBoot开发案例从0到1构建分布式秒杀系统，项目案例基本成型，逐步完善中。


## 秒杀场景

秒杀场景无非就是多个用户在同时抢购一件或者多件商品，专用词汇就是所谓的高并发。


## 业务特点

- 瞬间高并发、电脑旁边的小哥哥、小姐姐们如超市哄抢的大妈一般，疯狂的点着鼠标
- 库存少、便宜、稀缺限量，值得大家去抢购，如苹果肾，小米粉


## 用户规模

用户规模可大可小，几百或者上千人的活动单体架构足以可以应付，简单的加锁、进程内队列就可以轻松搞定。一旦上升到百万、千万级别的规模就要考虑分布式集群来应对瞬时高并发。

## 秒杀架构

![输入图片说明](https://gitee.com/uploads/images/2018/0515/184617_c7e13059_87650.png "秒杀架构.png")

#### 架构层级

- 一般商家在做活动的时候，经常会遇到各种不怀好意的DDOS攻击(利用无辜的吃瓜群众夺取资源)，导致真正的我们无法获得服务！所以说高防IP还是很有必要的。

- 搞活动就意味着人多，接入SLB，对多台云服务器进行流量分发，可以通过流量分发扩展应用系统对外的服务能力，通过消除单点故障提升应用系统的可用性。

- 基于SLB价格以及灵活性考虑后面我们接入Nginx做限流分发，来保障后端服务的正常运行。

- 后端秒杀业务逻辑，基于Redis 或者 Zookeeper 分布式锁，Kafka 或者 Redis 做消息队列，DRDS数据库中间件实现数据的读写分离。

#### 优化思路

- 分流、分流、分流，重要的事情说三遍，再牛逼的机器也抵挡不住高级别的并发。

- 限流、限流、限流，毕竟秒杀商品有限，防刷的前提下没有绝对的公平，根据每个服务的负载能力，设定流量极限。

- 缓存、缓存、缓存、尽量不要让大量请求穿透到DB层，活动开始前商品信息可以推送至分布式缓存。

- 异步、异步、异步，分析并识别出可以异步处理的逻辑，比如日志，缩短系统响应时间。

- 主备、主备、主备，如果有条件做好主备容灾方案也是非常有必要的(参考某年锤子的活动被攻击)。

- 最后，为了支撑更高的并发，追求更好的性能，可以对服务器的部署模型进行优化，部分请求走正常的秒杀流程，部分请求直接返回秒杀失败，缺点是开发部署时需要维护两套逻辑。

#### 分层优化

- 前端优化：活动开始前生成静态商品页面推送缓存和CDN，静态文件(JS/CSS)请求推送至文件服务器和CDN。
- 网络优化：如果是全国用户，最好是BGP多线机房，减少网络延迟。
- 应用服务优化：Nginx最佳配置、Tomcat连接池优化、数据库配置优化、数据库连接池优化。

## 全链路压测

- 分析需压测业务场景涉及系统
- 协调各个压测系统资源并搭建压测环境
- 压测数据隔离以及监控(响应时间、吞吐量、错误率等数据以图表形式实时显示)
- 压测结果统计(平均响应时间、平均吞吐量等数据以图表形式在测试结束后显示)
- 优化单个系统性能、关联流程以及整个业务流程

整个压测优化过程就是一个不断优化不断改进的过程，事先通过测试不断发现问题，优化系统，避免问题，指定应急方案，才能让系统的稳定性和性能都得到质的提升。

## 代码案例

可能秒杀架构原理大家都懂，网上也有不少实现方式，但大多都是文字的描述，告诉你如何如何，什么加锁、缓存、队列之类。但很少全面有的案例告诉你如何去做，既然是从0到1，希望以下代码案例可以帮助到你。当然最终落实到生产，还有很长的路要走，要根据自己的业务进行编码，实施并部署。

你将会在代码案例中学到以下知识：
- 如何大家SpringBoot微服务
- ThreadPoolExecutor线程池的使用
- ReentrantLock和Synchronized的使用场景
- 数据库锁机制(悲观锁、乐观锁)
- 分布式锁(RedissLock、Zookeeper)
- 进程内消息队列(LinkedBlockingQueue、ArrayBlockingQueue、ConcurrentLinkedQueue)
- 分布式消息队列(Redis、Kafka)
- AOP实现切面锁

#### 代码结构：
```
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─itstyle
│  │  │          └─seckill
│  │  │              │  Application.java
│  │  │              │  
│  │  │              ├─common
│  │  │              │  ├─api
│  │  │              │  │      SwaggerConfig.java 
│  │  │              │  │      
│  │  │              │  ├─config
│  │  │              │  │      IndexController.java  
│  │  │              │  │      
│  │  │              │  ├─dynamicquery   
│  │  │              │  │      DynamicQuery.java
│  │  │              │  │      DynamicQueryImpl.java
│  │  │              │  │      NativeQueryResultEntity.java
│  │  │              │  │      
│  │  │              │  ├─entity   
│  │  │              │  │      Result.java
│  │  │              │  │      Seckill.java
│  │  │              │  │      SuccessKilled.java
│  │  │              │  │      
│  │  │              │  ├─enums
│  │  │              │  │      SeckillStatEnum.java
│  │  │              │  │      
│  │  │              │  ├─interceptor
│  │  │              │  │      MyAdapter.java
│  │  │              │  │      
│  │  │              │  └─redis
│  │  │              │          RedisConfig.java
│  │  │              │          RedisUtil.java
│  │  │              │          
│  │  │              ├─distributedlock
│  │  │              │  ├─redis
│  │  │              │  │      RedissLockDemo.java
│  │  │              │  │      RedissLockUtil.java
│  │  │              │  │      RedissonAutoConfiguration.java
│  │  │              │  │      RedissonProperties.java
│  │  │              │  │      
│  │  │              │  └─zookeeper
│  │  │              │          ZkLockUtil.java
│  │  │              │          
│  │  │              ├─queue
│  │  │              │  ├─jvm
│  │  │              │  │      SeckillQueue.java
│  │  │              │  │      TaskRunner.java
│  │  │              │  │      
│  │  │              │  ├─kafka
│  │  │              │  │      KafkaConsumer.java
│  │  │              │  │      KafkaSender.java
│  │  │              │  │      
│  │  │              │  └─redis
│  │  │              │          RedisConsumer.java
│  │  │              │          RedisSender.java
│  │  │              │          RedisSubListenerConfig.java
│  │  │              │          
│  │  │              ├─repository
│  │  │              │      SeckillRepository.java
│  │  │              │      
│  │  │              ├─service
│  │  │              │  │  ISeckillDistributedService.java
│  │  │              │  │  ISeckillService.java
│  │  │              │  │  
│  │  │              │  └─impl
│  │  │              │          SeckillDistributedServiceImpl.java
│  │  │              │          SeckillServiceImpl.java
│  │  │              │          
│  │  │              └─web
│  │  │                      SeckillController.java
│  │  │                      SeckillDistributedController.java
│  │  │                      
│  │  ├─resources
│  │  │  │  application.properties
│  │  │  │  logback-spring.xml
│  │  │  │  
│  │  │  ├─sql
│  │  │  │      seckill.sql
│  │  │  │      
│  │  │  ├─static
│  │  │  └─templates
│  │  └─webapp

```

## 分布式锁应该具备哪些条件

- 在分布式系统环境下，一个方法在同一时间只能被一个机器的一个线程执行； 
- 高可用的获取锁与释放锁； 
- 高性能的获取锁与释放锁； 
- 具备可重入特性； 
- 具备锁失效机制，防止死锁； 
- 具备非阻塞锁特性，即没有获取到锁将直接返回获取锁失败。

## 分布式锁

- 基于数据库实现分布式锁； 
- 基于缓存（Redis等）实现分布式锁； 
- 基于Zookeeper实现分布式锁；

## 思考改进

- 如何防止单个用户重复秒杀下单？
- 如何防止恶意调用秒杀接口？
- 如果用户秒杀成功，一直不支付该怎么办？
- 消息队列处理完成后，如果异步通知给用户秒杀成功？
- 如何保障 Redis、Zookeeper 、Kafka 服务的正常运行(高可用)？
- 高并发下秒杀业务如何做到不影响其他业务(隔离性)？


## 可供参考



[SpringBoot开发案例从0到1构建分布式秒杀系统](https://blog.52itstyle.com/archives/2853/ "SpringBoot开发案例从0到1构建分布式秒杀系统")

[SpringBoot开发案例之整合Kafka实现消息队列](https://blog.52itstyle.com/archives/2868/ "SpringBoot开发案例之整合Kafka实现消息队列")

[从构建分布式秒杀系统聊聊线程池](https://blog.52itstyle.com/archives/2894/ "从构建分布式秒杀系统聊聊线程池")

[Nginx学习之负载均衡](https://blog.52itstyle.com/archives/623/ "Nginx学习之负载均衡")

[Nginx学习之如何防止流量攻击](https://blog.52itstyle.com/archives/775/ "Nginx学习之如何防止流量攻击")

[Nginx学习之缓存配置](https://blog.52itstyle.com/archives/826/ "Nginx学习之缓存配置")

[Nginx学习之HTTP/2.0配置](https://blog.52itstyle.com/archives/1524/ "Nginx学习之HTTP/2.0配置")

[企业云解析DNS](https://blog.52itstyle.com/archives/515/ "企业云解析DNS")

[Linux下搭建ZooKeeper集群](https://blog.52itstyle.com/archives/363/ "Linux下搭建ZooKeeper集群")
