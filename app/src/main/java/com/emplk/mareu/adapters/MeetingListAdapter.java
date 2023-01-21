package com.emplk.mareu.adapters;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.emplk.mareu.databinding.MeetingItemBinding;
import com.emplk.mareu.ui.list.MeetingViewStateItem;
import com.emplk.mareu.ui.list.OnMeetingClickedListener;


public class MeetingListAdapter extends ListAdapter<MeetingViewStateItem, MeetingListAdapter.MeetingsViewHolder> {

    private final OnMeetingClickedListener listener;

    public MeetingListAdapter(OnMeetingClickedListener listener) {
        super(new ListMeetingItemCallback());
        this.listener = listener;
    }


    @NonNull
    @Override
    public MeetingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MeetingItemBinding binding = MeetingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MeetingsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingsViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    public static class MeetingsViewHolder extends RecyclerView.ViewHolder {

        private final ImageView deleteMeeting;
        private final TextView roomName, meetingTitle, meetingDate, startingHour, participants;

        public MeetingsViewHolder(@NonNull MeetingItemBinding binding) {
            super(binding.getRoot());
            roomName = binding.roomNumber;
            meetingTitle = binding.meetingTitleTv;
            meetingDate = binding.meetingDateTv;
            startingHour = binding.meetingHourTv;
            participants = binding.meetingParticipantsTv;
            deleteMeeting = binding.deleteMeeting;
        }

        public void bind(MeetingViewStateItem item, OnMeetingClickedListener listener) {
            itemView.setOnClickListener(v -> listener.onMeetingClicked(item.getMeetingId()));
            roomName.setBackgroundResource(item.getRoomColor());
            roomName.setContentDescription(item.getRoomName()); // for accessibility
            roomName.setText(item.getRoomName());
            meetingTitle.setText(item.getMeetingTitle());
            meetingDate.setText(item.getDate());
            startingHour.setText(item.getTimeStart());
            participants.setText(item.getParticipants());
            deleteMeeting.setOnClickListener(view -> listener.onDeleteMeetingClicked(item.getMeetingId()));
        }
    }

    private static class ListMeetingItemCallback extends DiffUtil.ItemCallback<MeetingViewStateItem> {

        @Override
        public boolean areItemsTheSame(@NonNull MeetingViewStateItem oldItem, @NonNull MeetingViewStateItem newItem) {
            return oldItem.getMeetingId() == newItem.getMeetingId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MeetingViewStateItem oldItem, @NonNull MeetingViewStateItem newItem) {
            return oldItem.equals(newItem);
        }
    }
}
