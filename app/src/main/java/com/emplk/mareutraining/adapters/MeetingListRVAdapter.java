package com.emplk.mareutraining.adapters;


import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.databinding.MeetingItemBinding;
import com.emplk.mareutraining.ui.list.MeetingsViewStateItem;
import com.emplk.mareutraining.ui.list.OnMeetingClickedListener;


public class MeetingListRVAdapter extends ListAdapter<MeetingsViewStateItem, MeetingListRVAdapter.MeetingsViewHolder> {

    private final OnMeetingClickedListener listener;

    public MeetingListRVAdapter(OnMeetingClickedListener listener) {
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
        private final TextView roomName, meetingTitle, meetindDate, startingHour, participants;

        public MeetingsViewHolder(@NonNull MeetingItemBinding binding) {
            super(binding.getRoot());
            roomName = binding.roomNumber;
            meetingTitle = binding.meetingTitleTv;
            meetindDate = binding.meetingDateTv;
            startingHour = binding.meetingHourTv;
            participants = binding.meetingParticipantsTv;
            deleteMeeting = binding.deleteMeeting;
        }

        public void bind(MeetingsViewStateItem item, OnMeetingClickedListener listener) {
            itemView.setOnClickListener(v -> listener.onMeetingClicked(item.getMeetingId()));
            Resources res = roomName.getContext().getResources();
            roomName.setBackgroundColor(res.getColor(item.getRoomColor()));
           // roomName.setBackgroundResource(item.getRoomColor());
           // roomCircle.setImageResource(item.getRoomColor());
            roomName.setContentDescription(item.getRoomName()); // for accessibility
            roomName.setText(item.getRoomName());
            meetingTitle.setText(item.getMeetingTitle());
            meetindDate.setText(item.getDate());
            startingHour.setText(item.getTimeStart());
            participants.setText(item.getParticipants());
            deleteMeeting.setOnClickListener(view -> listener.onDeleteMeetingClicked(item.getMeetingId()));
        }
    }

    private static class ListMeetingItemCallback extends DiffUtil.ItemCallback<MeetingsViewStateItem> {

        @Override
        public boolean areItemsTheSame(@NonNull MeetingsViewStateItem oldItem, @NonNull MeetingsViewStateItem newItem) {
            return oldItem.getMeetingId() == newItem.getMeetingId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MeetingsViewStateItem oldItem, @NonNull MeetingsViewStateItem newItem) {
            return oldItem.equals(newItem);
        }
    }
}
