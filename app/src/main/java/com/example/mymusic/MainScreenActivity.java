package com.example.mymusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.mymusic.radio.RadioActivity;

public class MainScreenActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton radioBtn,musicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        radioBtn=(ImageButton) findViewById(R.id.radioBtn);
        radioBtn.setOnClickListener(this);
        musicBtn=(ImageButton) findViewById(R.id.musicButton);
        musicBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.musicButton):
                startActivity(new Intent(this,MainActivity.class));
                break;
            case (R.id.radioBtn):
                startActivity(new Intent(this,RadioActivity.class));
                break;
        }
    }

}
