package com.emplk.mareutraining.ui.list.main;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.emplk.mareutraining.ui.list.utils.TestUtils.actionOnItemViewAtPosition;
import static com.emplk.mareutraining.ui.list.utils.TestUtils.isToast;
import static com.emplk.mareutraining.ui.list.utils.TestUtils.withRecyclerView;

import static org.hamcrest.CoreMatchers.equalTo;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.ui.list.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final int ITEM_COUNT = 5;

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
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(0, R.id.delete_meeting))
                .perform(click());
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(ITEM_COUNT - 1)));
    }

    @Test
    public void onDeleteAllListItems_shouldDisplayNoMeeting() {
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(5)));

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
        onView(withText("Réunion supprimée")).inRoot(isToast()).check(matches(isDisplayed()));
    }

    @Test
    public void onClickFab_ShouldStartCreateActivity() {
        onView(withId(R.id.add_fab)).perform(click());
        onView(withId(R.id.activity_create_meeting)).check(matches(isDisplayed()));
    }

    @Test
    public void onFilterRoom4_shouldDisplayFilteredMeetings() {
        onView(withId(R.id.menu_filter_main)).check(matches(isDisplayed())).perform(click());

        onView(withText(R.string.filter_by_room)).perform(click());
        onView(withText(ROOM_FOUR)).perform(click());

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
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(5)));
        onView(withId(R.id.menu_filter_main)).check(matches(isDisplayed())).perform(click());

        onView(withText(R.string.filter_by_date)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 12, 8));
        onView(withId(android.R.id.button1)).perform(click());

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
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(5)));
        onView(withId(R.id.menu_filter_main)).check(matches(isDisplayed())).perform(click());

        onView(withText(R.string.filter_by_date)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 12, 8));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(2)));

        onView(withId(R.id.menu_filter_main)).check(matches(isDisplayed())).perform(click());
        onView(withText(R.string.reset_filter)).perform(click());

        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(5)));
    }

    @Test
    public void onFilterMeeting_noMeetingFound_shouldDisplayToast() {
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(5)));
        onView(withId(R.id.menu_filter_main)).check(matches(isDisplayed())).perform(click());

        onView(withText(R.string.filter_by_room)).perform(click());
        onView(withText("Salle 2")).perform(click());

        onView(withText("Aucune réunion à afficher")).inRoot(isToast()).check(matches(isDisplayed()));
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(0)));
    }
}
