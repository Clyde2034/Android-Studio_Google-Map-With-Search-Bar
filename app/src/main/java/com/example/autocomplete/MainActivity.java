package com.example.autocomplete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

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