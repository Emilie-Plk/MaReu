package com.emplk.mareutraining.ui.list;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.adapters.MeetingListRVAdapter;

import com.emplk.mareutraining.databinding.ActivityMainBinding;
import com.emplk.mareutraining.ui.create.CreateNewMeetingActivity;
import com.emplk.mareutraining.ui.detail.DetailActivity;
import com.emplk.mareutraining.ui.list.room_filter.RoomFilterDialogFragment;
import com.emplk.mareutraining.ui.list.room_filter.onRoomSelectedListener;
import com.emplk.mareutraining.utils.ViewModelFactory;
import com.emplk.mareutraining.viewmodels.MeetingViewModel;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements onRoomSelectedListener {

    private ActivityMainBinding binding;
    public MeetingListRVAdapter adapter;

    private MeetingViewModel viewModel;

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
                viewModel.setToast(MainActivity.this, "Réunion supprimée");
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

    /**
     * Calls all existing meetings
     */
    private void getMeetingList() {
        viewModel.getMeetingViewStateItems().observe(this,
                meetingsViewStateItems -> adapter.submitList(meetingsViewStateItems));
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
            getMeetingList();
            binding.toolbarMain.setTitle(R.string.app_name);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openRoomFilterList() {
        RoomFilterDialogFragment.newInstance().show(getSupportFragmentManager(), "ROOM DIALOG");
    }

    @Override
    public void onRoomSelected(String roomName) {
        viewModel.getMeetingsFilteredByRoom(roomName, this, getString(R.string.no_meeting_date_toast), binding.toolbarMain).observe(this,
                meetingsViewStateItems -> adapter.submitList(meetingsViewStateItems));
    }

    private void openDateFilterCalendar() {
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
                    LocalDate selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                    viewModel.getMeetingsFilteredByDate(selectedDate, MainActivity.this, getString(R.string.no_meeting_room_toast), binding.toolbarMain).observe(this,
                            meetingsViewStateItems -> adapter.submitList(meetingsViewStateItems));
                }, mYear, mMonth, mDay);
        dpd.show();

    }

}