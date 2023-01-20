package com.emplk.mareutraining.ui.list.create;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.emplk.mareutraining.ui.list.utils.TestUtils.isToast;
import static com.emplk.mareutraining.ui.list.utils.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.ui.list.MainActivity;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateMeetingTest {

    private static final String MEETING_TITLE = "Réunion test";
    private static final String MEETING_OBJECT = "Description de réunion test";
    private static final String ROOM = "Salle 5";
    private static final String PARTICIPANT_MAIL = "john@doe.fr";
    private static final String INVALID_PARTICIPANT_MAIL = "jôhИ@do€..fr";

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void onFillEveryFieldCorrectly_clickCreateBtn_shouldAddNewMeeting() {
        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(5)));
        onView(withId(R.id.add_fab)).perform(click());

        onView(withId(R.id.date_picker_btn_create)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 2, 22));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.starting_time_btn)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13, 0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.ending_time_btn)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(14, 0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.rooms_actv)).perform(click());
        onView(withText(ROOM)).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        onView(withId(R.id.title_textinput)).perform(replaceText(MEETING_TITLE));

        onView(withId(R.id.meeting_object_input)).perform(replaceText(MEETING_OBJECT));

        onView(withId(R.id.participants_input)).perform(replaceText(PARTICIPANT_MAIL));
        onView(withId(R.id.add_participant_fab)).perform(click());

        onView(withId(R.id.create_meeting_btn)).perform(click());

        onView(withId(R.id.meetings_rv)).check(matches(hasChildCount(6)));

        onView(withRecyclerView(R.id.meetings_rv)
                .atPositionOnView(5, R.id.meeting_title_tv))
                .check(matches(withText(MEETING_TITLE)));
    }

    @Test
    public void onAddNoInfo_shouldSubmitButtonBeDisabled() {
        onView(withId(R.id.add_fab)).perform(click());
        onView(withId(R.id.create_meeting_btn)).check(matches(not(isEnabled())));
        onView(withId(R.id.activity_create_meeting)).check(matches(isDisplayed()));
    }

    @Test
    public void onFillEveryFieldCorrectly_whenFilled_shouldDisplayChosenInfo() {
        onView(withId(R.id.add_fab)).perform(click());

        onView(withId(R.id.date_picker_btn_create)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 2, 22));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.selected_day_tv)).check(matches(withText("22-02-2023")));

        onView(withId(R.id.starting_time_btn)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.selected_time_start_tv)).check(matches(withText("13:00")));

        onView(withId(R.id.ending_time_btn)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(14, 0));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.selected_time_end_tv)).check(matches(withText("14:00")));

        onView(withId(R.id.rooms_actv)).perform(click());
        onView(withText(ROOM)).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.rooms_actv)).check(matches(withText(ROOM)));

        onView(withId(R.id.title_textinput)).perform(replaceText(MEETING_TITLE));
        onView(withId(R.id.title_textinput)).check(matches(withText(MEETING_TITLE)));

        onView(withId(R.id.meeting_object_input)).perform(replaceText(MEETING_OBJECT));
        onView(withId(R.id.meeting_object_input)).check(matches(withText(MEETING_OBJECT)));

        onView(withId(R.id.participants_input)).perform(replaceText(PARTICIPANT_MAIL));
        onView(withId(R.id.add_participant_fab)).perform(click());
        onView(allOf(withText(PARTICIPANT_MAIL), withParent(allOf(withId(R.id.participant_chipgroup),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup.class)))),
                isDisplayed()));
    }

    @Test
    public void onMeetingStartAt2pm_meetingEndAtpm_shouldDisplayToastError() {
        onView(withId(R.id.add_fab)).perform(click());

        onView(withId(R.id.date_picker_btn_create)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023, 2, 22));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.starting_time_btn)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(14, 0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.ending_time_btn)).perform(click());
        onView(withClassName(equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13, 0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.rooms_actv)).perform(click());
        onView(withText(ROOM)).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        onView(withId(R.id.title_textinput)).perform(replaceText(MEETING_TITLE));

        onView(withId(R.id.meeting_object_input)).perform(replaceText(MEETING_OBJECT));

        onView(withId(R.id.participants_input)).perform(replaceText(PARTICIPANT_MAIL));
        onView(withId(R.id.add_participant_fab)).perform(click());

        onView(withId(R.id.create_meeting_btn)).perform(click());

        onView(withText(R.string.check_time_ok_toast)).inRoot(isToast()).check(matches(isDisplayed()));
    }

    @Test
    public void onAddingInvalidMail_shouldDisplayError() {
        onView(withId(R.id.add_fab)).perform(click());

        onView(withId(R.id.participants_input)).perform(replaceText(INVALID_PARTICIPANT_MAIL));
        onView(withId(R.id.add_participant_fab)).perform(click());
        onView(withId(R.id.participants_layout)).check(matches(hasDescendant(
                withText(R.string.invalid_email_input))));
    }
}
