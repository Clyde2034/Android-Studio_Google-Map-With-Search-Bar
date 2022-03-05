# Android-Studio_Google-Map-with-Search-Bar
The Project is implement on Fragment with Google Map

>API Require
>
>>Maps SDK for Android  
>>Places API

>UI and Java Code
>>UI：activity_main.xml、fragment_map.xml
>>Java：MainActivity.java、MapFragment.java


<p align="center">
  <img align="left" src="https://user-images.githubusercontent.com/41913354/156886216-68dca4b4-ff69-4e24-88b3-442605bc1157.png" width="300"/>
  <img align="center" src="https://user-images.githubusercontent.com/41913354/156886292-f168fddb-1746-40ec-a073-f6f563806611.png" width="300"/>
  <img align="right" src="https://user-images.githubusercontent.com/41913354/156886646-b3628b4b-1259-4395-8d61-741d4c3a1169.png" width="300"/>
</p>



## AndroidManifest.xml
```
    //...
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    
    <application>
    //...
    
        <meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />

        <meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="@string/api_key" />
            
    //...
    </application>   
-------------------------------------------------------------------------------------------
dependencies {
    //...
    implementation 'com.google.android.gms:play-services-maps:18.0.2' //mapView
    implementation 'com.google.android.gms:play-services-location:19.0.1' //FusedLocationProviderClient
    implementation 'com.google.android.libraries.places:places:2.5.0' //Autocomplete_fragment
    //...
}  
```

## activity_main.xml
```
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <FrameLayout
        android:id="@+id/frameLayout"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_marginBottom="10dp"
        app:layout_constraintBottom_toTopOf="@id/btn_get"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

    </FrameLayout>


    <Button
        android:id="@+id/btn_get"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="10dp"
        android:text="GET"
        android:textSize="18sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## MainActivity.java
```
public class MainActivity extends AppCompatActivity {

    private Button btn_get;
    private FrameLayout frameLayout;
    private boolean flag = false;
    private String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_get = findViewById(R.id.btn_get);
        frameLayout = findViewById(R.id.frameLayout);


        Initial();
        btn_get.setOnClickListener(PERMISSION);

        if (flag) {
            //Used to Loading the MapFragment
            MapFragment mapFragment = new MapFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mapFragment).commit();
        }
    }

    private void Initial() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission[0]) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission[0]}, 100);
        } else {
            flag = true;
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, permission[1]) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission[1]}, 101);
        } else {
            flag = true;
        }
    }

    private View.OnClickListener PERMISSION = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Used to Check permission of ACCESS_FINE_LOCATION，the requestCode doesn't must be 101 or 100.
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission[0]) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission[0]}, 100);
            } else {
                flag = true;
                Toast.makeText(MainActivity.this, "The ACCESS_FINE_LOCATION Permission already have it.", Toast.LENGTH_SHORT).show();
            }

            //Used to Check permission of ACCESS_COARSE_LOCATION，the requestCode doesn't must be 101 or 100.
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission[1]) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission[1]}, 101);
            } else {
                flag = true;
                Toast.makeText(MainActivity.this, "The ACCESS_COARSE_LOCATION Permission already have it.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //The request result of ACCESS_FINE_LOCATION
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                flag = true;
                MapFragment mapFragment = new MapFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mapFragment).commit();
            }
        }

        //The request result of ACCESS_COARSE_LOCATION
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                flag = true;
                MapFragment mapFragment = new MapFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, mapFragment).commit();
            }
        }
    }
}
```

## fragment_map.xml
```
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MapFragment">
    
    //This Fragment is used to loading Google Map，and the fragment type depend on android:name attributers
    <fragment
        android:id="@+id/fragment_map"
        android:name="com.google.android.gms.maps.SupportMapFragment"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <androidx.cardview.widget.CardView
            android:id="@+id/cardview_search_ori"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginHorizontal="20dp"
            android:layout_marginTop="20dp"
            app:cardCornerRadius="15dp"
            app:cardElevation="10dp">
            
            //This Fragment is used to build a Search View ovelapped on fragment_map，its parent component Cardview just a decorate the layout view
            <fragment
                android:id="@+id/autocomplete_fragment_ori"
                android:name="com.google.android.libraries.places.widget.AutocompleteSupportFragment"
                android:layout_width="match_parent"
                android:layout_height="wrap_content" />

        </androidx.cardview.widget.CardView>

    </fragment>
    
</androidx.constraintlayout.widget.ConstraintLayout>
```

## MapFragment.java
```
public class MapFragment extends Fragment implements OnMapReadyCallback {


    private FusedLocationProviderClient fusedLocationClient;
    private SupportMapFragment supportMapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);


        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        supportMapFragment.getMapAsync(this);

        //Initial the Place API
        String api_key = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), api_key);
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment_ori = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_ori);


        // Specify the types of place data to return.
        autocompleteFragment_ori.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment_ori.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                if (place.getLatLng() != null) {
                    LatLng placeLatLng_ori = place.getLatLng();
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLatLng_ori));
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(placeLatLng_ori.latitude, placeLatLng_ori.longitude)).title(place.getName()));
                        }
                    });
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getActivity(), "Try to Fetch the Original Place, but occures error.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {

        //Initial the Map.
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        final LatLng[] myLocation = new LatLng[1];
        //Get Last Location,and catch the success or failed event.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            myLocation[0] = new LatLng(location.getLatitude(), location.getLongitude());
                            //In thw same time，move camera to the current location.
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation[0], 13));
                        } else {
                            //Taipei 101 LatLng
                            myLocation[0] = new LatLng(25.033964, 121.564468);
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(getActivity(), "Failed to get currnt last location.", Toast.LENGTH_SHORT).show();
                    }
                });

        //When Click the MyLocationButton，it will print put address by Geocoder translate the LatLng.
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                try {
                    //Geocoder used to convert from LatLng to Address
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.TRADITIONAL_CHINESE);
                    //Get the Address
                    List<Address> location = geocoder.getFromLocation(myLocation[0].latitude, myLocation[0].longitude, 1);
                    Toast.makeText(getActivity(), location.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
}
```

