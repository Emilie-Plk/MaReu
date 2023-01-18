package com.emplk.mareutraining.ui.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.repositories.MeetingsRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for MainActivity
 */
public class MeetingViewModel extends ViewModel {

    @NonNull
    private final MeetingsRepository repository;

    private final MediatorLiveData<List<MeetingViewStateItem>> meetingViewStateItemsMediatorLiveData = new MediatorLiveData<>();
    private final MutableLiveData<String> roomFilterMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<LocalDate> dateFilterMutableLiveData = new MutableLiveData<>();

    public MeetingViewModel(@NonNull MeetingsRepository repository) {
        this.repository = repository;

        LiveData<List<Meeting>> meetingsLiveData = repository.getMeetingsLiveData();

        // source of unfiltered meetings
        meetingViewStateItemsMediatorLiveData.addSource(meetingsLiveData, meetings ->
            combine(meetings, roomFilterMutableLiveData.getValue(), dateFilterMutableLiveData.getValue())
        );

        // meetings filtered by room
        meetingViewStateItemsMediatorLiveData.addSource(roomFilterMutableLiveData, roomFilter ->
            combine(meetingsLiveData.getValue(), roomFilter, dateFilterMutableLiveData.getValue())
        );

        // meetings filtered by date
        meetingViewStateItemsMediatorLiveData.addSource(dateFilterMutableLiveData, dateFilter ->
            combine(meetingsLiveData.getValue(), roomFilterMutableLiveData.getValue(), dateFilter)
        );
    }

    public LiveData<List<MeetingViewStateItem>> getMeetingViewStateItemsLiveData() {
        return meetingViewStateItemsMediatorLiveData;
    }

    private void combine(@Nullable List<Meeting> meetings, @Nullable String roomFilter, @Nullable LocalDate dateFilter) {
        if (meetings == null) {
            return;
        }

        List<MeetingViewStateItem> filteredMeetings = new ArrayList<>();
        for (Meeting meeting : meetings) {
            if ((roomFilter == null || meeting.getRoom().getRoomName().equals(roomFilter))
                && dateFilter == null || meeting.getDate().equals(dateFilter)) {
                // This meeting is matching the filters (if any), we have to display it !
                // But we should "pre-format" what should be displayed to the View. To do so, we map the Meeting model to a more
                // "view specific" model, the MeetingViewStateItem.
                filteredMeetings.add(
                    new MeetingViewStateItem(
                        meeting.getMeetingTitle(),
                        meeting.getRoom().getRoomName(),
                        formatDate(meeting.getDate()),
                        formatTime(meeting.getTimeStart()),
                        formatParticipantList(meeting.getParticipants()),
                        meeting.getRoom().getRoomColor(),
                        meeting.getId()
                    )
                );
            }
        }

        meetingViewStateItemsMediatorLiveData.setValue(filteredMeetings);
    }

    public void onRoomSelected(String room) {
        roomFilterMutableLiveData.setValue(room);
    }

    public void onDateFilterChanged(LocalDate date) {
        dateFilterMutableLiveData.setValue(date);
    }

    public void resetFilters() {
        roomFilterMutableLiveData.setValue(null);
        dateFilterMutableLiveData.setValue(null);
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

    // TODO EMILIE This function should be private. Try to guess how to modify your toolbar title when you set a date filter!
    public String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public void onDeleteMeetingClicked(long meetingId) {
        repository.deleteMeeting(meetingId);
    }

}
