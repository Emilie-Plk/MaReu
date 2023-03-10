package com.emplk.mareu.ui.create;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.emplk.mareu.R;
import com.emplk.mareu.databinding.ActivityCreateNewMeetingBinding;
import com.emplk.mareu.models.Room;
import com.emplk.mareu.utils.injection.ViewModelFactory;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class CreateNewMeetingActivity extends AppCompatActivity {

    private ActivityCreateNewMeetingBinding binding;

    private CreateMeetingViewModel viewModel;

    @NonNull
    private String selectedRoom = "";

    @Nullable
    private Integer pickedStartHour;
    @Nullable
    private Integer pickedStartMinute;

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

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set Date & Time pickers
        binding.datePickerBtnCreate.setOnClickListener(v -> getDatePicker());
        configureTimePickerStart();
        configureTimePickerEnd();
        binding.selectedTimeStartTv.addTextChangedListener(onPickTimeCorrect());
        binding.selectedTimeEndTv.addTextChangedListener(onPickTimeCorrect());

        // Set room selection
        setSpinnerAdapter();
        getPickedRoom();

        // Set chip for participants
        addParticipantChip();

        setupObservers();

        onAddTextChangedListeners();
    }

    /**
     * Observers for the CreateMeetingActivity
     */
    private void setupObservers() {
        // observer for submit button enabling
        viewModel.getIsValidAndCompleted().observe(this, aBoolean ->
                binding.createMeetingBtn.setEnabled(aBoolean));

        // observer for case of invalid ending time picked
        viewModel.getErrorState().observe(this, errorMessage ->
                Toasty.error(this, errorMessage, Toasty.LENGTH_SHORT).show());

        // observer for selected ending time TV (error state)
        viewModel.getTimeEndColor().observe(this, color ->
                binding.selectedTimeEndTv.setTextColor(Color.parseColor(color)));

        // observer for selected ending time TV's icon (error state)
        viewModel.getIsErrorIconVisible().observe(this, errorIcon ->
                binding.tvTimeEndErrorIcon.setVisibility(errorIcon ? View.VISIBLE : View.GONE));

        // observer for closing activity when onCreateMeetingClicked() is called
        viewModel.getCloseActivity().observe(this, closeActivitySingleLiveEvent -> finish());
    }

    private void getPickedRoom() {
        binding.roomsActv.setOnItemClickListener((adapterView, v, position, id) ->
                selectedRoom = adapterView.getItemAtPosition(position).toString());

        bindAddMeeting(viewModel, binding.titleTextinput, binding.selectedDayTv,
                binding.selectedTimeStartTv, binding.selectedTimeEndTv, binding.meetingObjectInput);
    }

    private void onAddTextChangedListeners() {
        binding.selectedDayTv.addTextChangedListener(getTextWatcher());
        binding.selectedTimeStartTv.addTextChangedListener(getTextWatcher());
        binding.selectedTimeEndTv.addTextChangedListener(getTextWatcher());
        binding.titleTextinput.addTextChangedListener(getTextWatcher());
        binding.meetingObjectInput.addTextChangedListener(getTextWatcher());
    }

    private TextWatcher onPickTimeCorrect() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.isTimeValidValidation(
                        binding.selectedTimeStartTv.getText().toString(),
                        binding.selectedTimeEndTv.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkForFieldCompletion();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
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
                    String date = formatDate(dayOfMonth, monthOfYear + 1, year);
                    binding.selectedDayTv.setText(date);
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
        dpd.getDatePicker().setMinDate(now.getTimeInMillis());
        dpd.show();
    }

    private String formatDate(int dayOfMonth, int month, int year) {
        return String.format(Locale.FRANCE, "%02d/%02d/%04d", dayOfMonth, month, year);
    }

    private void configureTimePickers(
            @NonNull TimePickerDialog.OnTimeSetListener listener) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, listener, hour, minute, true);
        if (pickedStartHour != null && pickedStartMinute != null) {
            timePickerDialog.updateTime(pickedStartHour, pickedStartMinute);
        }
        timePickerDialog.show();
    }

    private void configureTimePickerStart() {
        binding.startingTimeBtn.setOnClickListener(view ->
                configureTimePickers((timePicker, hourOfDay, mMinute) -> {
                    pickedStartHour = hourOfDay;
                    pickedStartMinute = mMinute;
                    binding.selectedTimeStartTv.setText(String.format(Locale.FRANCE, "%02d:%02d", hourOfDay, mMinute));
                }));
    }

    private void configureTimePickerEnd() {
        binding.endingTimeBtn.setOnClickListener(view -> {
            configureTimePickers((timePicker, hourOfDay, mMinute) ->
                    binding.selectedTimeEndTv.setText(String.format(Locale.FRANCE, "%02d:%02d", hourOfDay, mMinute)));
        });
    }

    private void addParticipantChip() {
        binding.addParticipantFab.setOnClickListener(v -> {
            //noinspection ConstantConditions
            if (viewModel.isValidEmail(binding.participantsInput.getText().toString())) {
                generateParticipantChip(binding.participantsInput);
                binding.participantsLayout.setError(null);
                binding.participantsInput.setText("");
                binding.participantsInput.onEditorAction(EditorInfo.IME_ACTION_DONE);
                checkForFieldCompletion();
            } else {
                binding.participantsLayout.setError(getString(R.string.invalid_email_input));
                checkForFieldCompletion();
            }
        });
    }

    private void generateParticipantChip(@NonNull TextInputEditText textInputEditText) {
        Chip participantChip = new Chip(this);
        participantChip.setText(textInputEditText.getText().toString());
        participantChip.setChipIconResource(R.drawable.ic_baseline_person_24);
        participantChip.setCloseIconVisible(true);
        participantChip.setOnCloseIconClickListener(v -> {
            binding.participantChipgroup.removeView(participantChip);
            participantsEmails.remove(participantChip.getText().toString());
            checkForFieldCompletion();
        });
        binding.participantChipgroup.addView(participantChip);
        participantsEmails.add(participantChip.getText().toString());
    }

    private void checkForFieldCompletion() {
        //noinspection ConstantConditions
        viewModel.isMeetingInfoComplete(
                binding.titleTextinput.getText().toString(),
                selectedRoom,
                binding.selectedDayTv.getText().toString(),
                binding.selectedTimeStartTv.getText().toString(),
                binding.selectedTimeEndTv.getText().toString(),
                participantsEmails,
                binding.meetingObjectInput.getText().toString()
        );
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
            @NonNull
            TextView timeEnd,
            @NonNull
            TextInputEditText meetingObject
    ) {
        binding.createMeetingBtn.setOnClickListener(view -> {
            //noinspection ConstantConditions
            viewModel.onCreateMeetingClicked(
                    meetingTitle.getText().toString(),
                    viewModel.getSelectedRoom(selectedRoom),
                    viewModel.formatDate(date.getText().toString()),
                    viewModel.formatTime(timeStart.getText().toString()),
                    viewModel.formatTime(timeEnd.getText().toString()),
                    participantsEmails,
                    meetingObject.getText().toString()
            );
        });
    }
}