package com.emplk.mareutraining.ui.list;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import com.emplk.mareutraining.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


    private static int ITEM_COUNT = 5;

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void isRecyclerViewVisible_onAppLaunch() {
        onView(withId(R.id.meetings_rv)).check(matches(isDisplayed()));
    }

    @Test
    public void meetingRecyclerView_shoulHaveAtLeastOneItem() {
        onView(withId(R.id.meetings_rv)).check(matches(hasMinimumChildCount(1)));
    }

// Correct meeting is in view
    @Test
    public void selectListItem_isActivityVisible() {

    }

   /* @Test
    public void onDeleteListItem_shouldRemoveOneItem() {
        onView(withId(R.id.meetings_rv))
    }*/

    @Test
    public void onClickFabShouldStartCreateActivity() {
        onView(withId(R.id.add_fab)).perform(click());
        onView(withId(R.id.activity_create_meeting)).check(matches(isDisplayed()));
    }

    @Test
    public void displayedMeetingInfo_displayedMeetingNameInDetailActivity_shouldMatch() {
        onView(allOf(withId(R.id.meetings_rv), isDisplayed())).perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.meeting_title_detail)).check(matches(withText("Réunion d'info")));
    }


    @Test
    public void displayedMeetingInfo_displayedMeetingName_shouldMatch1() {

    }
}
