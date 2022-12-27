package com.emplk.mareutraining.ui.create;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.databinding.FragmentCreateMeetingBinding;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.utils.ViewModelFactory;
import com.emplk.mareutraining.viewmodels.CreateMeetingViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class CreateMeetingFragment extends Fragment {

    private FragmentCreateMeetingBinding binding;

    private String selectedRoom;

    private final ArrayList<String> participantsEmails = new ArrayList<>();

    public static CreateMeetingFragment newInstance() {
        return new CreateMeetingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateMeetingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CreateMeetingViewModel viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(CreateMeetingViewModel.class);

        bindAddMeeting(viewModel, binding.titleTextinput, binding.selectedDayTv,
                binding.selectedHourStartTv, binding.selectedHourEndTv, binding.meetingObjectInput);

        binding.addParticipantFab.setOnClickListener(view1 -> {
            generateParticipantChip(binding.participantsInput);
            binding.participantsInput.setText("");
            binding.participantsInput.onEditorAction(EditorInfo.IME_ACTION_DONE);
        });

        // Set Time pickers
        binding.datePickerBtnCreate.setOnClickListener(v -> getDatePicker());
        configureTimePickerStart();
        configureTimePickerEnd();

        // Set ACTV spinner adapter
        setSpinnerAdapter();
        // Fetch selected room (string)
        binding.roomsActv.setOnItemClickListener((adapterView, v, position, id) ->
            selectedRoom = adapterView.getItemAtPosition(position).toString());
    }

    private void getDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE);
            binding.selectedDayTv.setText(formatter.format(calendar.getTime()));
        };

        new DatePickerDialog(
                getContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    private void configureTimePickerStart() {
        binding.startingHourBtn.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, 1);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                    (timePicker, hourOfDay, mMinute) ->
                            binding.selectedHourStartTv.setText(String.format(Locale.FRANCE, "%02d:%02d", hourOfDay, mMinute)), hour, minute, true);
            timePickerDialog.show();
        });
    }

    private void configureTimePickerEnd() {
        binding.endingHourBtn.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, 1);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                    (timePicker, hourOfDay, mMinute) ->
                            binding.selectedHourEndTv.setText(String.format(Locale.FRANCE, "%02d:%02d", hourOfDay, mMinute)), hour, minute, true);
            timePickerDialog.show();
        });
    }

    private void generateParticipantChip(@NonNull TextInputEditText textInputEditText) {
        Chip participantChip = new Chip(getContext());
        participantChip.setText(textInputEditText.getText().toString());
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
        binding.roomsActv.setAdapter(new ArrayAdapter<>(getActivity(),
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
        binding.createMeetingBtn.setOnClickListener(v -> viewModel.onCreateMeetingClicked(
                Objects.requireNonNull(meetingTitle.getText()).toString(),
                selectedRoom,
                date.getText().toString(),
                timeStart.getText().toString(),
                timeEnd.getText().toString(),
                participantsEmails,
                Objects.requireNonNull(meetingObject.getText()).toString()
        ));
    }

}



