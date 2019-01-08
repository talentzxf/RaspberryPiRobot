package com.vincentzhang.robotcontrol;

import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vincentzhang.robotcontrol.databinding.ActivityMainBinding;
import com.vincentzhang.robotcontrol.model.ControllerModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // setContentView(R.layout.activity_main);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
         binding.setControllerModel(new ControllerModel(this.getApplicationContext().getResources()));
    }
}
