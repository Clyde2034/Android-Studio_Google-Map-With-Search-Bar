package com.example.autocomplete;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


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