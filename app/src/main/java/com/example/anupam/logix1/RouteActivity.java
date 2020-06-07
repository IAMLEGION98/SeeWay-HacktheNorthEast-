package com.example.anupam.logix1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class RouteActivity extends FragmentActivity implements OnMapReadyCallback,RoutingListener,AsyncResponse,AsyncSResponse {


    private static final int LOCATION_REQUEST =500 ;
    private GoogleMap mMap;
    int statecount;
    int counts=0;
    LatLng portlatlng;
    LatLng current;
    String portlink;
    double stax=0;
    String prevstate="";
    ProgressDialog progressDialog;

    double latitude, longitude;
    Button btperform,btfinal;

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    String distance="", duration="";
    TextView tvdis,tvdur,tvrtax;
    int costoftoll=0;

    HashMap<LatLng,Integer> tollcost = new HashMap<LatLng, Integer>();
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        statecount=0;
        counts=0;
        stax=0.0;
        btperform = (Button)findViewById(R.id.btp);
        tvdis = (TextView)findViewById(R.id.tvdistance);
        tvdur=(TextView)findViewById(R.id.tvduration);

        tvrtax = (TextView)findViewById(R.id.tvtax);
        Intent intent = getIntent();

        portlatlng = new LatLng(Double.valueOf(intent.getStringExtra("destinationlat")),Double.valueOf(intent.getStringExtra("destinationlng")));
        current = new LatLng(Double.valueOf(intent.getStringExtra("currentlat")),Double.valueOf(intent.getStringExtra("currentlng")));

        polylines = new ArrayList<>();
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
    public static String getJSON(String url) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();

            con.connect();


            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(RouteActivity.this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        LocationManager locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria cr = new Criteria();
        String provider = locationmanager.getBestProvider(cr, true);
        final Location location = locationmanager.getLastKnownLocation(provider);
        if (location != null) {
            latitude=location.getLatitude();
            longitude=location.getLongitude();
        }
        btperform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IconGenerator iconGenerator = new IconGenerator(RouteActivity.this);

                Bitmap iconBitmap = iconGenerator.makeIcon("Truck");

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(current)
                        .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap));
                mMap.addMarker(markerOptions);

                String url = "https://maps.googleapis.com/maps/api/directions/json?origin=10.603095,77.472900&destination=+"+portlatlng.latitude+","+portlatlng.longitude+"&key=AIzaSyB5ibSpueClq2KFHxq21XWKCAnDetqLeGI";


              /* Routing routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener(RouteActivity.this)
                        .alternativeRoutes(false)
                        .waypoints(current, new LatLng(portlatlng.latitude, portlatlng.longitude))
                        .build();
                routing.execute(); */

                double pointY[]={75.02399,75.4599,75.882699,76.50109,75.882699,76.519456, 77.952864,78.609512, 79.432927, 80.774971,83.157897,83.136733,78.417158, 84.216097, 83.052077, 81.951548, 84.364245, 75.940968, 74.459488 , 80.851963,76.977533, 74.995266  };
                double pointX[]={19.228041,19.071375,20.048064,19.051782,19.055954,19.007034,19.436755,19.220374,18.312450,19.823645,20.562722,19.628584 ,19.169448,22.139573,18.287550,17.522275,19.329295,18.949407,18.488384,19.228820,19.228820,18.603753 };

                for(int i=0;i<pointX.length;i++) {
                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(pointX[i],pointY[i]))
                            .radius(10000)
                            .strokeColor(Color.RED)
                            .fillColor(Color.BLUE));
                }

                getpathdetails();
                iconBitmap = iconGenerator.makeIcon("Port");
                markerOptions.position(new LatLng(portlatlng.latitude, portlatlng.longitude))
                        .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap));
                mMap.addMarker(markerOptions);

            }
        });

    }
    public void getpathdetails()
    {
        StringBuilder matrixurl = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?key=AIzaSyB5ibSpueClq2KFHxq21XWKCAnDetqLeGI&");
        matrixurl.append("origins=10.603095,77.472900");
        matrixurl.append("&destinations="+portlatlng.latitude+","+portlatlng.longitude);
        matrixurl.append("&mode=driving&language=en-EN&sensor=true");
        String url = matrixurl.toString();
        Object routeinfo[] = new Object[2];
        routeinfo[0]= mMap;
        routeinfo[1]= url;
        Dialoginfo dialoginfo = new Dialoginfo(RouteActivity.this);
        //dialoginfo.delegate=this;
        dialoginfo.execute(routeinfo);




    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case LOCATION_REQUEST:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED )
                {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(portlatlng.latitude, portlatlng.longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        List<LatLng> points = route.get(0).getPoints();
        addtollcost();
        mMap.moveCamera(center);
        double tolerance = 1000;
        Iterator it = tollcost.entrySet().iterator();
        List<LatLng> routepoints =route.get(0).getPoints();



        for(int i=0;i<route.get(0).getPoints().size();i+=500) {
            String stateseen="";
            LatLng mll = routepoints.get(i);
            Object routeinfo[] = new Object[2];
            String locationdata = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyB5ibSpueClq2KFHxq21XWKCAnDetqLeGI&latlng=" + mll.latitude + "," + mll.longitude + "&sensor=false";
            routeinfo[0]= mMap;
            routeinfo[1]= locationdata;
            StateRoutine stateRoutine = new StateRoutine(RouteActivity.this);
            stateRoutine.execute(routeinfo);

        }
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Finding State Taxes...");
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runcost(); progressDialog.dismiss();
            }
        },9000);



        while(it.hasNext())
        {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            if(PolyUtil.isLocationOnPath((LatLng)pair.getKey(),points,true,tolerance))
            {
                costoftoll+=(int)pair.getValue();
            }
        }
        setui(costoftoll);
        System.out.println(costoftoll);
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);


        }
    }

    private void runcost() {
        int rc;
        if(counts>4)
        {
            rc = counts-4;
            stax = (rc)*350;
        }
        else
            stax=0;

        Log.d("The State tax is",Double.toString(stax));

    }

    private void addtollcost() {
        tollcost.put(new LatLng(11.983543, 79.788321),225);
        tollcost.put(new LatLng(12.347218, 79.780045),270);
        tollcost.put(new LatLng(12.724572, 79.981960),295);
        tollcost.put(new LatLng(12.982226, 79.969107),275);
        tollcost.put(new LatLng(13.043875, 80.149642),200);
        tollcost.put(new LatLng(13.136800, 80.181700),195);
        tollcost.put(new LatLng(13.217876, 80.169016),255);
        tollcost.put(new LatLng(13.745234, 79.990856),410);
        tollcost.put(new LatLng(14.060211, 79.894525),280);
        tollcost.put(new LatLng(14.318782, 79.918373),205);
        tollcost.put(new LatLng(14.852259, 79.987770),245);
        tollcost.put(new LatLng(15.358613, 80.040017),390);
        tollcost.put(new LatLng(15.886933, 80.070836),630);
        tollcost.put(new LatLng(16.385564, 80.533308),485);
        tollcost.put(new LatLng(16.572730, 80.862594),220);
        tollcost.put(new LatLng(16.676700, 81.022147),220);
        tollcost.put(new LatLng(16.824101, 81.430679),445);
        tollcost.put(new LatLng(16.715562, 81.827856),445);
        tollcost.put(new LatLng(17.215684, 82.110760),410);
        tollcost.put(new LatLng(17.398051, 82.675836),540);
        tollcost.put(new LatLng(17.685775, 83.149940),250);
        tollcost.put(new LatLng(18.058065, 83.521154),290);
        tollcost.put(new LatLng(19.111378, 84.699259),355);
        tollcost.put(new LatLng(19.638280, 85.140041),390);
        tollcost.put(new LatLng(20.137155, 85.839325),380);
        tollcost.put(new LatLng(20.203600, 85.668601),440);
        tollcost.put(new LatLng(20.540249, 85.963534),530);
        tollcost.put(new LatLng(20.678870, 86.165641),585);
        tollcost.put(new LatLng(21.176033, 86.118551),220);
        tollcost.put(new LatLng(21.428794, 86.835250),390);
        tollcost.put(new LatLng(22.240793, 87.392135),370);
        tollcost.put(new LatLng(22.396993, 87.522873),300);
        tollcost.put(new LatLng(22.335548, 87.869521),375);
        tollcost.put(new LatLng(22.654326, 88.325024),475);

//from cochin to mumbai east route
        tollcost.put(new LatLng(9.895511, 76.313551),175);
        tollcost.put(new LatLng(10.443163, 76.260163),385);
        tollcost.put(new LatLng(10.813182, 76.798028),300);
        tollcost.put(new LatLng(11.012860, 77.073194),415);
        tollcost.put(new LatLng(11.245720, 77.519675),290);
        tollcost.put(new LatLng(11.513721, 77.923843),320);
        tollcost.put(new LatLng(11.720114, 78.073405),410);
        tollcost.put(new LatLng(12.006381, 78.080620),510);
        tollcost.put(new LatLng(12.544602, 78.201193),340);
        tollcost.put(new LatLng(13.060726, 77.771809),95);
        tollcost.put(new LatLng(13.201878, 77.662449),390);
        tollcost.put(new LatLng(13.068153, 77.360660),235);
        tollcost.put(new LatLng(13.612200, 76.954287),395);
        tollcost.put(new LatLng(14.053876, 76.560527),320);
        tollcost.put(new LatLng(14.375625, 76.106660),310);
        tollcost.put(new LatLng(14.556925, 75.728139),405);
        tollcost.put(new LatLng(14.910099, 75.279748),285);
        tollcost.put(new LatLng(15.488713, 74.969892),178);
        tollcost.put(new LatLng(15.763110, 74.647870),450);
        tollcost.put(new LatLng(16.143323, 74.517023),135);
        tollcost.put(new LatLng(16.542258, 74.318267),345);
        tollcost.put(new LatLng(17.812157, 73.970203),315);
        tollcost.put(new LatLng(18.327536, 73.852309),455);
        tollcost.put(new LatLng(19.170661, 74.074929),215);
        tollcost.put(new LatLng(19.361474, 73.1658930),585);
        tollcost.put(new LatLng(19.520456, 72.916863),370);
        tollcost.put(new LatLng(19.520456, 72.916863),585);
        tollcost.put(new LatLng(19.520456, 72.916863),335);
        tollcost.put(new LatLng(19.517693, 74.201790),395);
        tollcost.put(new LatLng(20.141771, 73.976407),685);
//again from banglore to mumbai via pune
        tollcost.put(new LatLng(15.323387, 76.303574),420);
        tollcost.put(new LatLng(16.290481, 75.923571),410);
        tollcost.put(new LatLng(13.043572, 80.149642),245);
        tollcost.put(new LatLng(10.780848, 76.713917),275);
        tollcost.put(new LatLng(17.957638, 75.331071),285);
        tollcost.put(new LatLng(18.424587, 74.466197),355);
//from tutukudi to hyderabad and north
        tollcost.put(new LatLng(8.527011, 77.663241),345);
        tollcost.put(new LatLng(10.299617, 78.770695),295);
        tollcost.put(new LatLng(8.862754, 78.117113),375);
        tollcost.put(new LatLng(8.975751, 77.774591),425);
        tollcost.put(new LatLng(9.783008, 78.094513),375);
        tollcost.put(new LatLng(9.385394, 77.914342),365);
        tollcost.put(new LatLng(9.844303, 78.011224),340);
        tollcost.put(new LatLng(9.989658, 78.244087),410);
        tollcost.put(new LatLng(10.194410, 77.909132),315);
        tollcost.put(new LatLng(10.155873, 78.650641),310);
        tollcost.put(new LatLng(10.299626, 78.770662),150);
        tollcost.put(new LatLng(10.536455, 78.256923),515);
        tollcost.put(new LatLng(10.624218, 78.747562),145);
        tollcost.put(new LatLng(10.612548, 79.030654),205);
        tollcost.put(new LatLng(10.641311, 78.581317),380);
        tollcost.put(new LatLng(10.744430, 78.833973),330);
        tollcost.put(new LatLng(10.885925, 78.557224),135);
        tollcost.put(new LatLng(10.931802, 78.218255),240);
        tollcost.put(new LatLng(10.929444, 78.744052),230);
        tollcost.put(new LatLng(11.193901, 78.103180),245);
        tollcost.put(new LatLng(11.396773, 78.993675),280);
        tollcost.put(new LatLng(11.665032, 78.311757),265);
        tollcost.put(new LatLng(11.592748, 78.780755),265);
        tollcost.put(new LatLng(11.736543, 79.009600),265);
        tollcost.put(new LatLng(11.706363, 79.322428),280);
//some middle tolls
        tollcost.put(new LatLng(12.648648, 78.583213),410);
        tollcost.put(new LatLng(12.905828, 78.951684),425);
        tollcost.put(new LatLng(12.910878, 79.401137),270);
        tollcost.put(new LatLng(13.194220, 78.545426),125);
        tollcost.put(new LatLng(13.148495, 78.290530),375);
        tollcost.put(new LatLng(12.981985, 79.968968),275);
        tollcost.put(new LatLng(13.150887, 79.834653),145);
        tollcost.put(new LatLng(13.525528, 79.532902),305);
        tollcost.put(new LatLng(14.210428, 79.560353),175);
        tollcost.put(new LatLng(14.513514, 78.791099),160);
        tollcost.put(new LatLng(13.793069, 77.764300),450);
        tollcost.put(new LatLng(14.502116, 77.631281),535);
        tollcost.put(new LatLng(15.061820, 77.630402),485);
        tollcost.put(new LatLng(15.667433, 79.351856),250);
        tollcost.put(new LatLng(15.486513, 77.900971),520);
        tollcost.put(new LatLng(15.887758, 78.016993),520);
        tollcost.put(new LatLng(16.535719, 77.944380),330);
        tollcost.put(new LatLng(16.970369, 78.507791),205);
        tollcost.put(new LatLng(17.005948, 78.194385),345);
//vijayawada to mumbai via hyderabad
        tollcost.put(new LatLng(16.718097, 80.324270),390);
        tollcost.put(new LatLng(16.885029, 80.160679),395);
        tollcost.put(new LatLng(17.159684, 79.474819),480);
        tollcost.put(new LatLng(17.232884, 78.960484),350);
        tollcost.put(new LatLng(17.485742, 78.821305),400);
        tollcost.put(new LatLng(17.646712, 77.819779),440);
        tollcost.put(new LatLng(17.747180, 77.317426),420);




    }

    @Override
    public void onRoutingCancelled() {

    }

    public void setui(int ui) {
        tvrtax.setText(Integer.toString(ui));
        Log.d("The distance pp is",distance);

    }

    @Override
    public void processFinish(String output) {
        distance =output;
        System.out.println("\n The jojo distance is "+distance);

        Log.d("pp distance",distance);

    }

    @Override
    public void processSFinish(String stateseen) {
        if(stateseen.compareTo("Tamil Nadu")==0 || stateseen.compareTo("Kerala")==0 || stateseen.compareTo("Andhra Pradesh")==0 || stateseen.compareTo("Karnataka")==0 || stateseen.compareTo("Uttar Pradesh")==0 || stateseen.compareTo("Maharashtra")==0 || stateseen.compareTo("Delhi")==0 || stateseen.compareTo("Punjab")==0 || stateseen.compareTo("Haryana")==0 || stateseen.compareTo("West Bengal")==0 || stateseen.compareTo("Bihar")==0 || stateseen.compareTo("Gujarat")==0 || stateseen.compareTo("Rajasthan")==0 || stateseen.compareTo("Jharkand")==0)
        {
            if (prevstate.compareTo(stateseen)!=0){
                statecount++;
                counts++;
                prevstate = stateseen;
            }
        }


        Log.d("The stateCount is: ", String.valueOf(statecount));
    }
}
