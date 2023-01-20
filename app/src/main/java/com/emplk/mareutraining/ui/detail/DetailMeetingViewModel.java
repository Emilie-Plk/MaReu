package com.emplk.mareutraining.ui.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.repositories.MeetingsRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Business logic for DetailActivity
 */
public class DetailMeetingViewModel extends ViewModel {
    @NonNull
    private final MeetingsRepository repository;

    public DetailMeetingViewModel(@NonNull MeetingsRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns ViewState for a single, given meeting
     *
     * @param meetingId long
     * @return LiveDate of DetailViewState
     */
    public LiveData<DetailViewState> getDetailViewStateLiveData(long meetingId) {
        return Transformations.map(
                repository.getSingleMeeting(meetingId),
                meeting -> new DetailViewState(
                        meeting.getMeetingTitle(),
                        meeting.getRoom().getRoomName(),
                        formatDate(meeting.getDate()),
                        formatTime(meeting.getTimeStart()),
                        formatTime(meeting.getTimeEnd()),
                        formatParticipantList(meeting.getParticipants()),
                        meeting.getMeetingObject(),
                        meeting.getRoom().getRoomColor(),
                        meeting.getId()
                ));
    }

    /**
     * Format participants list to a single String
     *
     * @param participantsList List of participants
     * @return String of all participants
     */
    @NonNull
    private String formatParticipantList(List<String> participantsList) {
        return participantsList.toString()
                .replace("[", "")
                .replace("]", "");
    }

    /**
     * Format date for the view
     *
     * @param date LocalDate
     * @return String formatted date dd/MM/yyyy
     */
    private String formatDate(@NonNull LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        return date.format(formatter);
    }

    /**
     * Format time for the view
     *
     * @param timeLocalTime LocalTime
     * @return String formatted time HH:mm
     */
    private String formatTime(@NonNull LocalTime timeLocalTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return timeLocalTime.format(formatter);
    }

}
