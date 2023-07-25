package com.example.flashlight;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import com.example.flashlight.databinding.ActivityMainBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private CameraManager cameraManager;
    private String camId = "0";
    private boolean torch_status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            camId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                turnOnFlashLight();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(MainActivity.this, "please grant the permission!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void turnOnFlashLight() {
        binding.imgTorch.setOnClickListener(v -> {
            if (torch_status){
                try {
                    cameraManager.setTorchMode(camId,true);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                binding.imgTorch.setImageResource(R.drawable.torch_on);
                torch_status = false;
            }else {
                try {
                    cameraManager.setTorchMode(camId,false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                binding.imgTorch.setImageResource(R.drawable.torch_off);
                torch_status = true;
            }
        });
    }
}