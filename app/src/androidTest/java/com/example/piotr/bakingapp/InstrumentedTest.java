package com.example.piotr.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.piotr.bakingapp.ui.MainActivity;
import com.example.piotr.bakingapp.utils.EspressoIdlingResouce;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivtyRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init() {
        IdlingRegistry.getInstance().register(EspressoIdlingResouce.getIdlingResource());
        mainActivtyRule.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResouce.getIdlingResource());
    }

    @Test
    public void displaySteps() {
        onView(withId(R.id.master_list_rv)).check(matches(isDisplayed()));
    }

    @Test
    public void onListItemClick() {
        onView(withId(R.id.master_list_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void onIngredientButtonClick() {
        onView(withId(R.id.master_list_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        try {
            onView(withId(R.id.show_steps_btn)).check(matches(isDisplayed()));
            onView(withId(R.id.show_steps_btn)).perform(click());
        } catch (AssertionFailedError ignored) {

        }

    }

    @Test
    public void checkStepDisplayed() {
        onView(withId(R.id.master_list_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        try {
            onView(withId(R.id.show_steps_btn)).check(matches(isDisplayed()));
            onView(withId(R.id.show_steps_btn)).perform(click());
        } catch (AssertionFailedError ignored) {
        }
        onView(withId(R.id.desc_steps_tv)).check(matches(withText("Recipe Introduction")));
    }

    @Test
    public void swipeToNextStep() {
        onView(withId(R.id.master_list_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        try {
            onView(withId(R.id.show_steps_btn)).check(matches(isDisplayed()));
            onView(withId(R.id.show_steps_btn)).perform(click());
        } catch (AssertionFailedError ignored) {
        }
        onView(withId(R.id.swipe_placeholder)).perform(swipeLeft());
        onView(withId(R.id.short_desc_steps_tv)).check(matches(withText("Starting prep")));
    }
}
