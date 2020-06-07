package com.example.anupam.logix1;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;

public class DashActivity extends AppCompatActivity {


    CardView cexport,chealth,cout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        cexport =(CardView)findViewById(R.id.exportid);
        cout =(CardView)findViewById(R.id.signout);
        chealth =(CardView)findViewById(R.id.healthid);



        cexport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashActivity.this,AssetsDash.class);
                startActivity(intent);
            }
        });
        cout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DashActivity.this,MainLoginActivity.class);
                startActivity(intent1);
            }
        });
        chealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(DashActivity.this,HealthDash.class);
                startActivity(intent2);
            }
        });










    }
}
