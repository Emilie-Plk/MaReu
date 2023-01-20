package com.emplk.mareutraining.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.emplk.mareutraining.databinding.ActivityDetailBinding;
import com.emplk.mareutraining.utils.ViewModelFactory;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_MEETING_ID = "KEY_MEETING_ID";

    public static Intent navigate(Context context, long meetingId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_MEETING_ID, meetingId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        DetailMeetingViewModel viewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance())
                .get(DetailMeetingViewModel.class);

        Toolbar myToolbar = binding.toolbarDetail;
        setSupportActionBar(myToolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        long meetingId = getIntent().getLongExtra(KEY_MEETING_ID, -1);

        viewModel.getDetailViewStateLiveData(meetingId).observe(DetailActivity.this, detailViewState -> {
            binding.meetingTitleDetail.setText(detailViewState.getMeetingTitle());
            binding.roomNameDetail.setBackgroundResource(detailViewState.getRoomColor());
            binding.dayTimeTv.append(detailViewState.getDate() + " | " + detailViewState.getTimeStart() + " -" + detailViewState.getTimeEnd());
            binding.roomNameDetail.setText(detailViewState.getRoomName());
            binding.participantsListDetail.setText(detailViewState.getParticipants());
            binding.meetingObjectTv.setText(detailViewState.getMeetingObject());
        });
    }
}