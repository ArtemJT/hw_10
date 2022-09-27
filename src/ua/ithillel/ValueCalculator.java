package ua.ithillel;

import java.util.Arrays;
import java.util.concurrent.*;

public class ValueCalculator {
    private static final int SIZE = 2_000_000;
    private static final int HALF_SIZE = SIZE / 2;
    private static final float[] array = new float[SIZE];

    static void doCalc() {
        float[] arr1 = new float[HALF_SIZE];
        float[] arr2 = new float[HALF_SIZE];

        long start = System.currentTimeMillis();
        Arrays.fill(array, 3);
        System.arraycopy(array, 0, arr1, 0, HALF_SIZE);
        System.arraycopy(array, HALF_SIZE, arr2, 0, HALF_SIZE);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<float[]> callable1 = new MyCallable(arr1);
        Callable<float[]> callable2 = new MyCallable(arr2);

        FutureTask<float[]> futureTask1 = new FutureTask<>(callable1);
        FutureTask<float[]> futureTask2 = new FutureTask<>(callable2);

        executor.execute(futureTask1);
        executor.execute(futureTask2);

        while (!(futureTask1.isDone() && futureTask2.isDone())) {
            try {
                arr1 = futureTask1.get();
                arr2 = futureTask2.get();
                System.out.println("All Tasks Done!");
                executor.shutdown();
                break;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.arraycopy(arr1, 0, array, 0, HALF_SIZE);
        System.arraycopy(arr2, 0, array, HALF_SIZE, HALF_SIZE);

        long duration = System.currentTimeMillis() - start;
        System.out.println("Method duration: " + duration + " ms");
    }

    public static void main(String[] args) {
        doCalc();
    }
}