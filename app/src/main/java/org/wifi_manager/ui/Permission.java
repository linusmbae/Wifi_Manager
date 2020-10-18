package org.wifi_manager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.trncic.library.DottedProgressBar;

import org.wifi_manager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Permission extends AppCompatActivity {

    @BindView(R.id.grant) Button grant;
    @BindView(R.id.progress) DottedProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        if (ContextCompat.checkSelfPermission(Permission.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            startActivity(new Intent(Permission.this, MapActivity.class));
            finish();
            return;
        }
        ButterKnife.bind(this);

        permit();
        retry();

    }

    private void permit() {
        try {
            startTimer();
            grant.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            Dexter.withActivity(Permission.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        startActivity(new Intent(Permission.this,MapActivity.class));
                        finish();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        if (permissionDeniedResponse.isPermanentlyDenied()){
                            AlertDialog builder= new AlertDialog.Builder(Permission.this)
                                    .setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is completely denied. Kindly go to settings and allow the permission.")
                                    .setNegativeButton("Cancel",null)
                                    .setPositiveButton("Ok",null)
                                    .show();

                            Button positiveButton=builder.getButton(AlertDialog.BUTTON_POSITIVE);
                            positiveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.fromParts("package",getPackageName(),null));
                                }
                            });
                        }else{
                            new SweetAlertDialog(Permission.this,SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Permission Denied!")
                                    .show();
                            grant.setVisibility(View.VISIBLE);
                            stopProgress();
                            progress.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                        progress.setVisibility(View.INVISIBLE);
                        stopProgress();
                    }
                })
                .check();

        }catch (Exception e){
            new SweetAlertDialog(Permission.this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Error! Please restart the application")
                    .show();
            progress.setVisibility(View.INVISIBLE);
            stopProgress();
        }
    }

    private void retry() {
        grant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                grant.setVisibility(View.INVISIBLE);
                startTimer();
                progress.setVisibility(View.VISIBLE);
                Dexter.withActivity(Permission.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                startActivity(new Intent(Permission.this,MapActivity.class));
                                finish();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                if (permissionDeniedResponse.isPermanentlyDenied()){

                                    AlertDialog builder= new AlertDialog.Builder(Permission.this)
                                            .setTitle("Permission Denied")
                                            .setMessage("Permission to access device location is completely denied. Kindly go to settings and allow the permission.")
                                            .setNegativeButton("Cancel",null)
                                            .setPositiveButton("Ok",null)
                                            .show();

                                    Button positiveButton=builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                    positiveButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package",getPackageName(),null));
                                        }
                                    });
                                }else{
                                    new SweetAlertDialog(Permission.this,SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Permission Denied!")
                                            .show();
                                    grant.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                                progress.setVisibility(View.INVISIBLE);
                                stopProgress();
                            }
                        })
                        .check();
                }catch (Exception e){
                    new SweetAlertDialog(Permission.this,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error! Please restart the application")
                            .show();
                    progress.setVisibility(View.INVISIBLE);
                    stopProgress();
                }
            }
        });
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