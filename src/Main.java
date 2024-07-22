import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

/**
 * The entry point for the service is Task Executor interface. The interface is defined bellow including its dependencies.
 * The service is required to implement the following behaviors:
 * Tasks can be submitted concurrently. Task submission should not block the submitter.
 * Tasks are executed asynchronously and concurrently. Maximum allowed concurrency may be restricted.
 * Once task is finished, its results can be retrieved from the Future received during task submission.
 * The order of tasks must be preserved.
 * The first task submitted must be the first task started.
 * The task result should be available as soon as possible after the task completes.
 * Tasks sharing the same TaskGroup must not run concurrently.
 * Additional implementation requirements:
 * The implementation must run on OpenJDK 17.
 * No third-party libraries can be used.
 * The provided interfaces and classes must not be modified.
 */

public class Main {
    public static final int concurrencyLevel = 10;

    public static final Random RANDOM = new Random();

    public static void main(String[] args) {
        ExecutorService    executorService = Executors.newFixedThreadPool(concurrencyLevel);
        TaskExecutor       taskExecutor    = new TaskExecutorImpl(executorService);
        TaskService        taskService     = new TaskService(executorService, taskExecutor);
        List<Task<String>> inputTasks      = createTasks();
        taskService.submit(inputTasks);
        System.out.println("Task submitted: " + inputTasks.size());

        List<Future<String>> futures = taskService.getFutures();
        System.out.println("Future size: " + futures.size());
        try {
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Exception occured:" + e.getMessage());
        } finally {
            executorService.shutdown();
        }
    }

    private static List<Task<String>> createTasks() {
        TaskGroup          taskGroup1 = new TaskGroup(UUID.randomUUID());
        TaskGroup          taskGroup2 = new TaskGroup(UUID.randomUUID());
        TaskGroup          taskGroup3 = new TaskGroup(UUID.randomUUID());
        TaskGroup          taskGroup4 = new TaskGroup(UUID.randomUUID());
        List<TaskGroup>    taskGroups = List.of(taskGroup1, taskGroup2, taskGroup3, taskGroup4);
        List<Task<String>> tasks      = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Task<String> sometask = new Task<>(UUID.randomUUID(), selectRandomTaskGroup(taskGroups), selectRandomTaskType(), selectRandomCallable());
            tasks.add(sometask);
            selectRandomTaskGroup(taskGroups);
        }
        return tasks;
    }

    private static Callable<String> selectRandomCallable() {
        return () -> "Callable with UUID: " + UUID.randomUUID();
    }

    private static TaskType selectRandomTaskType() {
        int random = generateRandomInteger() % 2;
        return random == 0 ? TaskType.READ : TaskType.WRITE;
    }

    private static TaskGroup selectRandomTaskGroup(List<TaskGroup> taskGroups) {
        int randomIndex = generateRandomInteger() % (taskGroups.size());
        return taskGroups.get(randomIndex);
    }

    private static int generateRandomInteger() {
        return Math.abs(RANDOM.nextInt());
    }
}