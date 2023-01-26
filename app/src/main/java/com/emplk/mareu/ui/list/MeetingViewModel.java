package com.emplk.mareu.ui.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.emplk.mareu.models.Meeting;
import com.emplk.mareu.repositories.MeetingsRepository;
import com.emplk.mareu.utils.NotificationState;
import com.emplk.mareu.utils.SingleLiveEvent;

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

    private final MutableLiveData<String> filterToolbarSubtitle = new MutableLiveData<>();

    private final SingleLiveEvent<String> messageErrorToast = new SingleLiveEvent<>();

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

    /**
     * @return filterToolbarSubtitle (MutableLiveData of type String)
     */
    public MutableLiveData<String> getFilterToolbarSubtitle() {
        return filterToolbarSubtitle;
    }

    /**
     * @return messageErrorToast (SingleLiveEvent of type String)
     */
    public SingleLiveEvent<String> getMessageErrorToast() {
        return messageErrorToast;
    }

    /**
     * Used to filter and combine three parameters together
     * to produce a new list of MeetingViewStateItem
     * depending on the filters (room or date)
     *
     * @param meetings   List of Meeting
     * @param roomFilter String room filter
     * @param dateFilter LocalDate date filter
     */
    private void combine(@Nullable List<Meeting> meetings, @Nullable String roomFilter, @Nullable LocalDate dateFilter) {
        if (meetings == null) {
            return;
        }

        List<MeetingViewStateItem> filteredMeetings = new ArrayList<>();
        for (Meeting meeting : meetings) {
            if ((roomFilter == null || meeting.getRoom().getRoomName().equals(roomFilter))
                    && (dateFilter == null || meeting.getDate().equals(dateFilter))) {
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

    /**
     * @return LiveData List of MeetingViewStateItem
     */
    public LiveData<List<MeetingViewStateItem>> getMeetingViewStateItemsLiveData() {
        return meetingViewStateItemsMediatorLiveData;
    }

    /**
     * Set roomFilterMutableLiveData with chosen filter room
     * and display toolbar subtitle
     * (might trigger setDisplayToast if needed)
     *
     * @param room String room
     */
    public void onRoomFilter(String room) {
        dateFilterMutableLiveData.setValue(null);
        roomFilterMutableLiveData.setValue(room);
        filterToolbarSubtitle.setValue(NotificationState.FILTERED_SUBTITLE.getNotificationMessage() + room);  // hard coded but no memory leak risk
        setDisplayToast();
    }

    /**
     * Set dateFilterMutableLiveData with chosen filter date
     * and display toolbar subtitle
     * (might trigger setDisplayToast if needed)
     *
     * @param date LocalDate date
     */
    public void onDateFilter(LocalDate date) {
        roomFilterMutableLiveData.setValue(null);
        dateFilterMutableLiveData.setValue(date);
        filterToolbarSubtitle.setValue(NotificationState.FILTERED_SUBTITLE.getNotificationMessage() + formatDate(date));
        setDisplayToast();
    }

    /**
     * Set displayToast (SingleLiveEvent String)
     * with an "no meeting found" message if no meetingViewStateItems to display
     * (used for a Toast)
     */
    private void setDisplayToast() {
        if (meetingViewStateItemsMediatorLiveData.getValue() == null ||
                meetingViewStateItemsMediatorLiveData.getValue().isEmpty()) {
            messageErrorToast.setValue(NotificationState.INFO_NO_MEETING.getNotificationMessage());
        }
    }

    /**
     * Reset room and date MutableLiveData,
     * also set displayToolbarSubtitle (SingleLiveEvent String) to null
     */
    public void onResetFilters() {
        roomFilterMutableLiveData.setValue(null);
        dateFilterMutableLiveData.setValue(null);
        filterToolbarSubtitle.setValue(null);
    }

    /**
     * Call repository to delete one given meeting by its ID,
     * also set displayToast SingleLiveEvent String to "Meeting deleted"
     *
     * @param meetingId long meetingId
     */
    public void onDeleteMeetingClicked(long meetingId) {
        repository.deleteMeeting(meetingId);
        messageErrorToast.setValue(NotificationState.INFO_DELETED_MEETING.getNotificationMessage());
    }

    // region private helper methods

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
    // endregion
}
