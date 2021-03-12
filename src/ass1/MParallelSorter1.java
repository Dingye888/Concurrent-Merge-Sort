package ass1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MParallelSorter1 implements Sorter {

    public static ExecutorService POOL = Executors.newCachedThreadPool();
    public static final int THRESHOLD = 20;

    /**
     * The MParallelSorter1 class uses the merge sort algorithm to sort in parallel using Futures. The benefit of this algorithm-
     * compared to all other ones, is that we can use Futures to sort in parallel. We can create a thread working on one-
     * list while the current one can work on the other recursively. This way we can save about roughly half the time.
     * By using Futures and ExecutorService, we can have better management to our thread pools and create a personalized get-
     * method to manage our exceptions.
     * <p>
     * From implementing merge sort in this way, it helped understood the concept of thread pools and parallelism. If we didn't-
     * have a threshold to delegate to sequential merge sort, the program might result at a slower computation, or even-
     * encounter thread pool exhaustion due to not enough threads. The ordering of task is also important, we always want to-
     * delegate first, then perform to achieve concurrency.
     *
     * @param list - List to be sorted
     * @param <T>  - Comparable type
     * @return - Sorted list
     */
    @Override
    public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
        return mergeSort(new ArrayList<>(list));
    }

    /**
     * Recursive method for splitting list and sorting on two sublist using Futures
     *
     * @param result - Result merged list
     * @param <T>    - Comparable Type
     * @return - Result list sorted
     */
    private <T extends Comparable<? super T>> List<T> mergeSort(List<T> result) {
        int size = result.size();

        //  if its less than 20 then we don't need to split, just use sequental
        if (size < THRESHOLD) {
            return MSequentialSorter.mergeSort(result);
        }

        // Parameters and sub sets
        int mid = size / 2;
        List<T> left = result.subList(0, mid);
        List<T> right = result.subList(mid, size);

        // Allocate workrs for right side and this thread will work on the left side.
        Future<List<T>> futureRight = POOL.submit(() -> mergeSort(right));

        return MSequentialSorter.merge(result, mergeSort(left), get(futureRight));
    }

    /**
     * Get method for getting the Future while dealing with exceptions with a exaustion time out of 3 seconds.
     *
     * @param future - Future T
     * @param <T>    - Comparable T
     * @return - T
     */
    private <T> T get(Future<T> future) {
        try {
            return future.get(3, SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            throw new Error("Unexpected Checked Excepton", e.getCause());
        }
    }
}