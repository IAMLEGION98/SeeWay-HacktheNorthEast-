package com.example.anupam.logix1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HealthDash extends AppCompatActivity {

    TextView lastchecked, status;
    Button upload;
    DatabaseReference healthbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_dash);
        lastchecked = (TextView)findViewById(R.id.lastchecked);
        status = (TextView)findViewById(R.id.status);
        upload = (Button)findViewById(R.id.uploadBtn);
        healthbase = FirebaseDatabase.getInstance().getReference("Drivers");

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(HealthDash.this,ScanDocument.class);
                startActivity(intent2);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        healthbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot csnapshot: dataSnapshot.getChildren())
                {

                    Drivers d1 = csnapshot.getValue(Drivers.class);
                    Log.e("Header",d1.getName());
                    if(d1.getName().equals(((GlobalVariables)HealthDash.this.getApplication()).getEmail()))
                    {
                       lastchecked.setText(d1.getLastUpdated());
                        if(d1.getSick().equals("true"))
                        {

                            status.setText("You are Unfit");
                            status.setTextColor(Color.parseColor("#FF0000"));

                        }
                        else
                        {
                            status.setText("Healthy");
                            status.setTextColor(Color.parseColor("#00FF00"));

                        }


                     break;
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
