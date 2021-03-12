package ass1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MParallelSorter2 implements Sorter {

    /**
     * The MParallelSorter2 class uses the merge sort algorithm to sort in parallel using CompletableFutures.
     * The benefit of this algorithm is higher level (Writing efficient programs is bad),to work with locker algorithms and work stealing.
     * We can create a worker for a delayed start, it will start when other futures are completed.
     * With work stealing, each worker has its own task queue and when a worker finish a task, it searches for itself-
     * and then the others for work to do.
     * <p>
     * From implementing merge sort in this way, it helped understood the concept of workers work stealing. Since writing program is bad,
     * our priorities should be wiring/invoking mainly on libraries.
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
        if (size < MParallelSorter1.THRESHOLD)
            return MSequentialSorter.mergeSort(result);

        // Parameters and sub sets
        int mid = size / 2;
        List<T> left = result.subList(0, mid);
        List<T> right = result.subList(mid, size);

        // Allocate workrs for right side and this thread will work on the left side.
        CompletableFuture<List<T>> completableFutureRight = CompletableFuture.supplyAsync(() -> mergeSort(right));

        return MSequentialSorter.merge(result, mergeSort(left), completableFutureRight.join());
    }
}
