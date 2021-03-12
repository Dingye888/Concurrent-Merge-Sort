package ass1;

import java.util.ArrayList;
import java.util.List;


public class MSequentialSorter implements Sorter {

    /**
     * The MSequentialSorter class uses the merge sort algorithm to sort sequentially. The benefit of this algorithm-
     * compared to all other ones, is that we don't have to write any parallel program. ("Writing parallel programs is bad").
     * This algorithm is the equivalent to, "Before resorting to parallelism, try other kinds of optimizations.".
     * Since we've already sitting at a speed of O(nlogn) we don't necessarily have to introduce Parallelism.
     * This algorithm waves our needs of using additional threads. Too much threads used may cause a slower-
     * computation if not managed properly.
     * <p>
     * From implementing merge sort in this way, it helped me develop a deeper understanding towards merge sorting-
     * algorithm. This also helped me develop a mindset for considering how parallel programs may come into play and,-
     * what I have to change to make them compatible.
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
     * Recursive method for splitting list and sorting on two sublist
     *
     * @param result dest list
     * @param <T>    type
     */
    public static <T extends Comparable<? super T>> List<T> mergeSort(List<T> result) {
        int size = result.size();
        //  if its less than two then we don't need to divide
        if (size < 2) return result;

        // Parameters
        int mid = size / 2;
        List<T> left = result.subList(0, mid);
        List<T> right = result.subList(mid, size);

        // Sort each side then merge
        List<T> sortedLeft = mergeSort(left); // recursively call for divided sublist
        List<T> sortedRight = mergeSort(right); // recursively call for divided sublist

        return merge(result, sortedLeft, sortedRight);
    }

    /**
     * Mergers the two list together into one.
     *
     * @param result - The list that gets replace in place and becomes the combined list
     * @param left   - The sorted left side list
     * @param right  - The sorted right side list
     * @param <T>    - Comparable type
     * @return - merged result list
     */
    public static <T extends Comparable<? super T>> List<T> merge(List<T> result, List<T> left, List<T> right) {

        List<T> reference; //temporary arraylist to build the merged list
        int resultIndex = 0, leftIndex = 0, rightIndex = 0, tempIndex; //initial indices for lists; result, left ,right, temp.
        int leftSize = left.size(), rightSize = right.size();

        // Setting new values while stepping though both sides of the list
        while (leftIndex < leftSize && rightIndex < rightSize) {

            T leftValue = left.get(leftIndex), rightValue = right.get(rightIndex); // getting values

            if (leftValue.compareTo(rightValue) <= 0) { // stepping though left list
                result.set(resultIndex, leftValue);
                leftIndex++;
            } else {
                result.set(resultIndex, rightValue); //  stepping though right
                rightIndex++;
            }
            resultIndex++;
        }

        // If one side has reached the end, then just add the rest
        if (leftIndex >= leftSize) {
            reference = right;
            tempIndex = rightIndex;
        } else {
            reference = left;
            tempIndex = leftIndex;
        }

        // Adds the remaining back to the list
        for (int i = tempIndex; i < reference.size(); i++, resultIndex++) {
            result.set(resultIndex, reference.get(i));
        }
        return result; // our merged list
    }
}