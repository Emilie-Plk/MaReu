package com.emplk.mareutraining.ui.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
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


    private final MediatorLiveData<List<MeetingsViewStateItem>> meetingsViewStateItems = new MediatorLiveData<>();

    // The filter value
    private final MutableLiveData<String> roomFilter = new MutableLiveData<>();

    private final MutableLiveData<LocalDate> dateFilter = new MutableLiveData<>();

    private boolean isFilterApplied;

    public MeetingViewModel(@NonNull MeetingsRepository repository) {
        this.repository = repository;

        // The source of unfiltered meetings
if (!isFilterApplied) {
    meetingsViewStateItems.addSource(repository.getMeetings(), meetings -> {
        if (roomFilter.getValue() == null) {
            meetingsViewStateItems.setValue(convertToViewStateItems(meetings));
        } else {
            List<Meeting> filteredMeetings = new ArrayList<>();
            for (Meeting meeting : meetings) {
                if (meeting.getRoom().getRoomName().equals(roomFilter.getValue())) {
                    filteredMeetings.add(meeting);
                }
            }
            meetingsViewStateItems.setValue(convertToViewStateItems(filteredMeetings));
        }
    });
}

        meetingsViewStateItems.addSource(roomFilter, filterValue -> {
            if (meetingsViewStateItems.getValue() != null) {
                List<Meeting> meetings = convertToMeetings(meetingsViewStateItems.getValue());
                List<Meeting> filteredMeetings = new ArrayList<>();
                for (Meeting meeting : meetings) {
                    if (meeting.getRoom().getRoomName().equals(filterValue)) {
                        filteredMeetings.add(meeting);
                    }
                    isFilterApplied = true;
                }

                meetingsViewStateItems.setValue(convertToViewStateItems(filteredMeetings));
            }
        });

        meetingsViewStateItems.addSource(dateFilter, filterValue -> {
            if (meetingsViewStateItems.getValue() != null) {
                List<Meeting> meetings = convertToMeetings(meetingsViewStateItems.getValue());
                List<Meeting> filteredMeetings = new ArrayList<>();
                for (Meeting meeting : meetings) {
                    if (meeting.getDate().equals(filterValue)) {
                        filteredMeetings.add(meeting);
                    }
                    isFilterApplied = true;
                }
                meetingsViewStateItems.setValue(convertToViewStateItems(filteredMeetings));
            }
        });
    }
    public LiveData<List<MeetingsViewStateItem>> getMeetingViewStateItems() {
        return meetingsViewStateItems;
    }

    private List<MeetingsViewStateItem> convertToViewStateItems(List<Meeting> meetings) {
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
    }

    private List<Meeting> convertToMeetings(List<MeetingsViewStateItem> meetingsViewStateItems) {
        List<Meeting> meetings = new ArrayList<>();
        for (MeetingsViewStateItem meetingsViewStateItem : meetingsViewStateItems) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Room room = getRoomFromName(meetingsViewStateItem.getRoomName());
            assert room != null;
            Meeting meeting = new Meeting(
                    meetingsViewStateItem.getMeetingId(),
                    meetingsViewStateItem.getMeetingTitle(),
                    room,
                    LocalDate.parse(meetingsViewStateItem.getDate(), formatter),
                    LocalTime.parse(meetingsViewStateItem.getTimeStart()),
                    null, // Assume timeEnd is not known
                    Arrays.asList(meetingsViewStateItem.getParticipants()),
                    null // Assume meetingObject is not known
            );
            meetings.add(meeting);
        }
        return meetings;
    }


    public void setRoomFilter(String room) {
        this.roomFilter.setValue(room);
    }

    public void setDateFilter(LocalDate date) {
        this.dateFilter.setValue(date);
    }

    public void resetFilters() {
        isFilterApplied = false;
        meetingsViewStateItems.setValue(getMeetingViewStateItems().getValue());
    }

    private Room getRoomFromName(String roomName) {
        switch (roomName) {
            case "Salle 1":
                return Room.ROOM_ONE;
            case "Salle 2":
                return Room.ROOM_TWO;
            case "Salle 3":
                return Room.ROOM_THREE;
            case "Salle 4":
                return Room.ROOM_FOUR;
            case "Salle 5":
                return Room.ROOM_FIVE;
            case "Salle 6":
                return Room.ROOM_SIX;
            case "Salle 7":
                return Room.ROOM_SEVEN;
            case "Salle 8":
                return Room.ROOM_EIGHT;
            case "Salle 9":
                return Room.ROOM_NINE;
            case "Salle 10":
                return Room.ROOM_TEN;
        }
        return null;
    }


   /* public void onResetFilter() {
        repository.getAllMeetings();
    }*/

    /**
     * Format participant list to String,
     * extract names from email (john@gmail.com -> John)
     *
     * @param participantList List<String>
     * @return String
     */
    private String formatParticipantList(List<String> participantList) {
        String stringList = participantList.toString().replace("[", "").replace("]", "");
        return Arrays.stream(stringList.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .map(word -> word.indexOf('@') != -1 ? word.substring(0, word.indexOf('@')) : word)
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
