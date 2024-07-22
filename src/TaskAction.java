import java.util.UUID;
import java.util.concurrent.Callable;

public class TaskAction implements Callable<String> {

    @Override
    public String call() throws Exception {
        return "RandomTask: "+ UUID.randomUUID();
    }
}
