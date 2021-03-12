package ass1;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Task 2: Using another kind of comparable datastructures to test the merge sort implementations.
 * Using Char to test the merge sort implementations.
 */
public class TestChar {

    public static final Character[][] dataset={
            {'a','b','c','d','e','f'},
            {'f','e','d','c','b','a'},
            {'A','x','V','d','D','z'},
            {'2','*','<','.','?',' '},
            {1+2,2*2,1,'h','b',' ',' ',' ',1},
            {},
            manyOrdered(10000),
            manyReverse(10000),
            manyRandom(10000)
    };
    static private Character[] manyRandom(int size) {
        Random r=new Random(0);
        Character[] result=new Character[size];
        for(int i=0;i<size;i++){result[i]= (char)(r.nextInt());}
        return result;
    }
    static private Character[] manyReverse(int size) {
        Character[] result=new Character[size];
        for(int i=0;i<size;i++){result[i]=(char)(size-i);}
        return result;
    }
    static private Character[] manyOrdered(int size) {
        Character[] result=new Character[size];
        for(int i=0;i<size;i++){result[i]=(char)i;}
        return result;
    }

    @Test
    public void testISequentialSorter() {
        Sorter s=new ISequentialSorter();
        for(Character[]l:dataset){TestHelper.testData(l,s);}
    }

    @Test
    public void testMSequentialSorter() {
        Sorter s=new MSequentialSorter();
        for(Character[]l:dataset){TestHelper.testData(l,s);}
    }
    @Test
    public void testMParallelSorter1() {
        Sorter s=new MParallelSorter1();
        for(Character[]l:dataset){TestHelper.testData(l,s);}
    }
    @Test
    public void testMParallelSorter2() {
        Sorter s=new MParallelSorter2();
        for(Character[]l:dataset){TestHelper.testData(l,s);}
    }
    @Test
    public void testMParallelSorter3() {
        Sorter s=new MParallelSorter3();
        for(Character[]l:dataset){TestHelper.testData(l,s);}
    }
}
