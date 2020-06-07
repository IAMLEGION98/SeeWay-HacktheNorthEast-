package com.example.anupam.logix1;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class AnimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainintent = new Intent(AnimeActivity.this, ((GlobalVariables) AnimeActivity.this.getApplication()).getRole().equals("driver") ? DashActivity.class : Dash2Activity.class);
                AnimeActivity.this.startActivity(mainintent);
                AnimeActivity.this.finish();
            }
        }, 3000);
    }
}
