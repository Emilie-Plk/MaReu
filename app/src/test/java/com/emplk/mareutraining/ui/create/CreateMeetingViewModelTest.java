package com.emplk.mareutraining.ui.create;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

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

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CreateMeetingViewModelTest {

    private static final String TITLE = "TEST MEETING TITLE";
    private static final String ROOM = "Salle 1";
    private static final String DATE = "13-12-2022";
    private static final String TIME_START = "15:00";
    private static final String TIME_END = "15:45";
    private static final List<String> PARTICIPANTS = Arrays.asList("john@doe.com", "jane@doe.com");
    private static final String OBJECT = "TEST MEETING OBJECT";


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private MeetingsRepository repository;

    @Mock
    private BuildConfigResolver buildConfigResolver = Mockito.mock(BuildConfigResolver.class);

    private CreateMeetingViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new CreateMeetingViewModel(repository);
        Mockito.doReturn(false).when(buildConfigResolver).isDebug();
    }

    @Test
    public void onCreateMeetingClicked() {
        repository = new MeetingsRepository(buildConfigResolver);

        viewModel.onCreateMeetingClicked(
                TITLE,
                ROOM,
                DATE,
                TIME_START,
                TIME_END,
                PARTICIPANTS,
                OBJECT
        );

        // Comment faire car mes paramètres passent ensuite par des méthodes privées ? (formatDate, etc.)

        List<Meeting> result = TestUtil.getValueForTesting(repository.getMeetings());

        assertEquals(1, result.size());
    }


    @Test
    public void setToast() {
    }

    @Test
    public void isValidEmail() {
    }
}