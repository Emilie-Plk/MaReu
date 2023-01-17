package com.emplk.mareutraining.ui.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.repositories.MeetingsRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for MainActivity
 */
public class MeetingViewModel extends ViewModel {

    @NonNull
    private final MeetingsRepository repository;


    private final MediatorLiveData<List<Meeting>> meetingListMediator = new MediatorLiveData<>();
    private final MutableLiveData<String> roomFilter = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> dateFilter = new MutableLiveData<>();

    public MeetingViewModel(@NonNull MeetingsRepository repository) {
        this.repository = repository;

        // source of unfiltered meetings
        meetingListMediator.addSource(repository.getMeetings(), meetings -> {
            if (roomFilter.getValue() == null) {
                meetingListMediator.setValue(meetings);
            } else {
                List<Meeting> allMeetings = new ArrayList<>();
                for (Meeting meeting : meetings) {
                    if (meeting.getRoom().getRoomName().equals(roomFilter.getValue())) {
                        allMeetings.add(meeting);
                    }
                }
                meetingListMediator.setValue(allMeetings);
            }
        });


        // meetings filtered by room
        meetingListMediator.addSource(roomFilter, filterValue -> {
            if (meetingListMediator.getValue() != null) {
                List<Meeting> meetings = repository.getMeetings().getValue();
                List<Meeting> roomFilterMeetings = new ArrayList<>();
                assert meetings != null;
                for (Meeting meeting : meetings) {
                    if (meeting.getRoom().getRoomName().equals(filterValue)) {
                        roomFilterMeetings.add(meeting);
                    }
                }
                meetingListMediator.setValue(roomFilterMeetings);
            }
        });

        // meetings filtered by date
        meetingListMediator.addSource(dateFilter, filterValue -> {
            if (meetingListMediator.getValue() != null) {
                List<Meeting> meetings = repository.getMeetings().getValue();
                List<Meeting> dateFilterMeetings = new ArrayList<>();
                assert meetings != null;
                for (Meeting meeting : meetings) {
                    if (meeting.getDate().equals(filterValue)) {
                        dateFilterMeetings.add(meeting);
                    }
                }
                meetingListMediator.setValue(dateFilterMeetings);
            }
        });
    }


    public LiveData<List<MeetingsViewStateItem>> getMeetingViewStateItems() {
        return Transformations.map(meetingListMediator, meetings -> {
            List<MeetingsViewStateItem> meetingsViewStateItems = new ArrayList<>();
            for (Meeting meeting : meetings) {
                meetingsViewStateItems.add(new MeetingsViewStateItem(
                        meeting.getMeetingTitle(),
                        meeting.getRoom().getRoomName(),
                        formatDate(meeting.getDate()),
                        formatTime(meeting.getTimeStart()),
                        formatParticipantList(meeting.getParticipants()),
                        meeting.getRoom().getRoomColor(),
                        meeting.getId()));
            }
            return meetingsViewStateItems;
        });
    }


    public void setRoomFilter(String room) {
        this.roomFilter.setValue(room);
    }

    public void setDateFilter(LocalDate date) {
        this.dateFilter.setValue(date);
    }

    public void resetFilters() {
        meetingListMediator.setValue(repository.getMeetings().getValue());
    }

    /**
     * Format participant list to String,
     * extract names from email (john@gmail.com -> John)
     *
     * @param participantList List<String>
     * @return String
     */
    private String formatParticipantList(List<String> participantList) {
        return participantList.stream()
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .map(word -> word.split("@")[0])
                .collect(Collectors.joining(" "));
    }

    /**
     * Format time for the view
     *
     * @param timeLocalTime LocalTime
     * @return String formatted time HH:mm
     */
    private String formatTime(LocalTime timeLocalTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return timeLocalTime.format(formatter);
    }

    /**
     * Format date for the view
     *
     * @param date LocalDate
     * @return String formatted date dd/MM/yyyy
     */
    public String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public void onDeleteMeetingClicked(long meetingId) {
        repository.deleteMeeting(meetingId);
    }

}
