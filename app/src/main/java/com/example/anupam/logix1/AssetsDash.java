package com.example.anupam.logix1;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;

public class AssetsDash extends AppCompatActivity {


    CardView fleet, assets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_dash);
        fleet = (CardView)findViewById(R.id.fleet1);
        assets = (CardView)findViewById(R.id.comm1);


        fleet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(AssetsDash.this,FleetDash.class);
                startActivity(intent1);
            }
        });
        assets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(AssetsDash.this,CommDash.class);
                startActivity(intent2);
            }
        });
    }
}
