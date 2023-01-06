package com.emplk.mareutraining.ui.list.room_filter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.emplk.mareutraining.databinding.FragmentRoomFilterBinding;
import com.emplk.mareutraining.models.Room;


public class RoomFilterDialogFragment extends DialogFragment {

    private FragmentRoomFilterBinding binding;

    private OnRoomSelectedListener listener;

    private String selectedRoomString;

    @NonNull
    public static RoomFilterDialogFragment newInstance() {
        return new RoomFilterDialogFragment();
    }

 /*  @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnRoomSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getLocalClassName() + " must implement onSomeEventListener");
        }
    }*/

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof  OnRoomSelectedListener) {
            listener = (OnRoomSelectedListener) context;
        } else  {
            throw new ClassCastException(context + " must implement OnRoomSelectedListener");
        }
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

        binding.roomListviewMenu.setOnItemClickListener((adapterView, view1, position, id) -> {
            selectedRoomString = binding.roomListviewMenu.getItemAtPosition(position).toString();

            // store the selected room name from ListView (String)
            listener.onRoomSelected(selectedRoomString);

            // dismiss the dialog fragment
            dismiss();
        });
    }

}

