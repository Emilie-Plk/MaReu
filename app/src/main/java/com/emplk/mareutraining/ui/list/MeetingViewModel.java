package com.emplk.mareutraining.ui.list;

import static java.lang.String.valueOf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.utils.SingleLiveEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private final SingleLiveEvent<String> displayToolbarSubtitle = new SingleLiveEvent<>();

    private final SingleLiveEvent<String> displayToast = new SingleLiveEvent<>();

    public MeetingViewModel(@NonNull MeetingsRepository repository) {
        this.repository = repository;
        LiveData<List<Meeting>> meetingsLiveData = repository.getMeetingsLiveData();

        meetingViewStateItemsMediatorLiveData.addSource(meetingsLiveData, meetings ->
                combine(meetings, roomFilterMutableLiveData.getValue(), dateFilterMutableLiveData.getValue())
        );

        meetingViewStateItemsMediatorLiveData.addSource(roomFilterMutableLiveData, roomFilter ->
                combine(meetingsLiveData.getValue(), roomFilter, dateFilterMutableLiveData.getValue())
        );

        meetingViewStateItemsMediatorLiveData.addSource(dateFilterMutableLiveData, dateFilter ->
                combine(meetingsLiveData.getValue(), roomFilterMutableLiveData.getValue(), dateFilter)
        );
    }

    public SingleLiveEvent<String> getDisplayToolbarSubtitle() {
        return displayToolbarSubtitle;
    }

    public SingleLiveEvent<String> getDisplayToast() {
        return displayToast;
    }

    private void combine(@Nullable List<Meeting> meetings, @Nullable String roomFilter, @Nullable LocalDate dateFilter) {
        if (meetings == null) {
            return;
        }

        List<MeetingViewStateItem> filteredMeetings = new ArrayList<>();
        for (Meeting meeting : meetings) {
            if ((roomFilter == null || meeting.getRoom().getRoomName().equals(roomFilter))
                    && dateFilter == null || meeting.getDate().equals(dateFilter)) {
                //  we should "pre-format" what should be displayed to the View. To do so, we map the Meeting model to a more
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

    public LiveData<List<MeetingViewStateItem>> getMeetingViewStateItemsLiveData() {
        return meetingViewStateItemsMediatorLiveData;
    }

    public void onRoomFilter(String room) {
        roomFilterMutableLiveData.setValue(room);
        displayToolbarSubtitle.setValue("Réunions filtrées : " + room);
        setDisplayToast();
    }

    public void onDateFilter(LocalDate date) {
        dateFilterMutableLiveData.setValue(date);
        displayToolbarSubtitle.setValue("Réunions filtrées : " + formatDate(date));
       setDisplayToast();
    }

    private void setDisplayToast() {
        if (meetingViewStateItemsMediatorLiveData.getValue() == null || meetingViewStateItemsMediatorLiveData.getValue().isEmpty()) {
            displayToast.setValue("Aucune réunion à afficher"); // hard coded but no memory leak risk
        }
    }

    public void onResetFilters() {
        roomFilterMutableLiveData.setValue(null);
        dateFilterMutableLiveData.setValue(null);
        displayToolbarSubtitle.setValue(null);
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
    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public void onDeleteMeetingClicked(long meetingId) {
        repository.deleteMeeting(meetingId);
        displayToast.setValue("Réunion supprimée");
    }

}
