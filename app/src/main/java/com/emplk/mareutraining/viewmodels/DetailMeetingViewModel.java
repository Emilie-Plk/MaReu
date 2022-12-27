package com.emplk.mareutraining.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.ui.detail.DetailViewState;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DetailMeetingViewModel extends ViewModel {

    @NonNull
    private final MeetingsRepository repository;

    public DetailMeetingViewModel(@NonNull MeetingsRepository repository) {
        this.repository = repository;
    }

  public LiveData<DetailViewState> getDetailViewStateLiveData(long meetingId) {
        return Transformations.map(
                repository.getSingleMeetingLiveData(meetingId),
                meeting -> new DetailViewState(
                        meeting.getMeetingTitle(),
                        meeting.getRoom().getRoomName(),
                        formatDate(meeting.getDate()),
                        formatTimeStart(meeting.getTimeStart()),
                        formatTimeEnd(meeting.getTimeEnd()),
                        formatParticipantList(meeting.getParticipants()),
                        meeting.getMeetingObject(),
                        meeting.getRoom().getRoomColor(),
                        meeting.getId()
                ));
    }


    private String formatParticipantList(List<String> participantsList) {
        return participantsList.toString()
                   .replace("[", "")
                   .replace("]", "");
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        return date.format(formatter);
    }

    private String formatTimeStart(LocalTime startingTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return startingTime.format(formatter);
    }

    private String formatTimeEnd(LocalTime endingTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return endingTime.format(formatter);
    }


}
