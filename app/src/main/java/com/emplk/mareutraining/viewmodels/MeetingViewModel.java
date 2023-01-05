package com.emplk.mareutraining.viewmodels;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

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

public class MeetingViewModel extends ViewModel {
    @NonNull
    private final MeetingsRepository repository;


    public MeetingViewModel(@NonNull MeetingsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<MeetingsViewStateItem>> getMeetingViewStateItems() {
        return Transformations.map(repository.getMeetings(), meetings -> {
                    List<MeetingsViewStateItem> meetingsViewStateItems = new ArrayList<>();
                    for (Meeting meeting : meetings) {
                        meetingsViewStateItems.add(
                                new MeetingsViewStateItem(
                                        meeting.getMeetingTitle(),
                                        meeting.getRoom().getRoomName(),
                                        formatDate(meeting.getDate()),
                                        formatTimeStart(meeting.getTimeStart()),
                                        formatParticipantList(meeting.getParticipants()),
                                        meeting.getRoom().getRoomColor(),
                                        meeting.getId())
                        );
                    }
                    return meetingsViewStateItems;
                }
        );
    }

    private static String formatParticipantList(List<String> participantList) {
        String stringList = participantList.toString()
                .replace("[", "")
                .replace("]", "");

        return Arrays.stream(stringList.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .map(word -> word.substring(0, word.indexOf('@')))
                .collect(Collectors.joining(" "));
    }

    private String formatTimeStart(LocalTime startingTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return startingTime.format(formatter);
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public void onDeleteMeetingClicked(long meetingId) {
        repository.deleteMeeting(meetingId);
    }

    public LiveData<List<MeetingsViewStateItem>> getMeetingsFilteredByRoom(String selectedRoom, Context context, String message) {
        return Transformations.map(repository.getMeetings(), meetings -> {
            List<MeetingsViewStateItem> meetingsFilteredByRoomViewStateItems = new ArrayList<>();
            for (Meeting meeting : meetings) {
                if ((meeting.getRoom().getRoomName()).equals(selectedRoom)) {
                    meetingsFilteredByRoomViewStateItems.add(
                            new MeetingsViewStateItem(
                                    meeting.getMeetingTitle(),
                                    meeting.getRoom().getRoomName(),
                                    formatDate(meeting.getDate()),
                                    formatTimeStart(meeting.getTimeStart()),
                                    formatParticipantList(meeting.getParticipants()),
                                    meeting.getRoom().getRoomColor(),
                                    meeting.getId())
                    );
                }
            }
            // If no meeting(s) found for this given room, display a Toast
            if (meetingsFilteredByRoomViewStateItems.isEmpty()) {
                setToast(context, message + selectedRoom);
            }
            return meetingsFilteredByRoomViewStateItems;
        });
    }

    public LiveData<List<MeetingsViewStateItem>> getMeetingsFilteredByDate(LocalDate date, Context context, String message) {
        return Transformations.map(repository.getMeetings(), meetings -> {
            List<MeetingsViewStateItem> meetingsFilteredByDateViewStateItems = new ArrayList<>();
            for (Meeting meeting : meetings) {
                if ((meeting.getDate()).equals(date)) {
                    meetingsFilteredByDateViewStateItems.add(
                            new MeetingsViewStateItem(
                                    meeting.getMeetingTitle(),
                                    meeting.getRoom().getRoomName(),
                                    formatDate(meeting.getDate()),
                                    formatTimeStart(meeting.getTimeStart()),
                                    formatParticipantList(meeting.getParticipants()),
                                    meeting.getRoom().getRoomColor(),
                                    meeting.getId())
                    );
                }
            }
            // If no meeting(s) found for this given date, display a Toast
            if (meetingsFilteredByDateViewStateItems.isEmpty()) {
                setToast(context, message + formatDate(date));
            }
            return meetingsFilteredByDateViewStateItems;
        });
    }

    public void setToast(Context context, String message) {
        Toasty.info(context, message, Toast.LENGTH_SHORT).show();
    }
}
