import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TaskExecutorImpl implements TaskExecutor {


    private ExecutorService executorService;

    public TaskExecutorImpl(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public <String> Future<String> submit(Task<String> task) {
        return executorService.submit(task.taskAction());
    }

}
