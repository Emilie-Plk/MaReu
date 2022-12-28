package com.emplk.mareutraining.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.emplk.mareutraining.config.BuildConfigResolver;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.viewmodels.CreateMeetingViewModel;
import com.emplk.mareutraining.viewmodels.DetailMeetingViewModel;
import com.emplk.mareutraining.viewmodels.MeetingViewModel;
import com.emplk.mareutraining.viewmodels.RoomFilterDialogFragmentViewModel;

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
        }
        else if (modelClass.isAssignableFrom(CreateMeetingViewModel.class)) {
            return (T) new CreateMeetingViewModel(meetingRepository);
        }
        else if (modelClass.isAssignableFrom(RoomFilterDialogFragmentViewModel.class)) {
            return (T) new RoomFilterDialogFragmentViewModel(meetingRepository);
        }
        throw new IllegalArgumentException("Unknown model class!");
    }
}
