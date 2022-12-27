package com.emplk.mareutraining.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.emplk.mareutraining.config.BuildConfigResolver;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.viewmodels.CreateMeetingViewModel;
import com.emplk.mareutraining.viewmodels.DetailMeetingViewModel;
import com.emplk.mareutraining.viewmodels.MeetingViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {


    private static ViewModelFactory factory;

    @NonNull
    private final MeetingsRepository meetingRepository;

    private ViewModelFactory(@NonNull MeetingsRepository meetingsRepository) {
        this.meetingRepository = meetingsRepository;
    }


    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory(
                            new MeetingsRepository(
                                    new BuildConfigResolver()
                            )
                    );
                }
            }
        }
        return factory;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MeetingViewModel.class)) {
            return (T) new MeetingViewModel(
                    meetingRepository);
        } else if (modelClass.isAssignableFrom(DetailMeetingViewModel.class)) {
            return (T) new DetailMeetingViewModel(meetingRepository);
        }
        else if (modelClass.isAssignableFrom(CreateMeetingViewModel.class)) {
            return (T) new CreateMeetingViewModel(meetingRepository);
        }
        throw new IllegalArgumentException("Unknown model class!");
    }
}
