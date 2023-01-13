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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.adapters.MeetingListRVAdapter;
import com.emplk.mareutraining.databinding.ActivityMainBinding;
import com.emplk.mareutraining.ui.create.CreateNewMeetingActivity;
import com.emplk.mareutraining.ui.detail.DetailActivity;
import com.emplk.mareutraining.ui.list.room_filter.OnRoomSelectedListener;
import com.emplk.mareutraining.ui.list.room_filter.RoomFilterDialogFragment;
import com.emplk.mareutraining.utils.ViewModelFactory;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements OnRoomSelectedListener {

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
        initRecyclerView();
        initMeetingList();
        setCreateFab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResetFilter();
        setEmptyListToast();
    }

    private void setViewModel() {
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MeetingViewModel.class);
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
                Toasty.info(MainActivity.this, R.string.meeting_deleted_toast, Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView recyclerView = binding.meetingsRv;
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Observer for meetings livedata
     */
    private void initMeetingList() {
        viewModel.getMeetingViewStateItems().observe(this, meetingsViewStateItems -> adapter.submitList(meetingsViewStateItems));
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
            viewModel.onResetFilter();
            binding.toolbarMain.setSubtitle(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openRoomFilterList() {
        RoomFilterDialogFragment.newInstance().show(getSupportFragmentManager(), null);
    }

    @Override
    public void onRoomSelected(String roomName) {
        viewModel.onFetchingMeetingsFilteredByRoom(roomName);
        binding.toolbarMain.setSubtitle(getString(R.string.filter_meeting_appbar) + roomName);
    }

    private void openDateFilterCalendar() {
        Locale.setDefault(Locale.FRANCE);

        final Calendar now = Calendar.getInstance();
        int mYear = now.get(Calendar.YEAR);
        int mMonth = now.get(Calendar.MONTH);
        int mDay = now.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            cal.set(Calendar.YEAR, year);
            LocalDate selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
            viewModel.onFetchingMeetingsFilteredByDate(selectedDate);
            binding.toolbarMain.setSubtitle(getString(R.string.filter_meeting_appbar) + viewModel.formatDate(selectedDate));
        }, mYear, mMonth, mDay);
        dpd.show();
    }

    private void setEmptyListToast() {
        viewModel.getMeetingViewStateItems().observe(this, meetingsViewStateItems -> {
            if (meetingsViewStateItems.isEmpty()) {
                Toasty.info(MainActivity.this, R.string.no_meeting_toast, Toasty.LENGTH_SHORT).show();
            }
        });
    }
}