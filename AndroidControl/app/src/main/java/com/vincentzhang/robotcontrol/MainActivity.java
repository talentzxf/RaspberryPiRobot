package com.vincentzhang.robotcontrol;

import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.vincentzhang.robotcontrol.databinding.ActivityMainBinding;
import com.vincentzhang.robotcontrol.model.ControllerModel;
import com.vincentzhang.robotcontrol.model.SeekBarHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // setContentView(R.layout.activity_main);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setControllerModel(new ControllerModel());

        setupCallbacks();
    }

    private void setupCallbacks() {
        SeekBarHandler seekBarHandler = new SeekBarHandler();
        // Let seek bar always return to 0 when released
        SeekBar leftSeekBar = findViewById(R.id.leftWheelSpeed);
        SeekBar rightSeekBar = findViewById(R.id.rightWheelSpeed);

        leftSeekBar.setOnTouchListener(seekBarHandler);
        rightSeekBar.setOnTouchListener(seekBarHandler);
    }
}
