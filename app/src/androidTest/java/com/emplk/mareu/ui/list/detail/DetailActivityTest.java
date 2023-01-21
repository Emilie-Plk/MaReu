package com.emplk.mareu.ui.list.detail;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.emplk.mareu.ui.list.utils.TestUtils.withRecyclerView;
import static org.hamcrest.CoreMatchers.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.emplk.mareu.R;
import com.emplk.mareu.ui.list.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    private static final String MEETING_TITLE = "Présentation nouveau design";
    private static final String ROOM_TEN = "Salle 10";

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void displayedMeetingInfo_displayedMeetingInfoInDetailActivity_shouldMatch() {
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(2, R.id.meeting_title_tv))
                .check(matches(withText(MEETING_TITLE)));
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(2, R.id.room_number))
                .check(matches(withText(ROOM_TEN)));
        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(2, R.id.room_number))
                .check(matches(withText(ROOM_TEN)));

        onView(allOf(withId(R.id.meetings_rv), isDisplayed())).perform(actionOnItemAtPosition(2, click()));

        onView(withId(R.id.meeting_title_detail)).check(matches(withText(MEETING_TITLE)));
        onView(withId(R.id.room_name_detail)).check(matches(withText(ROOM_TEN)));
    }
}
