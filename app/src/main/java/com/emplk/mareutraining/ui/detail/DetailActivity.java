package com.emplk.mareutraining.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.databinding.ActivityDetailBinding;


import java.util.Objects;

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

        Toolbar myToolbar = binding.toolbarDetail;
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_detail, DetailFragment.newInstance(), "DetailFragment")
                    .setReorderingAllowed(true)
                    .commit();
        }
    }
}