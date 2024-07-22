import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskExecutorImpl implements TaskExecutor{


    private ExecutorService executorService;

    public TaskExecutorImpl(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public <String> Future<String> submit(Task<String> task) {
        return executorService.submit(task.taskAction());
    }

}
