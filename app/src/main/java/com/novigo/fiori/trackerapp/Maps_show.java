package com.novigo.fiori.trackerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Maps_show extends FragmentActivity implements OnMapReadyCallback , TaskLoadedCallback{

    private GoogleMap mMap;
    public Polyline polyline;
    public Polyline traceline;
    public String foid,url;
    public MarkerOptions source,destination;
    private Polyline currentPolyline;
    double s_lat,s_log,d_lat,d_log,c_lat,c_log;

    private BottomSheetBehavior bottomSheetBehavior;

    public ImageView imageView_fo,imageView_fotime,imageView_folocation;
    public TextView foid_tv,duration_tv,location_tv;

    String startingLocName,endingLocName,startingDate,endingDate;

    String waypoints_string = "";
    String orgin_url = "";
    String dest_url = "";

    View bottomSheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_show);
        foid= getIntent().getStringExtra("FO_ID");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initComponent();

        imageView_fo = findViewById(R.id.imageView4);
        imageView_fotime = findViewById(R.id.imageView3);
        imageView_folocation = findViewById(R.id.imageView2);

        imageView_fo.setImageResource(R.mipmap.truck_image_foreground);
        imageView_fotime.setImageResource(R.mipmap.truck_timing_foreground);
        imageView_folocation.setImageResource(R.mipmap.truck_loc_foreground);

        duration_tv = findViewById(R.id.freightorderid3);
        location_tv = findViewById(R.id.freightorderid2);
        foid_tv = findViewById(R.id.freightorderid);


    }

    private void initComponent() {
        ConstraintLayout bottomSheetLayout = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
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

        final Marker[] curr_marker = new Marker[1];

        final Marker[] boundry =  new Marker[3];

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        long fo_as_int = Long.parseLong(foid);
        Query qry = ref.child("frieght_orders").orderByChild("fo_id").equalTo(fo_as_int);
        qry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LatLng curr;
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        s_lat = (double) dataSnapshot1.child("start_loc").child("latitude").getValue();
                        s_log = (double) dataSnapshot1.child("start_loc").child("longitude").getValue();
                        d_lat = (double) dataSnapshot1.child("dest_loc").child("latitude").getValue();
                        d_log = (double) dataSnapshot1.child("dest_loc").child("longitude").getValue();
                        try {
                            c_lat = (double) dataSnapshot1.child("current_loc").child("latitude").getValue();
                            c_log = (double) dataSnapshot1.child("current_loc").child("longitude").getValue();
                            curr = new LatLng(c_lat,c_log);
                            curr_marker[0] = mMap.addMarker(new MarkerOptions().position(curr).title("Truck Location").icon(bitmapDescriptorfromVector(getApplicationContext(),R.drawable.ic_028_pin_15_48)));
                            boundry[0] = curr_marker[0];
                        }catch (ClassCastException e){
                            Toast.makeText(Maps_show.this,"Truck Location not updated yet!",Toast.LENGTH_LONG).show();
                        }



                        LatLng start = new LatLng(s_lat,s_log);
                        LatLng dest = new LatLng(d_lat,d_log);

                        source = new MarkerOptions().position(start);
                        destination = new MarkerOptions().position(dest);

                        //URL Orgin,Destination construction
                        orgin_url += Double.toString(s_lat)+","+Double.toString(s_log);
                        dest_url += Double.toString(d_lat)+","+Double.toString(d_log);
                        //


//                        url = getUrl(source.getPosition(),destination.getPosition(),"driving");

                        if(boundry[0] == null){
                            boundry[0] = mMap.addMarker(new MarkerOptions().position(start).title("Starting Location").icon(bitmapDescriptorfromVector(getApplicationContext(),R.drawable.ic_041_pin_26_48)));
                            boundry[1] = mMap.addMarker(new MarkerOptions().position(dest).title("Destination Location").icon(bitmapDescriptorfromVector(getApplicationContext(),R.drawable.ic_041_pin_26_48)));
                        }
                        else{
                            boundry[1] = mMap.addMarker(new MarkerOptions().position(start).title("Starting Location").icon(bitmapDescriptorfromVector(getApplicationContext(),R.drawable.ic_041_pin_26_48)));
                            boundry[2] = mMap.addMarker(new MarkerOptions().position(dest).title("Destination Location").icon(bitmapDescriptorfromVector(getApplicationContext(),R.drawable.ic_041_pin_26_48)));
                        }
//                        mMap.addMarker(new MarkerOptions().position(start).title("Starting Location"));
//                        mMap.addMarker(new MarkerOptions().position(dest).title("Destination Location"));

                        float zoomLevel = 12.0f;

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for(Marker mark : boundry){
                            if (mark != null) {
                                builder.include(mark.getPosition());
                            }
                        }
                        LatLngBounds bounds = builder.build();
                        int padding = 10;
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,padding);
                        mMap.animateCamera(cu);
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr,zoomLevel));
//                        mMap.getMinZoomLevel();
                        double trace_lat,trace_long;
                        double way_lat,way_long;
                        for(DataSnapshot trace_datasnapshot : dataSnapshot1.child("traces").getChildren()){
                                String tracekey = trace_datasnapshot.getKey();
                                trace_lat = (double) dataSnapshot1.child("traces").child(tracekey).child("latitude").getValue();
                                trace_long = (double) dataSnapshot1.child("traces").child(tracekey).child("longitude").getValue();
                                LatLng trace_icon = new LatLng(trace_lat,trace_long);
                                mMap.addMarker(new MarkerOptions().position(trace_icon).title("Trace").icon(bitmapDescriptorfromVector(getApplicationContext(),R.drawable.ic_fiber_manual_record_black_24dp)));
                            }
//                        https://maps.googleapis.com/maps/api/directions/json?origin=sydney,au&destination=perth,au&waypoints=-37.81223%2C144.96254|-34.92788%2C138.60008&key=AIzaSyBxPzyNuKBwMxvINdh55kkAnhveYhMXWbA
                        int flag = 1;
                        for(DataSnapshot waypoints: dataSnapshot1.child("waypoints").getChildren()){
                            String waypoint_key = waypoints.getKey();
                            way_lat = (double) dataSnapshot1.child("waypoints").child(waypoint_key).child("latitude").getValue();
                            way_long = (double) dataSnapshot1.child("waypoints").child(waypoint_key).child("longitude").getValue();
                            waypoints_string += Double.toString(way_lat)+"%2C"+Double.toString(way_long)+"|";

                        }
                        startingLocName = (String) dataSnapshot1.child("sloc_name").getValue();
                        endingLocName = (String) dataSnapshot1.child("dloc_name").getValue();
                        startingDate = (String) dataSnapshot1.child("starting_date").getValue();
                        endingDate = (String) dataSnapshot1.child("ending_date").getValue();
                    }
                    foid_tv.setText((String) foid);
                    duration_tv.setText( (String) startingLocName + " - " + endingLocName);
                    location_tv.setText( (String) startingDate + " - " + endingDate);
                }
                new FetchURL(Maps_show.this).execute(getUrl(),"driving");
            }

            private BitmapDescriptor bitmapDescriptorfromVector(Context applicationContext, int ic_local_shipping_black_24dp) {
                Drawable vector = ContextCompat.getDrawable(applicationContext,ic_local_shipping_black_24dp);
                vector.setBounds(0,0,vector.getIntrinsicWidth(),vector.getIntrinsicHeight());
                Bitmap bitmap = Bitmap.createBitmap(vector.getIntrinsicWidth(),vector.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                vector.draw(canvas);
                return BitmapDescriptorFactory.fromBitmap(bitmap);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        qry.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    double clat_change = 0,clog_change = 0;
                    double dlat_change = 0,dlog_change = 0;
                    String truck_current = "";
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                        String key = dataSnapshot2.getKey();
                        if (key.matches("current_loc")){
                            try {
                                clat_change = (double) dataSnapshot2.child("latitude").getValue();
                                clog_change = (double) dataSnapshot2.child("longitude").getValue();
                                LatLng curr_ch = new LatLng(clat_change,clog_change);
                                if (curr_marker[0] != null){
                                    curr_marker[0].remove();
                                }

                                curr_marker[0] = mMap.addMarker(new MarkerOptions().position(curr_ch).title("Truck Location").icon(bitmapDescriptorfromVector(getApplicationContext(),R.drawable.ic_028_pin_15_48)));
                                Toast.makeText(Maps_show.this,"Truck Location updated!",Toast.LENGTH_LONG).show();
                            }
                            catch (ClassCastException e){
                                if (curr_marker[0] != null){
                                    curr_marker[0].remove();
                                }
                                Toast.makeText(Maps_show.this,"Truck Location not updated yet!",Toast.LENGTH_LONG).show();
                            }

                        }
                        else if(key.matches("dest_loc")){
                            dlat_change = (double) dataSnapshot2.child("latitude").getValue();
                            dlog_change = (double) dataSnapshot2.child("longitude").getValue();
                        }
                        else if(key.matches("truck_id")){
                            truck_current = (String) dataSnapshot2.getValue();
                        }
                        else if(key.matches("traces")){
                            for(DataSnapshot trace_datasnapshot : dataSnapshot2.child("traces").getChildren()){
                                String tracekey = trace_datasnapshot.getKey();
                                dataSnapshot2.child("traces").child(tracekey).child("latitude").getValue();
                                dataSnapshot2.child("traces").child(tracekey).child("longitude").getValue();
                                Log.e("Traces", (String) dataSnapshot2.child("traces").child(tracekey).child("latitude").getValue());
                                Log.e("Traces", (String) dataSnapshot2.child("traces").child(tracekey).child("longitude").getValue());
                            }

                        }

                    }

                    double distance = haversine(clat_change,clog_change,dlat_change,dlog_change);
                    if (distance < 20){
                        Toast.makeText(Maps_show.this,"Got Distance",Toast.LENGTH_LONG).show();
                        Handler handler = new Handler(Looper.getMainLooper());
                        String finalTruck_current = truck_current;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                sendMailUsingSendGrid("novigotracker@novigo.com","badhusha.s@novigo.com","Truck No."+ finalTruck_current +"will arrive shortly","Truck Arrival Update");
                            }
                        });
                    }
                }
            }

            private BitmapDescriptor bitmapDescriptorfromVector(Context applicationContext, int ic_local_shipping_black_24dp) {
                Drawable vector = ContextCompat.getDrawable(applicationContext,ic_local_shipping_black_24dp);
                vector.setBounds(0,0,vector.getIntrinsicWidth(),vector.getIntrinsicHeight());
                Bitmap bitmap = Bitmap.createBitmap(vector.getIntrinsicWidth(),vector.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                vector.draw(canvas);
                return BitmapDescriptorFactory.fromBitmap(bitmap);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getUrl() {
//        String poly_orgin = "orgin="+position1.latitude+","+position1.longitude;
//        String poly_dest= "destination="+position2.latitude+","+position2.longitude;
//        String poly_mode="mode="+directionMode;
//        String poly_params = poly_orgin+"&"+poly_dest+"&"+poly_mode;
//        String poly_output = "json";
        //https://maps.googleapis.com/maps/api/directions/json?origin=12.9880,77.6895&destination=12.9177,77.6238&key=AIzaSyD-2zFWDriYqjqf5vtJa6kqilT6XqYGcQ0
//        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=sydney,au&destination=perth,au&waypoints=-37.81223%2C144.96254|-34.92788%2C138.60008&key=AIzaSyBxPzyNuKBwMxvINdh55kkAnhveYhMXWbA";
          String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+orgin_url+"&destination="+dest_url+"&waypoints="+waypoints_string+"&key=API_KEY";
        return url;
    }
    private double haversine(double lat1,double log1,double lat2,double log2){
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(log2 - log1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);

        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    private void sendMailUsingSendGrid(String from, String to, String body, String heading) {
        Hashtable<String,String> hashtable = new Hashtable<>();
        hashtable.put("to",to);
        hashtable.put("from",from);
        hashtable.put("subject",heading);
        hashtable.put("text",body);

        SendGridAsyncTask email = new SendGridAsyncTask();
        try {
            email.execute(hashtable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
