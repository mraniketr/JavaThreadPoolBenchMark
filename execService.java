import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


public class execService {

    public static final int OPS = 2000000;
    public static final int OPS_LATENCY = 100;
    public static final int N_THREADS = 2;

    public static void main(String[] args)  {

        System.out.println("newSingleThreadExecutor DUMMY IDEAL- "+ OPS*OPS_LATENCY);
        System.out.println("newVirtualThreadPerTaskExecutor - "+ poolRunner(Executors.newVirtualThreadPerTaskExecutor()));
        System.out.println("newFixedThreadPool - "+ poolRunner(Executors.newFixedThreadPool(N_THREADS)));
        System.out.println("newSingleThreadExecutor - "+ poolRunner(Executors.newSingleThreadExecutor()));
        System.exit(0);

    }

    public static long  poolRunner(ExecutorService executorService){
        long start = System.currentTimeMillis();
        List<Future> futures = new ArrayList<>();
        for(int i = 0; i< OPS; i++) {

            Future<String> res = executorService.submit(() -> {
                try {
                    Thread.sleep(OPS_LATENCY);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//                System.out.println("Thread " + System.currentTimeMillis() + " =" + Thread.currentThread().toString());
                return "DONE";
            });
            futures.add(res);

        }
//        System.out.println("P1 "+(System.currentTimeMillis()-start));
        futures.forEach(f-> {

            try {
                f.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
//        System.out.println("P2 "+(System.currentTimeMillis()-start));
        return (System.currentTimeMillis()-start);
    }

}
