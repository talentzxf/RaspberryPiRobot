package com.vincentzhang.robotcontrol;

import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.vincentzhang.robotcontrol.databinding.ActivityMainBinding;
import com.vincentzhang.robotcontrol.model.ControllerModel;
import com.vincentzhang.robotcontrol.model.SeekBarHandler;
import com.vincentzhang.robotcontrol.utils.ReflectionHelper;
import com.vincentzhang.robotcontrol.view.IKView2D;

public class MainActivity extends AppCompatActivity {

    private ControllerModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ReflectionHelper.setContext(this.getApplicationContext());
        ReflectionHelper.setActivity(this);

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        model = new ControllerModel(this.getApplicationContext().getResources());
        binding.setControllerModel(model);

        setupCallbacks();
    }

    private void setupCallbacks() {
        SeekBarHandler seekBarHandler = new SeekBarHandler();
        // Let seek bar always return to 0 when released
        SeekBar leftSeekBar = findViewById(R.id.leftWheelSpeed);
        SeekBar rightSeekBar = findViewById(R.id.rightWheelSpeed);

        leftSeekBar.setOnTouchListener(seekBarHandler);
        rightSeekBar.setOnTouchListener(seekBarHandler);

        IKView2D ikView2D = findViewById(R.id.ikView2d);
        ikView2D.setOnSkeletonChangeListener(model);
    }
}
