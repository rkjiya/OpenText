import java.util.concurrent.Future;

/**
 * Task Executor.
 */
public interface TaskExecutor {

    /**
     *
     * @param task Task to be executed by the executor. Must not be null.
     * @return Future for the task asynchronous computation result.
     */
    <T> Future<T> submit(Task<T> task);
}
