package com.emplk.mareu.ui.list;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.emplk.mareu.R;
import com.emplk.mareu.adapters.MeetingListAdapter;
import com.emplk.mareu.databinding.ActivityMainBinding;
import com.emplk.mareu.ui.create.CreateNewMeetingActivity;
import com.emplk.mareu.ui.detail.DetailActivity;
import com.emplk.mareu.ui.list.room_filter.OnRoomSelectedListener;
import com.emplk.mareu.ui.list.room_filter.RoomFilterDialogFragment;
import com.emplk.mareu.utils.injection.ViewModelFactory;

import java.time.LocalDate;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements OnRoomSelectedListener {

    private ActivityMainBinding binding;

    private MeetingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setViewModel();
        configureToolbar();
        initRecyclerView();
        setCreateFab();

        viewModel.getFilterToolbarSubtitle().observe(this, subtitle -> {
            binding.toolbarMain.setSubtitle(subtitle);
        });

        viewModel.getMessageErrorToast().observe(this, toast ->
                Toasty.info(this, toast, Toasty.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetFilters();  // called here to clear filters on rotate (POC)
    }

    private void setViewModel() {
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MeetingViewModel.class);
    }

    private void configureToolbar() {
        setSupportActionBar(binding.toolbarMain);
    }

    private void initRecyclerView() {
        MeetingListAdapter adapter = new MeetingListAdapter(new OnMeetingClickedListener() {
            @Override
            public void onMeetingClicked(long meetingId) {
                startActivity(DetailActivity.navigate(MainActivity.this, meetingId));
            }

            @Override
            public void onDeleteMeetingClicked(long meetingId) {
                viewModel.onDeleteMeetingClicked(meetingId);
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.meetingsRv.addItemDecoration(dividerItemDecoration);
        binding.meetingsRv.setAdapter(adapter);
        viewModel.getMeetingViewStateItemsLiveData().observe(this, adapter::submitList);
    }

    private void setCreateFab() {
        binding.addFab.setOnClickListener(v -> startActivity(CreateNewMeetingActivity.navigate(this)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sortbydate_menu) {
            openDateFilterCalendar();
            return true;
        }
        if (id == R.id.sortbyroom_menu) {
            openRoomFilterList();
            return true;
        }
        if (id == R.id.sortdelete_menu) {
            resetFilters();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openRoomFilterList() {
        RoomFilterDialogFragment.newInstance().show(getSupportFragmentManager(), null);
    }

    @Override
    public void onRoomSelected(String roomName) {
        viewModel.onRoomFilter(roomName);
    }

    private void openDateFilterCalendar() {
        final Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                (view, selectedYear, selectedMonthOfYear, selectedDayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(selectedYear, selectedMonthOfYear + 1, selectedDayOfMonth);
                    viewModel.onDateFilter(selectedDate);
                }, year, month, day);
        dpd.show();
    }

    private void resetFilters() {
        viewModel.onResetFilters();
    }
}