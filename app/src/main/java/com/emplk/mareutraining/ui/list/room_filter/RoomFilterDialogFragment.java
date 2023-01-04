package com.emplk.mareutraining.ui.list.room_filter;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.adapters.MeetingListRVAdapter;
import com.emplk.mareutraining.databinding.FragmentRoomFilterBinding;
import com.emplk.mareutraining.models.Room;
import com.emplk.mareutraining.ui.detail.DetailActivity;
import com.emplk.mareutraining.ui.list.MainActivity;
import com.emplk.mareutraining.ui.list.OnMeetingClickedListener;
import com.emplk.mareutraining.utils.ViewModelFactory;
import com.emplk.mareutraining.viewmodels.MeetingViewModel;


public class RoomFilterDialogFragment extends DialogFragment {

    private FragmentRoomFilterBinding binding;
    private MeetingViewModel viewModel;

    private String selectedRoomString;

    @NonNull
    public static RoomFilterDialogFragment newInstance() {
        return new RoomFilterDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRoomFilterBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(binding.getRoot()).create();

        Room[] roomList = Room.values();
        binding.roomListviewMenu.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, roomList));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MeetingViewModel.class);

        MeetingListRVAdapter adapter = new MeetingListRVAdapter(new OnMeetingClickedListener() {
            @Override
            public void onMeetingClicked(long meetingId) {
                startActivity(DetailActivity.navigate(getContext(), meetingId));
            }

            @Override
            public void onDeleteMeetingClicked(long meetingId) {
                viewModel.onDeleteMeetingClicked(meetingId);
                viewModel.setToast(getContext(), "Réunion supprimée");
            }
        });

        binding.roomListviewMenu.setOnItemClickListener((adapterView, view1, position, id) -> {
            selectedRoomString = binding.roomListviewMenu.getItemAtPosition(position).toString();
            //viewModel.onRoomClicked(selectedRoomString);

            viewModel.getMeetingsFilteredByRoom(selectedRoomString, this.getActivity(), getString(R.string.no_meeting_date_toast)).observe(this.getViewLifecycleOwner(),
                    meetingsViewStateItems -> adapter.submitList(meetingsViewStateItems));
           dismiss();
        });
    }

}

