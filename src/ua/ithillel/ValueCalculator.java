package ua.ithillel;

import java.util.Arrays;
import java.util.concurrent.*;

public class ValueCalculator {

    static void doCalc() {
        int size = 2_000_000;
        int halfSize = size / 2;
        float[] array = new float[size];
        float[] arr1 = new float[halfSize];
        float[] arr2 = new float[halfSize];

        long start = System.currentTimeMillis();
        Arrays.fill(array, 3);
        System.arraycopy(array, 0, arr1, 0, halfSize);
        System.arraycopy(array, halfSize, arr2, 0, halfSize);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<float[]> callable1 = new MyCallable(arr1);
        Callable<float[]> callable2 = new MyCallable(arr2);

        FutureTask<float[]> futureTask1 = new FutureTask<>(callable1);
        FutureTask<float[]> futureTask2 = new FutureTask<>(callable2);

        while (true) {
            try {
                if (futureTask1.isDone() && futureTask2.isDone()) {
                    arr1 = futureTask1.get();
                    arr2 = futureTask2.get();
                    System.out.println("All Tasks Done!");
                    executor.shutdown();
                    break;
                }
                executor.execute(futureTask1);
                executor.execute(futureTask2);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.arraycopy(arr1, 0, array, 0, halfSize);
        System.arraycopy(arr2, 0, array, halfSize, halfSize);

        long duration = System.currentTimeMillis() - start;
        System.out.println("Method duration: " + duration + " ms");
    }

    public static void main(String[] args) {
        doCalc();
    }
}