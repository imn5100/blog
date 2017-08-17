package com.shaw.util;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 异步任务调用线程池工具
 */
public enum ThreadPoolManager {
    INSTANCE; // 唯一的实例
    private int POOL_SIZE_CORE = Runtime.getRuntime().availableProcessors() * 4; // 核心线程池数量 ，添加任务时，当前线程小于该值，直接创建线程运行
    private int POOL_SIZE_MAX = 200; // 线程池最大数量数，核心线程池满，进入队列，队列满，判断当前线程<该值 创建线程运行 >该值 使用拒绝策略
    private int TIME_KEEP_ALIVE = 5; // 线程允许空闲时间  当前运行线程>code数量且线程池中的某个线程 闲置时间>该时间，该线程被停止。
    private int SIZE_WORK_QUEUE = 20; // 线程池缓存队列大小  线程数量大于核心线程池数 则如队列。
    // 线程执行类 属于google Guava包下，在Future基础上对线程池的封装，
    ListeningExecutorService executorService;
    //是否开启记录日志
    public static final boolean OPEN_LOGGER = false;
    //日志记录类
    private Logger logger = LoggerFactory.getLogger(ThreadPoolManager.class);

    // 通过构造函数完成 线程池初始化和装饰。
    ThreadPoolManager() {
        // 构造线程池执行器
        final ThreadPoolExecutor threadPool =
                new ThreadPoolExecutor(
                        POOL_SIZE_CORE,
                        POOL_SIZE_MAX,
                        TIME_KEEP_ALIVE,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<Runnable>(SIZE_WORK_QUEUE),
                        new RejectedExecution());
        // 包装线程池执行器
        executorService = MoreExecutors.listeningDecorator(threadPool);
        // 打开线程池日志
        if (OPEN_LOGGER) {
            //日志输出定时任务
            Runnable mAccessBufferThread = new Runnable() {
                @Override
                public void run() {
                    logger.info(
                            String.format(
                                    "treadPool thread message activeCount: %s,CompletedTaskCount: %s,taskCount: %s, queue_size: %s",
                                    threadPool.getActiveCount(),
                                    threadPool.getCompletedTaskCount(),
                                    threadPool.getTaskCount(),
                                    threadPool.getQueue().size()));
                }
            };
            // 定时任务执行器
            final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            // 将日志输出放入定时轮询任务执行器。延迟0秒。每10秒输出一次线程池状态
            scheduler.scheduleAtFixedRate(mAccessBufferThread, 0, 10, TimeUnit.SECONDS);
        }
    }

    /**
     * 向线程池中添加Callable任务
     */
    public <T> ListenableFuture<T> addExecuteTask(Callable<T> task) {
        return executorService.submit(task);
    }

    /**
     * 向线程池中添加Runnable任务
     */
    public ListenableFuture<?> addExecuteTask(Runnable task) {
        return executorService.submit(task);
    }

    /**
     * 异步执行
     */
    public void execute(Runnable task) {
        executorService.execute(task);
    }

    /**
     * 线程池拒绝任务处理策略。即进入放入队列时被阻塞。
     */
    public class RejectedExecution implements RejectedExecutionHandler {
        public RejectedExecution() {
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            logger.info("Reject msg: {} {}", r.toString(), executor.getActiveCount());
        }
    }
}
