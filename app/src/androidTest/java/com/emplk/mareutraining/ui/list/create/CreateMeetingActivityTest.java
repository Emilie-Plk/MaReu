package com.emplk.mareutraining.ui.list.create;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.emplk.mareutraining.ui.create.CreateNewMeetingActivity;


import org.junit.Rule;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateMeetingActivityTest {

    @Rule
    public ActivityScenarioRule<CreateNewMeetingActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(CreateNewMeetingActivity.class);
}
