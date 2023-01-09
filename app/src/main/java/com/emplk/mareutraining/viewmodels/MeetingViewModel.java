package com.emplk.mareutraining.viewmodels;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.repositories.MeetingsRepository;
import com.emplk.mareutraining.ui.list.MeetingsViewStateItem;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;

/**
 * Business logic for MainActivity
 */
public class MeetingViewModel extends ViewModel {
    @NonNull
    private final MeetingsRepository repository;

    public MeetingViewModel(@NonNull MeetingsRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns a LiveData object of List of MeetingsViewStateItem
     *
     * @return List of MeetingsViewStateItem
     */
    public LiveData<List<MeetingsViewStateItem>> getMeetingViewStateItems() {
        return Transformations.map(repository.getMeetings(), meetings -> {
            List<MeetingsViewStateItem> meetingsViewStateItems = new ArrayList<>();
            for (Meeting meeting : meetings) {
                meetingsViewStateItems.add(new MeetingsViewStateItem(meeting.getMeetingTitle(), meeting.getRoom().getRoomName(), formatDate(meeting.getDate()), formatTime(meeting.getTimeStart()), formatParticipantList(meeting.getParticipants()), meeting.getRoom().getRoomColor(), meeting.getId()));
            }
            return meetingsViewStateItems;
        });
    }

    /**
     * Format participant list to String,
     * extract names from email (john@gmail.com -> John)
     *
     * @param participantList List<String>
     * @return String
     */
    private static String formatParticipantList(List<String> participantList) {
        String stringList = participantList.toString().replace("[", "").replace("]", "");

        return Arrays.stream(stringList.split(" ")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()).map(word -> word.substring(0, word.indexOf('@'))).collect(Collectors.joining(" "));
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
    }

    public LiveData<List<MeetingsViewStateItem>> getMeetingsFilteredByRoom(String selectedRoom) {
        return Transformations.map(repository.getMeetings(), meetings -> {
            List<MeetingsViewStateItem> meetingsFilteredByRoomViewStateItems = new ArrayList<>();
            for (Meeting meeting : meetings) {
                if ((meeting.getRoom().getRoomName()).equals(selectedRoom)) {
                    meetingsFilteredByRoomViewStateItems.add(new MeetingsViewStateItem(meeting.getMeetingTitle(), meeting.getRoom().getRoomName(), formatDate(meeting.getDate()), formatTime(meeting.getTimeStart()), formatParticipantList(meeting.getParticipants()), meeting.getRoom().getRoomColor(), meeting.getId()));
                }
            }

            return meetingsFilteredByRoomViewStateItems;
        });
    }

    public LiveData<List<MeetingsViewStateItem>> getMeetingsFilteredByDate(LocalDate date) {
        return Transformations.map(repository.getMeetings(), meetings -> {
            List<MeetingsViewStateItem> meetingsFilteredByDateViewStateItems = new ArrayList<>();
            for (Meeting meeting : meetings) {
                if ((meeting.getDate()).equals(date)) {
                    meetingsFilteredByDateViewStateItems.add(new MeetingsViewStateItem(meeting.getMeetingTitle(), meeting.getRoom().getRoomName(), formatDate(meeting.getDate()), formatTime(meeting.getTimeStart()), formatParticipantList(meeting.getParticipants()), meeting.getRoom().getRoomColor(), meeting.getId()));
                }
            }
            return meetingsFilteredByDateViewStateItems;
        });
    }

    public void setToast(Context context, String message) {
        Toasty.info(context, message, Toast.LENGTH_LONG).show();
    }
}
