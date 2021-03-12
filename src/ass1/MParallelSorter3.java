package ass1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MParallelSorter3 implements Sorter {
    static final ForkJoinPool mainPool = new ForkJoinPool();

    /**
     * The MParallelSorter4 class uses the merge sort algorithm to sort in parallel using ForkJoin frame work.
     * The benefit of this algorithm is that lower level features tend to have better efficiency and can become more optimized-
     * especially for "natrualy" recursive tasks.
     * <p>
     * From implementing merge sort in this way, it helped understood that writing efficient and correct parallel program-
     * with lower level features are going to be difficult and should only be used as a last resort only.
     *
     * @param list - List to be sorted
     * @param <T>  - Comparable type
     * @return - Sorted list
     */
    @Override
    public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
        return mainPool.invoke(new Saut<>(new ArrayList<>(list)));
    }
}

/**
 * Saut class to sort using Fork/join.
 *
 * @param <T> - Comparable type
 */
class Saut<T extends Comparable<? super T>> extends RecursiveTask<List<T>> {

    List<T> result;

    Saut(List<T> ns) {
        this.result = ns;
    }

    @Override
    protected List<T> compute() {
        int size = result.size();
        if (size < MParallelSorter1.THRESHOLD) {
            return MSequentialSorter.mergeSort(result);
        }

        int mid = size / 2;
        Saut<T> left = new Saut<>(result.subList(0, mid));
        Saut<T> right = new Saut<>(result.subList(mid, size));

        right.fork();
        return MSequentialSorter.merge(result, left.compute(), right.join());
    }
}

