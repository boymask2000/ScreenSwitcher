package com.posbeu.screenswitcher2;

/**
 * Created by gposabella on 09/02/2016.
 */
public class Heap {
    public static boolean isStop() {
        return stop;
    }

    public static void setStop(boolean stop) {
        Heap.stop = stop;
    }

    private static boolean stop=false;
}
