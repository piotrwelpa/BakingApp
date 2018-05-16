package com.example.piotr.bakingapp.utils;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;

/**
 * Created by piotr.welpa on 13.05.2018.
 */

public class EspressoIdlingResouce {
    private static final String RESOURCE = "GLOBAL";

    private static CountingIdlingResource countingIdlingResource =
            new CountingIdlingResource(RESOURCE);

    public static void increment() {
        countingIdlingResource.increment();
    }

    public static void decrement() {
        countingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return countingIdlingResource;
    }
}
