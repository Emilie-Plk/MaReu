package com.emplk.mareutraining.ui.create;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.databinding.ActivityCreateNewMeetingBinding;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.utils.ViewModelFactory;
import com.emplk.mareutraining.viewmodels.CreateMeetingViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class CreateNewMeetingActivity extends AppCompatActivity {

    ActivityCreateNewMeetingBinding binding;

    CreateMeetingViewModel viewModel;

    @NonNull
    private String selectedRoom = "";
    private final ArrayList<String> participantsEmails = new ArrayList<>();

    public static Intent navigate(Context context) {
        return new Intent(context, CreateNewMeetingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateNewMeetingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(CreateMeetingViewModel.class);


        Toolbar myToolbar = binding.toolbarCreate;
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bindAddMeeting(viewModel, binding.titleTextinput, binding.selectedDayTv,
                binding.selectedTimeStartTv, binding.selectedTimeEndTv, binding.meetingObjectInput);


        // Add participants (chips)
        addParticipantChip();

        // Set Time pickers
        binding.datePickerBtnCreate.setOnClickListener(v -> getDatePicker());
        configureTimePickerStart();
        configureTimePickerEnd();

        // Set ACTV spinner adapter
        setSpinnerAdapter();

        // Fetch selected room (string)
        binding.roomsActv.setOnItemClickListener((adapterView, v, position, id) ->
                selectedRoom = adapterView.getItemAtPosition(position).toString());

        viewModel.getCloseActivity().observe(this, closeActivitySingleLiveEvent -> finish());

      //  viewModel.getIsCreateButtonEnabled().observe(this, isCreateButtonEnabled -> binding.createMeetingBtn.setEnabled(isCreateButtonEnabled));
    }

    private void addParticipantChip() {
        binding.addParticipantFab.setOnClickListener(view1 -> {
            if (viewModel.isValidEmail(Objects.requireNonNull(binding.participantsInput.getText()).toString(), binding.participantsLayout, this)) {
                generateParticipantChip(binding.participantsInput);
                binding.participantsInput.setText("");
                binding.participantsInput.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
    }

    private void getDatePicker() {
        Locale.setDefault(Locale.FRANCE);

        final Calendar now = Calendar.getInstance();
        int mYear = now.get(Calendar.YEAR);
        int mMonth = now.get(Calendar.MONTH);
        int mDay = now.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH, monthOfYear);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    cal.set(Calendar.YEAR, year);

                    StringBuilder date = new StringBuilder();
                    date.append((dayOfMonth<10?"0":"")).append(dayOfMonth)
                            .append("-").append((monthOfYear + 1) < 10 ? "0" : "")
                            .append((monthOfYear+1)).append("-").append(year);
                    binding.selectedDayTv.setText(date);
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(now.getTimeInMillis());
        dpd.show();
    }

    private void configureTimePickerStart() {
        binding.startingTimeBtn.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                    (timePicker, hourOfDay, mMinute) ->
                            binding.selectedTimeStartTv.setText(String.format(Locale.FRANCE, "%02d:%02d", hourOfDay, mMinute)), hour, minute, true);
            timePickerDialog.show();
        });
    }

    private void configureTimePickerEnd() {
        binding.endingTimeBtn.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                    (timePicker, hourOfDay, mMinute) ->
                            binding.selectedTimeEndTv.setText(String.format(Locale.FRANCE, "%02d:%02d", hourOfDay, mMinute)), hour, minute, true);
            timePickerDialog.show();
        });
    }

    private void generateParticipantChip(@NonNull TextInputEditText textInputEditText) {
        Chip participantChip = new Chip(this);
        participantChip.setText(Objects.requireNonNull(textInputEditText.getText()).toString());
        participantChip.setChipIconResource(R.drawable.ic_baseline_person_24);
        participantChip.setCloseIconVisible(true);
        participantChip.setOnCloseIconClickListener(v -> {
            binding.participantChipgroup.removeView(participantChip);
            participantsEmails.remove(participantChip.getText().toString());
        });
        binding.participantChipgroup.addView(participantChip);
        participantsEmails.add(participantChip.getText().toString());
    }

    private void setSpinnerAdapter() {
        Room[] rooms = Room.values();
        binding.roomsActv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, rooms));
    }



    private void bindAddMeeting(
            CreateMeetingViewModel viewModel,
            TextInputEditText meetingTitle,
            TextView date,
            TextView timeStart,
            TextView timeEnd,
            TextInputEditText meetingObject
    ) {
      //  viewModel.getIsCreateButtonEnabled().observe(this, aBoolean -> binding.createMeetingBtn.setEnabled(aBoolean));

        binding.createMeetingBtn.setOnClickListener(v -> viewModel.onCreateMeetingClicked(
                Objects.requireNonNull(meetingTitle.getText()).toString(),
                selectedRoom,
                date.getText().toString(),
                timeStart.getText().toString(),
                timeEnd.getText().toString(),
                participantsEmails,
                Objects.requireNonNull(meetingObject.getText()).toString(),
                CreateNewMeetingActivity.this
        ));
    }
}