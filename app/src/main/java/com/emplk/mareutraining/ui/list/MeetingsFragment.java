package com.emplk.mareutraining.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.adapters.MeetingListRVAdapter;
import com.emplk.mareutraining.databinding.FragmentMeetingsBinding;
import com.emplk.mareutraining.ui.detail.DetailActivity;
import com.emplk.mareutraining.ui.detail.DetailFragment;
import com.emplk.mareutraining.utils.ViewModelFactory;
import com.emplk.mareutraining.viewmodels.MeetingViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeetingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingsFragment extends Fragment {

    private MeetingViewModel viewModel;

    private MeetingListRVAdapter adapter;

    public static Fragment newInstance() {
        return new MeetingsFragment();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        FragmentMeetingsBinding binding = FragmentMeetingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

         viewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance())
                .get(MeetingViewModel.class);

         // configure and init recyclerview
        RecyclerView recyclerView = view.findViewById(R.id.meetings_rv);
        initRecyclerView();
        recyclerView.setAdapter(adapter);
        getMeetingList();




// TODO: add the other viewStates + implements interfaces onMeetingRoomListener + onMeetingDateListener
    }

    private void initRecyclerView() {
        adapter = new MeetingListRVAdapter(new OnMeetingClickedListener() {
            @Override
            public void onMeetingClicked(long meetingId) {
                startActivity(DetailActivity.navigate(requireContext(), meetingId));
            }

            @Override
            public void onDeleteMeetingClicked(long meetingId) {
                viewModel.onDeleteMeetingClicked(meetingId);
            }
        });
    }

    private void getMeetingList() {
        viewModel.getMeetingViewStateItemsLiveData().observe(getViewLifecycleOwner(),
                meetingsViewStateItems -> adapter.submitList(meetingsViewStateItems));
    }

}