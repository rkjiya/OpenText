import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskService<T> {

     private List<Task<T>> tasks = new ArrayList<>();
     private Collection<Future<T>> futures = new ArrayList<>();

     private ExecutorService executorService;

     private TaskExecutor taskExecutor;

     public TaskService(ExecutorService executorService, TaskExecutor taskExecutor){
         this.executorService = executorService;
         this.taskExecutor = taskExecutor;
     }

      void submit(List<Task<T>> inputTasks){
          tasks.addAll(inputTasks);
          execute();
     }

     private void execute(){
             Map<TaskGroup, List<Task<T>>> tasksMappedByGroup = tasks.stream()
                     .collect(Collectors.groupingBy(task -> task.taskGroup(), Collectors.toList()));
         System.out.println("TaskMap: "+tasksMappedByGroup);
             for(TaskGroup taskGroup : tasksMappedByGroup.keySet()){
                   new Thread(() -> {
                       runTaskSynchronously(tasksMappedByGroup.get(taskGroup));
                   }).start();
             }
         }

    private void runTaskSynchronously(List<Task<T>> tasks) {
        System.out.println("Current Thread: "+Thread.currentThread().getName());
        for(Task<T> task : tasks){
            Future<T> future = taskExecutor.submit(task);
            futures.add(future);
        }
    }

    public List<Future<T>> getFutures(){
         return new ArrayList<>(futures);
    }


}
