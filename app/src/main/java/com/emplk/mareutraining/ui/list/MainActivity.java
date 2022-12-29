package com.emplk.mareutraining.ui.list;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.databinding.ActivityMainBinding;
import com.emplk.mareutraining.ui.create.CreateNewMeetingActivity;
import com.emplk.mareutraining.ui.list.room_filter.RoomFilterDialogFragment;
import com.emplk.mareutraining.utils.ViewModelFactory;
import com.emplk.mareutraining.viewmodels.RoomFilterDialogFragmentViewModel;
import com.google.android.material.dialog.MaterialDialogs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    RoomFilterDialogFragmentViewModel roomFilterViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        roomFilterViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RoomFilterDialogFragmentViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        configureToolbar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_main, MeetingsFragment.newInstance(), "MeetingsFragment")
                    .setReorderingAllowed(true)
                    .commit();
        }

        addMeeting();
    }

    private void addMeeting() {
        binding.addFab.setOnClickListener(v -> startActivity(CreateNewMeetingActivity.navigate(this)));
    }

    private void configureToolbar() {
        Toolbar myToolbar = binding.toolbarMain;
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sortbydate_menu) {
 openDateFilterCalendar();
        }
        if (item.getItemId() == R.id.sortbyroom_menu) {
 openRoomFilterList();
        }
        if (item.getItemId() == R.id.sortdelete_menu) {
 deleteSortingFilter();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSortingFilter() {
    }

    private void openRoomFilterList() {
        RoomFilterDialogFragment.newInstance().show(getSupportFragmentManager(), "ROOM DIALOG");
    }

    private void openDateFilterCalendar() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
// TODO: getMeetingsFilteredByDate
        };

        new DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();

    }
}