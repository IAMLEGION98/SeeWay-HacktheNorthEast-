package com.example.anupam.logix1;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;

public class BuyActivity extends AppCompatActivity {


    CardView alibaba, indiamart, ec21;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        alibaba = (CardView)findViewById(R.id.buy1);
        indiamart = (CardView)findViewById(R.id.buy2);
        ec21 = (CardView)findViewById(R.id.buy3);
        alibaba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link="https://www.alibaba.com";
                Intent intent = new Intent(BuyActivity.this,WebFace.class);
                intent.putExtra("address",link);
                startActivity(intent);

            }
        });
        indiamart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link="https://www.indiamart.com";
                Intent intent = new Intent(BuyActivity.this,WebFace.class);
                intent.putExtra("address",link);
                startActivity(intent);

            }
        });
        ec21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link="https://www.ec21.com";
                Intent intent = new Intent(BuyActivity.this,WebFace.class);
                intent.putExtra("address",link);
                startActivity(intent);
            }
        });
    }
}

