package com.april.house.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    /**
     * 线程池包括以下四个基本组成部分
     * 1. 线程池管理器(ThreadPool)： 用于创建并管理线程池，包括创建线程池，销毁线程池，添加新任务
     * 2. 工作线程(PoolWorker)： 线程池中的线程，在没有任务时处于等待状态，可以循环执行任务
     * 3. 任务接口(Task)： 每个任务必须实现的接口，以供工作线程调度任务执行，
     *    它主要规定了任务的入口，任务执行完后的收尾工作，任务的执行状态等。
     * 4. 任务队列(TaskQueue): 用于存放没有处理的任务，提供一种缓冲机制。
     *
     * 线程池的主要参数
     * corePoolSize: 核心线程数
     *   - 核心线程会一直存活，即使没有任务需要执行
     *   - 当线程数小于核心线程数，即使有线程空闲，线程池也会优先创建新线程处理
     *   - 设置allowCoreThreadTimeout=true(默认为false),核心线程会超时关闭
     *
     * maxPoolSize: 最大线程数
     *   - 当线程数 >= corePoolSize, 且任务队列已满时，线程池会创建新线程来处理任务
     *   - 当线程数 = maxPoolSize，且任务队列已满时，线程池会拒绝处理任务而抛出异常
     *
     * keepAliveTime: 线程空闲时间
     *   - 当线程空闲时间达到keepAliveTime时，线程会退出，直到线程数量 = corePoolSize
     *   - 如果allowCoreThreadTimeout=true，则会直到线程数量 = 0
     *
     * workQueue: 任务阻塞队列，用来存储等待执行的任务。
     *            这个参数的选择也很重要，会对线程池的运行过程产生重大影响。常用的阻塞队列有以下几种选择。
     *   - ArrayBlockingQueue
     *   - LinkedBlockingQueue
     *   - SynchronousQueue
     *
     * threadFactory: 线程工厂，主要用来创建线程
     *
     * rejectedExecutionHandler: 任务拒绝处理器，两种情况会拒绝处理任务
     *   - 当线程数已经达到maxPoolSize时，且阻塞队列已满，会拒绝执行新任务
     *   - 当线程池被调用shutdown()后，会等待线程池里的任务执行完毕，再shutdown。
     *     如果在调用shutdown()和线程池真正shutdown之间提交任务，会拒绝新任务。
     *
     * 当拒绝处理任务时线程池会调用rejectedExecutionHandler来处理这个任务。
     * 如果没有设置，默认是AbortPolicy，会抛出异常。ThreadPoolExecutor类有几个内部实现类来处理这种情况
     *  - AbortPolicy 丢弃任务，抛出运行时异常
     *  - CallerRunsPolicy 执行任务
     *  - DiscardPolicy 忽视，什么都不会发生
     *  - DiscardOldestPolicy 从队列中踢出最先进入队列(最后一个执行）的任务
     *  - 实现RejectedExecutionHandler接口，可自定义处理器
     *
     */

    @Value("${thread.pool.core-pool-size}")
    private int corePoolSize;
    @Value("${thread.pool.max-pool-size}")
    private int maxPoolSize;
    @Value("${thread.pool.queue-capacity}")
    private int queueCapacity;


    @Bean(name = "defaultThreadPool")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        return executor;
    }

    @Bean(name = "schedulePoolExecutor")
    public Executor schedulePoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(maxPoolSize);
        executor.setCorePoolSize(corePoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("ScheduledExecutor-");
        executor.initialize();
        return executor;
    }
}
