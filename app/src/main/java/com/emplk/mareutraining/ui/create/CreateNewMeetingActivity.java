package com.emplk.mareutraining.ui.create;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.emplk.mareutraining.R;
import com.emplk.mareutraining.databinding.ActivityCreateNewMeetingBinding;

import java.util.Objects;

public class CreateNewMeetingActivity extends AppCompatActivity {

    public static Intent navigate(Context context) {
        return new Intent(context, CreateNewMeetingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCreateNewMeetingBinding binding = ActivityCreateNewMeetingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar myToolbar = binding.toolbarCreate;
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_create, CreateMeetingFragment.newInstance())
                    .setReorderingAllowed(true)
                    .commit();

        }
    }
}