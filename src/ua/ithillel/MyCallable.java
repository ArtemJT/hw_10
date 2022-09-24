package ua.ithillel;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<float[]> {

    private final float[] array;

    public MyCallable(float[] array) {
        this.array = array;
    }

    @Override
    public float[] call() {
        for (int i = 0; i < array.length; i++) {
            array[i] = (float)(array[i] * Math.sin(0.2f + i / 5.)
                    * Math.cos(0.2f + i / 5.) * Math.cos(0.4f + i / 2.));
        }
        System.out.printf("Thread \"%s\" started...\n", Thread.currentThread().getName());
        return array;
    }
}
