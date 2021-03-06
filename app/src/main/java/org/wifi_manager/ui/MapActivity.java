package org.wifi_manager.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trncic.library.DottedProgressBar;

import org.wifi_manager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    @BindView(R.id.maps_progress) DottedProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ButterKnife.bind(this);

        startTimer();

        try {
            SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }catch (Exception e){
            new SweetAlertDialog(MapActivity.this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Errors!")
                    .show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            stopProgress();
            progress.setVisibility(View.INVISIBLE);
            map=googleMap;
            LatLng home = new LatLng(-1.2840425907023074, 36.70896668144134);
            map.addMarker(new MarkerOptions().position(home).title("Home"));
            map.moveCamera(CameraUpdateFactory.newLatLng(home));
        }catch (Exception e){
            new SweetAlertDialog(MapActivity.this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Errors")
                    .show();
            stopProgress();
            progress.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.wifi_manager_menu, menu);
        MenuItem menuItem=menu.findItem(R.id.search);
                SearchView searchView=(SearchView)menuItem.getActionView();
                searchView.setQueryHint("Search Here");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Toast.makeText(MapActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        Toast.makeText(MapActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addBoosters) {
            startActivity(new Intent(MapActivity.this, AddBoosters.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startTimer() {
        Runnable run = new Runnable(){

            @Override
            public void run() {
                startProgress();
            }
        };
        Handler han = new Handler();
        han.postAtTime(run, 100);
    }

    public void stopProgress() {
        progress.stopProgress();
    }

    public void startProgress() {
        progress.startProgress();
    }
}