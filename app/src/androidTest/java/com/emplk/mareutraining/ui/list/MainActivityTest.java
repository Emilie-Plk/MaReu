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

import static com.emplk.mareutraining.ui.list.utils.TestUtils.actionOnItemViewAtPosition;
import static com.emplk.mareutraining.ui.list.utils.TestUtils.isToast;
import static com.emplk.mareutraining.ui.list.utils.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.emplk.mareutraining.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final int ITEM_COUNT = 5;

    private static final String MEETING_TITLE = "Présentation nouveau design";
    private static final String ROOM = "Salle 10";

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void isRecyclerViewVisible_onAppLaunch() {
        onView(withId(R.id.meetings_rv)).check(matches(isDisplayed()));
    }

    @Test
    public void meetingRecyclerView_shouldHaveAtLeastOneItem() {
        onView(withId(R.id.meetings_rv)).check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void onDeleteOneListItem_shouldRemoveOneItem() {
        onView(withId(R.id.meetings_rv)).perform(actionOnItemViewAtPosition(0,
                R.id.delete_meeting,
                click()));
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(ITEM_COUNT - 1)));
    }

    @Test
    public void onDeleteAllListItems_shouldDisplayNoMeeting() {
        onView(new RecyclerViewMatcher(R.id.meetings_rv)
                .atPositionOnView(0, R.id.delete_meeting))
                .perform(click());
        onView(new RecyclerViewMatcher(R.id.meetings_rv)
                .atPositionOnView(0, R.id.delete_meeting))
                .perform(click());
        onView(new RecyclerViewMatcher(R.id.meetings_rv)
                .atPositionOnView(0, R.id.delete_meeting))
                .perform(click());
        onView(new RecyclerViewMatcher(R.id.meetings_rv)
                .atPositionOnView(0, R.id.delete_meeting))
                .perform(click());
        onView(new RecyclerViewMatcher(R.id.meetings_rv)
                .atPositionOnView(0, R.id.delete_meeting))
                .perform(click());

        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(0)));
        }

        @Test
        public void onDeleteOneItem_shouldDisplayToast() {
            onView(withId(R.id.meetings_rv)).perform(actionOnItemViewAtPosition(0,
                    R.id.delete_meeting,
                    click()));
            onView(withText(R.string.meeting_deleted_toast)).inRoot(isToast()).check(matches(isDisplayed()));
    }

    @Test
    public void onClickFab_ShouldStartCreateActivity() {
        onView(withId(R.id.add_fab)).perform(click());
        onView(withId(R.id.activity_create_meeting)).check(matches(isDisplayed()));
    }

    @Test
    public void displayedMeetingInfo_displayedMeetingInfoInDetailActivity_shouldMatch() {
        //  Check third meeting title and room name in RV
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(2, R.id.meeting_title_tv))
                .check(matches(withText(MEETING_TITLE)));
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(2, R.id.room_number))
                .check(matches(withText(ROOM)));

        // Check meeting title and room name are the same in detail Activity
        onView(allOf(withId(R.id.meetings_rv), isDisplayed())).perform(actionOnItemAtPosition(2, click()));
        onView(withId(R.id.meeting_title_detail)).check(matches(withText(MEETING_TITLE)));
        onView(withId(R.id.room_name_detail)).check(matches(withText(ROOM)));
    }

}
