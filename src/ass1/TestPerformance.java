package ass1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TestPerformance {

    /**
     * This timeOf method Uses the pre overwritten Runnable run method to sort though an 2d array of dataset-
     * with a specific sorting algorithm. It garbage collects first,then runs though a number of warmups before-
     * recording the time for another set of runs. Finally we return the calculated time for the 200 runs.
     *
     * @param r      - overwritten run method in Runnable to sort though a 2d list
     * @param warmUp - numbers of warmup
     * @param runs   - numbers of runs
     * @return - time duration of the sort.
     */
    long timeOf(Runnable r, int warmUp, int runs) {
        System.gc();
        for (int i = 0; i < warmUp; i++) {
            r.run();
        }
        long time0 = System.currentTimeMillis();
        for (int i = 0; i < runs; i++) {
            r.run();
        }
        long time1 = System.currentTimeMillis();
        return time1 - time0;
    }

    /**
     * The msg method calls the timeOf method by giving it a Runnable r which overwrites the run method using lambda expression,-
     * amount of warm ups and runs, which obtains the time spent for sorting the giving 2d array of dataset.
     * This then prints out the result of time this Sorter took to sort the 2d array.
     *
     * @param s       - Sorter class
     * @param name    - name of the algorithm
     * @param dataset - 2d array of T type dataset
     * @param <T>     - Comparable type
     */
    <T extends Comparable<? super T>> void msg(Sorter s, String name, T[][] dataset) {
        long time = timeOf(() -> {
            for (T[] l : dataset) {
                s.sort(Arrays.asList(l));
            }
        }, 20000, 200);//realistically 20.000 to make the JIT do his job..
        System.out.println(name + " sort takes " + time / 1000d + " seconds");
    }

    /**
     * The msgAll method calls the msg method for each of the Merge sort algorithm, it passes them the current-
     * type dataset for each algorithm to test on.
     *
     * @param dataset - 2d array of T type dataset
     * @param <T>     - Comparable type
     */
    <T extends Comparable<? super T>> void msgAll(T[][] dataset) {
        //msg(new ISequentialSorter(),"Sequential insertion",TestBigInteger.dataset);//so slow
        //uncomment the former line to include performance of ISequentialSorter
        msg(new MSequentialSorter(), "Sequential merge sort", dataset);
        msg(new MParallelSorter1(), "Parallel merge sort (futures)", dataset);
        msg(new MParallelSorter2(), "Parallel merge sort (completablefutures)", dataset);
        msg(new MParallelSorter3(), "Parallel merge sort (forkJoin)", dataset);
    }

    /**
     * testing for all Biginteger data
     */
    @Test
    void testBigInteger() {
        System.out.println("On the data type BigInteger");
        msgAll(TestBigInteger.dataset);
    }

    /**
     * testing for all Float data
     */
    @Test
    void testFloat() {
        System.out.println("On the data type Float");
        msgAll(TestFloat.dataset);
    }

    /**
     * testing for all Point data
     */
    @Test
    void testPoint() {
        System.out.println("On the data type Point");
        msgAll(TestPoint.dataset);
    }

    /**
     * testing for all Char data
     */
    @Test
    void testChar() {
        System.out.println("On the data type Char");
        msgAll(TestChar.dataset);
    }
}
/*
With the model solutions, on a lab machine we may get those results:
On the data type Float
Sequential merge sort sort takes 1.178 seconds
Parallel merge sort (futures) sort takes 0.609 seconds
Parallel merge sort (completablefutures) sort takes 0.403 seconds
Parallel merge sort (forkJoin) sort takes 0.363 seconds
On the data type Point
Sequential merge sort sort takes 1.373 seconds
Parallel merge sort (futures) sort takes 0.754 seconds
Parallel merge sort (completablefutures) sort takes 0.541 seconds
Parallel merge sort (forkJoin) sort takes 0.48 seconds
On the data type BigInteger
Sequential merge sort sort takes 1.339 seconds
Parallel merge sort (futures) sort takes 0.702 seconds
Parallel merge sort (completablefutures) sort takes 0.452 seconds
Parallel merge sort (forkJoin) sort takes 0.492 seconds
*/