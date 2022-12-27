package com.emplk.mareutraining.ui.detail;

import static com.emplk.mareutraining.ui.detail.DetailActivity.KEY_MEETING_ID;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.emplk.mareutraining.databinding.FragmentDetailBinding;
import com.emplk.mareutraining.utils.ViewModelFactory;
import com.emplk.mareutraining.viewmodels.DetailMeetingViewModel;

import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    public static Fragment newInstance() {
        return new DetailFragment();
    }

    private FragmentDetailBinding binding;

    private long meetingId;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        meetingId = requireActivity().getIntent().getLongExtra(KEY_MEETING_ID, -1);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DetailMeetingViewModel viewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance())
                .get(DetailMeetingViewModel.class);

        viewModel.getDetailViewStateLiveData(meetingId).observe(getViewLifecycleOwner(),
                detailViewState -> {
                    binding.meetingTitleDetail.setText(detailViewState.getMeetingTitle());
                   /* GradientDrawable backgroundDrawable = (GradientDrawable) binding.roomNameDetail.getBackground();
                   backgroundDrawable.setColor(Color.parseColor(Integer.toString(detailViewState.getRoomColor())));
                   binding.roomNameDetail.setBackground(backgroundDrawable);*/
                    binding.dayTimeTv.append(detailViewState.getDate() + " | " + detailViewState.getTimeStart() + " -" + detailViewState.getTimeEnd());
                    binding.roomNameDetail.setBackgroundResource(detailViewState.getRoomColor());
                    binding.roomNameDetail.setText(detailViewState.getRoomName());
                    binding.participantsListDetail.setText(detailViewState.getParticipants());
                    binding.meetingObjectTv.setText(detailViewState.getMeetingObject());
                });
    }

}