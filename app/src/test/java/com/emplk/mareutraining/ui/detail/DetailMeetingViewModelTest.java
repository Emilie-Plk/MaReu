package com.emplk.mareutraining.ui.detail;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.emplk.mareutraining.config.BuildConfigResolver;
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

    private MeetingsRepository repository;

    private DetailMeetingViewModel viewModel;


    @Before
    public void setUp() {
        Mockito.doReturn(true).when(buildConfigResolver).isDebug();
        repository = new MeetingsRepository(buildConfigResolver);
        viewModel = new DetailMeetingViewModel(repository);
    }

    @Test
    public void getDetailMeeting() {
        DetailViewState result = TestUtil.getValueForTesting(viewModel.getDetailViewStateLiveData(0));
        assertEquals("Réunion d'info", result.getMeetingTitle());
        assertEquals("Salle 4", result.getRoomName());
        assertEquals("pierre@lamzone.fr, charlotte@lamzone.fr, patrice@lamzone.fr", result.getParticipants());
        assertEquals("Nouveaux arrivants dans l'équipe + point sur les congés", result.getMeetingObject());
    }
}
