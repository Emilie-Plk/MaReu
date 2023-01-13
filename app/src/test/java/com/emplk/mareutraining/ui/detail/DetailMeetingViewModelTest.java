package com.emplk.mareutraining.ui.detail;


import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.config.BuildConfigResolver;
import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.utils.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DetailMeetingViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    private BuildConfigResolver buildConfigResolver = Mockito.mock(BuildConfigResolver.class);

    @Before
    public void setUp() {
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
    }

    @Test
    public void getMeeting() {
        MeetingsRepository repository = new MeetingsRepository(buildConfigResolver);
        DetailMeetingViewModel viewModel = new DetailMeetingViewModel(repository);
        viewModel.getDetailViewStateLiveData(0);

    }
}
