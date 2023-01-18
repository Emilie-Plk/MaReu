package com.emplk.mareutraining.ui.list;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.databinding.ActivityMainBinding;
import com.emplk.mareutraining.ui.create.CreateNewMeetingActivity;
import com.emplk.mareutraining.ui.detail.DetailActivity;
import com.emplk.mareutraining.ui.list.room_filter.OnRoomSelectedListener;
import com.emplk.mareutraining.ui.list.room_filter.RoomFilterDialogFragment;
import com.emplk.mareutraining.utils.ViewModelFactory;

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
                // TODO Emilie Regarde du côté du SingleLiveEvent ou de l'EventWrapper pour pouvoir envoyer un événement à la vue (ici,
                //  afficher un toast). Ce n'est pas à la vue de décider d'afficher un Toast (on ne peut pas garantir ce comportement grâce
                //  à un test unitaire si c'est fait côté vue
                Toasty.info(MainActivity.this, R.string.meeting_deleted_toast, Toast.LENGTH_SHORT).show();
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.meetingsRv.addItemDecoration(dividerItemDecoration);
        binding.meetingsRv.setAdapter(adapter);
        viewModel.getMeetingViewStateItemsLiveData().observe(this, meetingsViewStateItems ->
            adapter.submitList(meetingsViewStateItems)
        );
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
        viewModel.onRoomSelected(roomName);
        // TODO Emilie shouldn't the ViewModel choose what the toolbar title is ? :)
        binding.toolbarMain.setSubtitle(getString(R.string.filter_meeting_appbar) + roomName);
    }

    private void openDateFilterCalendar() {
        final Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, (view, selectedYear, selectedMonthOfYear, selectedDayOfMonth) -> {
            LocalDate selectedDate = LocalDate.of(selectedYear, selectedMonthOfYear + 1, selectedDayOfMonth);
            viewModel.onDateFilterChanged(selectedDate);
            // TODO Emilie shouldn't the ViewModel choose what the toolbar title is ? :)
            binding.toolbarMain.setSubtitle(getString(R.string.filter_meeting_appbar) + viewModel.formatDate(selectedDate));
        }, year, month, day);
        dpd.show();
    }

    private void resetFilters() {
        viewModel.resetFilters();
        // TODO Emilie shouldn't the ViewModel choose what the toolbar title is ? :)
        binding.toolbarMain.setSubtitle(null);
    }

    // TODO Emilie A part quelques rares exceptions (onOptionsItemSelected par exemple), jamais un "if" ne devrait être présent dans
    //  la vue en MVVM. Regarde du côté du SingleLiveEvent ou de l'EventWrapper pour pouvoir envoyer un événement à la vue (ici, afficher
    //  un toast)
//    private void setEmptyListToast() {
//        viewModel.getMeetingViewStateItems().observe(this, meetingsViewStateItems -> {
//            if (meetingsViewStateItems.isEmpty()) {
//                Toasty.info(MainActivity.this, R.string.no_meeting_toast, Toasty.LENGTH_SHORT).show();
//            }
//        });
//    }
}