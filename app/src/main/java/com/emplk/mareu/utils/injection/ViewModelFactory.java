package com.emplk.mareu.utils.injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.emplk.mareu.config.BuildConfigResolver;
import com.emplk.mareu.repositories.MeetingsRepository;
import com.emplk.mareu.ui.create.CreateMeetingViewModel;
import com.emplk.mareu.ui.detail.DetailMeetingViewModel;
import com.emplk.mareu.ui.list.MeetingViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {


    @NonNull
    private final MeetingsRepository meetingRepository;


    private ViewModelFactory(@NonNull MeetingsRepository meetingsRepository) {
        this.meetingRepository = meetingsRepository;
    }


    private static final class FactoryHolder {
        static final ViewModelFactory factory = new ViewModelFactory(
                new MeetingsRepository(
                        new BuildConfigResolver()
                ));
    }

    /**
     * Create an instance of a given ViewModel.
     * Keep the state of the ViewModel across configuration changes
     * and avoid unnecessary re-creation of ViewModel instances.
     *
     * @return ViewModelFactory factory
     */
    public static ViewModelFactory getInstance() {
        return FactoryHolder.factory;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MeetingViewModel.class)) {
            return (T) new MeetingViewModel(meetingRepository);
        } else if (modelClass.isAssignableFrom(DetailMeetingViewModel.class)) {
            return (T) new DetailMeetingViewModel(meetingRepository);
        } else if (modelClass.isAssignableFrom(CreateMeetingViewModel.class)) {
            return (T) new CreateMeetingViewModel(meetingRepository);
        }
        throw new IllegalArgumentException("Unknown model class!");
    }
}
