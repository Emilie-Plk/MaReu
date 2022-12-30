package com.emplk.mareutraining.adapters;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.databinding.MeetingItemBinding;
import com.emplk.mareutraining.models.Meeting;
import com.emplk.mareutraining.ui.list.MeetingsViewStateItem;
import com.emplk.mareutraining.ui.list.OnMeetingClickedListener;

import java.util.List;
import java.util.Objects;


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

        public void bind(MeetingsViewStateItem item, OnMeetingClickedListener listener) {
            itemView.setOnClickListener(v -> listener.onMeetingClicked(item.getMeetingId()));
           Resources res = itemView.getContext().getResources();
           roomName.setBackgroundColor(res.getColor(item.getRoomColor()));
           // roomName.getBackground().setTint(Color.parseColor(Integer.toHexString(item.getRoomColor())));
           // roomName.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), item.getRoomColor()));
           // roomName.setBackgroundResource(item.getRoomColor());
           // roomCircle.setImageResource(item.getRoomColor());
            roomName.setContentDescription(item.getRoomName()); // for accessibility
            roomName.setText(item.getRoomName());
            meetingTitle.setText(item.getMeetingTitle());
            meetingDate.setText(item.getDate());
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
