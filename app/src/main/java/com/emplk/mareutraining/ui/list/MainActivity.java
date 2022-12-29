package com.emplk.mareutraining.ui.list;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.adapters.MeetingListRVAdapter;
import com.emplk.mareutraining.databinding.ActivityMainBinding;
import com.emplk.mareutraining.ui.create.CreateNewMeetingActivity;
import com.emplk.mareutraining.ui.detail.DetailActivity;
import com.emplk.mareutraining.ui.list.room_filter.RoomFilterDialogFragment;
import com.emplk.mareutraining.utils.ViewModelFactory;
import com.emplk.mareutraining.viewmodels.MeetingViewModel;
import com.emplk.mareutraining.viewmodels.RoomFilterDialogFragmentViewModel;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MeetingListRVAdapter adapter;
    RoomFilterDialogFragmentViewModel roomFilterViewModel;
    MeetingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomFilterViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(RoomFilterDialogFragmentViewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setViewModel();
        configureToolbar();
        addMeeting();
        getMeetingListFilteredByRoom();

        // configure and init recyclerview
        RecyclerView recyclerView = view.findViewById(R.id.meetings_rv);
        initRecyclerView();
        recyclerView.setAdapter(adapter);

    }

    private void addMeeting() {
        binding.addFab.setOnClickListener(v -> startActivity(CreateNewMeetingActivity.navigate(this)));
    }

    private void configureToolbar() {
        Toolbar myToolbar = binding.toolbarMain;
        setSupportActionBar(myToolbar);
    }

    private void initRecyclerView() {
        adapter = new MeetingListRVAdapter(new OnMeetingClickedListener() {
            @Override
            public void onMeetingClicked(long meetingId) {
                startActivity(DetailActivity.navigate(MainActivity.this, meetingId));
            }

            @Override
            public void onDeleteMeetingClicked(long meetingId) {
                viewModel.onDeleteMeetingClicked(meetingId);
            }
        });
    }

    private void setViewModel() {
        viewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance())
                .get(MeetingViewModel.class);
    }

    private void getMeetingList() {
        viewModel.getMeetingViewStateItemsLiveData().observe(this,
                meetingsViewStateItems -> adapter.submitList(meetingsViewStateItems));
    }

    private void getMeetingListFilteredByRoom() {
        viewModel.getMeetingFilteredByRoomViewStateItemsLiveData("Salle 1").observe(this,
                meetingsViewStateItems -> adapter.submitList(meetingsViewStateItems));
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
            getMeetingList();
        }
        return super.onOptionsItemSelected(item);
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