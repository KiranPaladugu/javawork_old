import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolExecutor {
    private int poolSize = 10;
    private int maxPoolSize = 50;
    private long keepAliveTime = 10;
    private ThreadPoolExecutor threadPool = null;
    private final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
            100000);

    public MyThreadPoolExecutor() {
        threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize,
                keepAliveTime, TimeUnit.SECONDS, queue);
        threadPool.setRejectedExecutionHandler(new RejectedExecutionHandler() {

            public void rejectedExecution(Runnable runnable,
                    ThreadPoolExecutor threadPoolExecutor) {
                System.out
                        .println("Execution rejected. Please try restarting the application.");
            }

        });
    }

    public void runTask(Runnable task) {
        threadPool.execute(task);
    }

    public void shutDown() {
        threadPool.shutdownNow();
    }
    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPoolExecutor threadPool) {
        this.threadPool = threadPool;
    }

    public static void main(String[] args) {
        MyThreadPoolExecutor mtpe = new MyThreadPoolExecutor();
        for (int i = 0; i < 1000; i++) {
            final int j = i;
            mtpe.runTask(new Runnable() {

                public void run() {
                    System.out.println(j);
                }

            });
        }
    }
}
