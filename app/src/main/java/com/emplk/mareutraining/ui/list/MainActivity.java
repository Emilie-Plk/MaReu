package com.emplk.mareutraining.ui.list;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MeetingListRVAdapter adapter;

    MeetingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setViewModel();
        configureToolbar();
        createMeeting();
        initRecyclerView();
        getMeetingList();
    }

    private void createMeeting() {
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
        RecyclerView recyclerView = binding.meetingsRv;
        recyclerView.setAdapter(adapter);
    }

    private void setViewModel() {
        viewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance())
                .get(MeetingViewModel.class);
    }

    private void getMeetingList() {
        viewModel.fetchMeetingViewStateItemsLiveData().observe(this,
                meetingsViewStateItems -> adapter.submitList(meetingsViewStateItems));
    }

    private void getMeetingListFilteredByRoom() {
        viewModel.getMeetingsFilteredByRoom().observe(this,
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
            getMeetingListFilteredByRoom();
        }
        if (item.getItemId() == R.id.sortdelete_menu) {
            getMeetingList();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openRoomFilterList() {
        RoomFilterDialogFragment.newInstance().show(getSupportFragmentManager(), "ROOM DIALOG");
 /*       viewModel.fetchMeetingFilteredByRoomViewStateItemsLiveData("Salle 10").observe(this, meetingsViewStateItems ->
                adapter.submitList(meetingsViewStateItems));*/
    }

    private void openDateFilterCalendar() {
        Locale.setDefault(Locale.FRANCE);

        final Calendar now = Calendar.getInstance();
        int mYear = now.get(Calendar.YEAR);
        int mMonth = now.get(Calendar.MONTH);
        int mDay = now.get(Calendar.DAY_OF_MONTH);

            now.set(Calendar.DAY_OF_MONTH, mDay);
            DatePickerDialog dpd = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.YEAR, year);
                        // TODO: logic to get the date and filter (viewModel)
                    }, mYear, mMonth, mDay);

            dpd.show();
    }

}