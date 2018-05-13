package com.example.piotr.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.piotr.bakingapp.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.core.IsAnything.anything;
import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule public ActivityTestRule<MainActivity> activtyRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init(){
        IdlingRegistry.getInstance().register(EspressoIdlingResouce.getIdlingResource());
        activtyRule.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @After
    public void unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResouce.getIdlingResource());
    }

    @Test
    public void onListItemClick(){
        onData(anything()).inAdapterView(withId(R.id.master_list_rv)).atPosition(0).perform(click());
    }
}
