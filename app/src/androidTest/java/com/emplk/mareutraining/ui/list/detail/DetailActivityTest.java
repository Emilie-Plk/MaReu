package com.emplk.mareutraining.ui.list.detail;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.emplk.mareutraining.ui.detail.DetailActivity;
import com.emplk.mareutraining.ui.list.MainActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    @Rule
    public ActivityScenarioRule<DetailActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(DetailActivity.class);
}
