package com.example.mobilesensorapplication.Permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mobilesensorapplication.R;

public class Permissions {
    Context context;
    private final int REQUEST_READ_PUBDIR_PERMISSIONS = 1337;


    public Permissions(Context context) {
        this.context = context;
    }


    public void requestAccessPubSpacePermissions() {
        /*Here we will do the remaining actual work*/

        Log.i("test", "MainActivity: Access Public space");
        String readWritePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (!isPermissionGranted(context, readWritePermission)) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{readWritePermission},
                    REQUEST_READ_PUBDIR_PERMISSIONS);
        }
    }


    private boolean isPermissionGranted(Context context, String permissionToCheck) {
        Log.i("test", "Common: Permission Check");
        int permissionCheck = ContextCompat.checkSelfPermission(context, permissionToCheck);
        Boolean checkFlag = permissionCheck == PackageManager.PERMISSION_GRANTED;
        return checkFlag;

    }





}
