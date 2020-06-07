package com.example.anupam.logix1;

import android.content.Intent;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.provider.UserDictionary.Words.APP_ID;

public class Commonitor extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String mlat,mlng;
    TextView vib;
    TextView heat;
    TextView humid;
    TextView temp;
    String crateid;
    String contents;
    TextView cid,cont;
    DatabaseReference iotbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonitor);
        Intent fromin = getIntent();
        cid = (TextView)findViewById(R.id.tvcid);
        cont = (TextView)findViewById(R.id.tvcontents);
        crateid = fromin.getStringExtra("crateid");
        contents = fromin.getStringExtra("contents");
        cid.setText(crateid);
        cont.setText(contents);


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
        vib = (TextView)findViewById(R.id.vibration);
        heat = (TextView)findViewById(R.id.heat);
        temp = (TextView)findViewById(R.id.temp);
        humid = (TextView)findViewById(R.id.humid);


        iotbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childsnap : dataSnapshot.getChildren())
                {
                    if((childsnap.getKey().toString().trim()).equals("Vibration"))
                    {
                        vib.setText(childsnap.getValue().toString());
                    }
                    if((childsnap.getKey().toString().trim()).equals("Flame"))
                    {
                        heat.setText(childsnap.getValue().toString());
                    }
                    if((childsnap.getKey().toString().trim()).equals("Temperature"))
                    {
                        temp.setText(childsnap.getValue().toString());
                    }
                    if((childsnap.getKey().toString().trim()).equals("Humidity"))
                    {
                        humid.setText(childsnap.getValue().toString());
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        iotbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                    String id = postsnapshot.getKey().toString().trim();
                    if(id.equals("Latitude"))
                    {
                        mlat =postsnapshot.getValue().toString().trim();
                    }
                    if (id.equals("Longitude"))
                    {
                        mlng = postsnapshot.getValue().toString().trim();
                    }
                }
                LatLng sydney = new LatLng(Double.valueOf(mlat), Double.valueOf(mlng));
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Add a marker in Sydney and move the camera

    }
}
