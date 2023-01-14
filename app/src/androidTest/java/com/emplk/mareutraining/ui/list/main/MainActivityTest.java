package com.emplk.mareutraining.ui.list.main;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.emplk.mareutraining.ui.list.utils.TestUtils.actionOnItemViewAtPosition;
import static com.emplk.mareutraining.ui.list.utils.TestUtils.childAtPosition;
import static com.emplk.mareutraining.ui.list.utils.TestUtils.isToast;
import static com.emplk.mareutraining.ui.list.utils.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.allOf;

import android.widget.DatePicker;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.ui.list.MainActivity;
import com.emplk.mareutraining.ui.list.utils.RecyclerViewMatcher;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final int ITEM_COUNT = 5;
    private static final String MEETING_TITLE = "Présentation nouveau design";
    private static final String ROOM_TEN = "Salle 10";
    private static final String ROOM_FOUR = "Salle 4";

    private static final String DATE = "08/12/2022";

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
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(0, R.id.delete_meeting))
                .perform(click());
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(0, R.id.delete_meeting))
                .perform(click());
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(0, R.id.delete_meeting))
                .perform(click());
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(0, R.id.delete_meeting))
                .perform(click());
        onView(withRecyclerView(R.id.meetings_rv)
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
                .check(matches(withText(ROOM_TEN)));

        // Check meeting title and room name are the same in detail Activity
        onView(allOf(withId(R.id.meetings_rv), isDisplayed())).perform(actionOnItemAtPosition(2, click()));
        onView(withId(R.id.meeting_title_detail)).check(matches(withText(MEETING_TITLE)));
        onView(withId(R.id.room_name_detail)).check(matches(withText(ROOM_TEN)));
    }

    @Test
    public void onFilterRoom4_shouldDisplayFilteredMeetings() {
        // GIVEN click on filter menu
        onView(withId(R.id.menu_filter_main)).check(matches(isDisplayed())).perform(click());

        // WHEN click on filter by room for room 4
        onView(withText(R.string.filter_by_room)).perform(click());
        onView(withText(ROOM_FOUR)).perform(click());

        // THEN recycler view should have two meetings filtered (room 4)
       onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(0, R.id.room_number))
                .check(matches(withText(ROOM_FOUR)));
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(1, R.id.room_number))
                .check(matches(withText(ROOM_FOUR)));

        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(2)));
    }

    @Test
    public void onFilterDate_shouldDisplayFilteredMeetings() {
        // GIVEN recycler view has 5 meeting items, click on filter menu
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(5)));
        onView(withId(R.id.menu_filter_main)).check(matches(isDisplayed())).perform(click());

        // WHEN click on filter by date for given date 08 dec 2022
        onView(withText(R.string.filter_by_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 12, 8));
        onView(withId(android.R.id.button1)).perform(click());

        // THEN recycler view should have two meetings filtered (room 4)
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(0, R.id.meeting_date_tv))
                .check(matches(withText(DATE)));
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(1, R.id.meeting_date_tv))
                .check(matches(withText(DATE)));

        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(2)));
    }

    @Test
    public void onFilterDate_resetFilter_shouldResetFilter() {
        // GIVEN recycler view display 5 meeting items, click on filter menu
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(5)));
        onView(withId(R.id.menu_filter_main)).check(matches(isDisplayed())).perform(click());

        // WHEN click on filter by date for given date 08 dec 2022
        onView(withText(R.string.filter_by_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 12, 8));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(2)));

        // ...and click on reset filter
        onView(withId(R.id.menu_filter_main)).check(matches(isDisplayed())).perform(click());
        onView(withText(R.string.reset_filter)).perform(click());

        // THEN recycler view should display all (5) meetings
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(5)));
    }

    @Test
    public void onFilterMeeting_noMeetingFound_shouldDisplayToast() {
        // GIVEN recycler view display 5 meeting items, click on filter menu
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(5)));
        onView(withId(R.id.menu_filter_main)).check(matches(isDisplayed())).perform(click());

        // WHEN click on filter by room for room 2
        onView(withText(R.string.filter_by_room)).perform(click());
        onView(withText("Salle 2")).perform(click());

        // THEN recycler view should display 0 meeting and "Aucune réunion trouvée" Toast
        onView(withText(R.string.no_meeting_toast)).inRoot(isToast()).check(matches(isDisplayed()));
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(0)));

    }

}
