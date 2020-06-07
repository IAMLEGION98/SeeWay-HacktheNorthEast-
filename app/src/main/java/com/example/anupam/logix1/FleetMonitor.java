package com.example.anupam.logix1;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.provider.UserDictionary.Words.APP_ID;

public class FleetMonitor extends AppCompatActivity {


    DatabaseReference iotbase;
    TextView truckid,crateid,cloc,dloc;
    TextView drivername,proxs,blinks;
    TextView grievance;
    TextView action;
    TextView speed;
    public String tid;
    Button routego;
    Assets a1;

    DatabaseReference assetbase;
    DatabaseReference gbase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_monitor);


        Intent intent = getIntent();
        tid = intent.getStringExtra("truckid");

        truckid = (TextView)findViewById(R.id.tvtruckid);
        crateid = (TextView)findViewById(R.id.tvcrateid);
        cloc = (TextView)findViewById(R.id.tvcurrent);
        dloc = (TextView)findViewById(R.id.tvdestination);
        drivername = (TextView)findViewById(R.id.tvdriver);
        proxs = (TextView)findViewById(R.id.proximity);
        blinks = (TextView)findViewById(R.id.blink);
        speed = (TextView)findViewById(R.id.tvspeed);
        routego = (Button)findViewById(R.id.bttrack);
        grievance = (TextView)findViewById(R.id.tvg);
        action = (TextView)findViewById(R.id.tvcoa);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId(APP_ID)// Required for Analytics.
                .setApiKey("AIzaSyBqFXyqj9nt600E4Zg3Gck-sXyBEUMPsWU") // Required for Auth.
                .setDatabaseUrl("https://a124-270e9.firebaseio.com/") // Required for RTDB.
                .build();

        if(((GlobalVariables)this.getApplication()).getWentin()==0) {
            FirebaseApp.initializeApp(this, options, "secondary");
            ((GlobalVariables)this.getApplication()).setWentin(1);
        }
            FirebaseApp app = FirebaseApp.getInstance("secondary");
            iotbase = FirebaseDatabase.getInstance(app).getReference();
            assetbase = FirebaseDatabase.getInstance().getReference("Assets");
            gbase = FirebaseDatabase.getInstance().getReference("Greivance");




        iotbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot childsnap : dataSnapshot.getChildren())
                {
                    if((childsnap.getKey().toString().trim()).equals("Infrontdistance"))
                    {
                        proxs.setText(childsnap.getValue().toString());
                    }
                    if((childsnap.getKey().toString().trim()).equals("Blinkrate"))
                    {
                        blinks.setText(childsnap.getValue().toString());
                    }
                    if((childsnap.getKey().toString().trim()).equals("Speed"))
                    {
                        speed.setText(childsnap.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.e("Truck id",tid);
        assetbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot childsnap : dataSnapshot.getChildren())
                {
                    /*if(tid.equals(childsnap.getRef().child("headerid").toString().trim()))
                    {
                        crateid.setText(childsnap.getRef().child("crateid").toString().trim());
                        drivername.setText(childsnap.getRef().child("driver").toString().trim());
                    } */
                    a1= childsnap.getValue(Assets.class);
                    if(tid.equals(a1.getHeaderid()))
                    {
                        crateid.setText(a1.getCrateid());
                        drivername.setText(a1.getDriver());
                        cloc.setText(String.valueOf(a1.getCurrentlat())+ ","+String.valueOf(a1.getCurrentlng()));
                        dloc.setText(String.valueOf(a1.getDestinationlat())+ ","+String.valueOf(a1.getDestinationlng()));
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        gbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot childsnap : dataSnapshot.getChildren())
                {
                    /*if(tid.equals(childsnap.getRef().child("headerid").toString().trim()))
                    {
                        crateid.setText(childsnap.getRef().child("crateid").toString().trim());
                        drivername.setText(childsnap.getRef().child("driver").toString().trim());
                    } */
                    Grievance g1= childsnap.getValue(Grievance.class);
                    if(tid.equals(g1.getTruckid()))
                    {
                        grievance.setText("YES");

                        if(g1.getRating().equals("Package Defect"))
                        {
                            action.setText("Retrace back to Warehouse");
                        }
                        if(g1.getRating().equals("Accident"))
                        {
                            action.setText("Find Repair Job");
                        }
                        if(g1.getRating().equals("Road Block") || g1.getRating().equals("Others"))
                        {
                            action.setText("Find Alternative Route");
                        }

                    }
                    else
                    {
                        grievance.setText("NO");
                        action.setText("Continue Commutation");
                    }

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        routego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(FleetMonitor.this,RouteActivity.class);
                intent4.putExtra("currentlat",String.valueOf(a1.getCurrentlat()));
                intent4.putExtra("currentlng",String.valueOf(a1.getCurrentlng()));
                intent4.putExtra("destinationlat",String.valueOf(a1.getDestinationlat()));
                intent4.putExtra("destinationlng",String.valueOf(a1.getDestinationlng()));
                startActivity(intent4);
            }
        });
    }
}
