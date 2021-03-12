package ass1;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class TestPerformance {
  long timeOf(Runnable r,int warmUp,int runs) {
    System.gc();
    for(int i=0;i<warmUp;i++) {r.run();}
    long time0=System.currentTimeMillis();
    for(int i=0;i<runs;i++) {r.run();}
    long time1=System.currentTimeMillis();
    return time1-time0;
  }
  <T extends Comparable<? super T>>void msg(Sorter s,String name,T[][] dataset) {
    long time=timeOf(()->{
      for(T[]l:dataset){s.sort(Arrays.asList(l));}
      },20 ,200);//realistically 20.000 to make the JIT do his job.. 20000
      System.out.println(name+" sort takes "+time/1000d+" seconds");
    }
  <T extends Comparable<? super T>>void msgAll(T[][] dataset) {
    //msg(new ISequentialSorter(),"Sequential insertion",TestBigInteger.dataset);//so slow
    //uncomment the former line to include performance of ISequentialSorter
    msg(new MSequentialSorter(),"Sequential merge sort",dataset);
    msg(new MParallelSorter1(),"Parallel merge sort (futures)",dataset);
    msg(new MParallelSorter2(),"Parallel merge sort (completablefutures)",dataset);
    msg(new MParallelSorter3(),"Parallel merge sort (forkJoin)",dataset);
    }
  @Test
  void testBigInteger() {
    System.out.println("On the data type BigInteger");
    msgAll(TestBigInteger.dataset);
    }
  @Test
  void testFloat() {
    System.out.println("On the data type Float");
    msgAll(TestFloat.dataset);
    }
  @Test
  void testPoint() {
    System.out.println("On the data type Point");
    msgAll(TestPoint.dataset);
    }
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