package com.example.mobilesensorapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mobilesensorapplication.HeartRateSensorFragment.HeartRateLiveDataFragment;
import com.example.mobilesensorapplication.HeartRateSensorFragment.OnSelectedListener;
import com.example.mobilesensorapplication.HeartRateSensorFragment.ServerStartFragment;
import com.example.mobilesensorapplication.Permissions.Permissions;
import com.example.mobilesensorapplication.SocketServer.Server;
import com.example.mobilesensorapplication.SocketServer.ServerMessageListener;
import com.example.mobilesensorapplication.VideoRecording.RecordClass;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSelectedListener, ServerMessageListener {
    FragmentTransaction ft;
    int fragmentCount = 0;
    ServerStartFragment serverStartFragment;
    HeartRateLiveDataFragment heartRateLiveDataFragment;
    Server server;
    private static final int REQUEST_PERMISSIONS = 1; // Permission for all requests of Video record, Audio record and save video
    private Runnable doAfterAllPermissionsGranted;
    public RecordClass recordClass;


    private final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //   checkPermission();
        // startSocketServer();
       recordClassInitializer();

    }

    private void recordClassInitializer(){
        recordClass = new RecordClass(this, this, getWindow().getDecorView());
        // recordClass.onCreate();
    }


    private void checkPermission() {
        Permissions permissions = new Permissions(this);
        permissions.requestAccessPubSpacePermissions();
    }

    public void startSocketServer() {
        serverStartFragment = new ServerStartFragment(this);
        fragmentCount = 1;
        set_Fragment(serverStartFragment); // main fragment
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof ServerStartFragment) {
            fragmentCount = 1;
        } else if (fragment instanceof HeartRateLiveDataFragment) {
            fragmentCount = 2;
        }
    }

    @Override
    public void proceedSelction(String name) {
        serverReady(name);

        // Call new Fragment with all details for Accelerometer
        heartRateLiveDataFragment = new HeartRateLiveDataFragment(name);
        heartRateLiveDataFragment.registerListener( this);
        fragmentCount = 2;
        set_Fragment(heartRateLiveDataFragment); // Accelerometer fragment

    }

    private void serverReady(String name) {
        if (!name.equals("Second")) {
            sendMessage("Server Ready");
        }
    }

    @Override
    public void startServer() {
        server = new Server(this, this);
        server.startServer();
    }

    private void set_Fragment(Fragment fragment) {
        // Begin the transaction
        ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_holder, fragment);
        // Complete the changes added above
        ft.commit();
    }


    @Override
    public void message(String message) {

        if (fragmentCount == 1) {
           /* Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast.show();*/
            serverStartFragment.message(message);
        } else if (fragmentCount == 2) {
            Log.i("LagTest", "message: main activity");
            if (HeartRateLiveDataFragment.saveDataFlag)
                heartRateLiveDataFragment.message(message);
        }
    }

    public void sendMessage(String message) {
        server.sendData(message);
    }


    @Override
    protected void onStop() {
        super.onStop();
        onDestroyFunction();
    }

    private void onDestroyFunction(){
        Log.i("ErrorCheck", "onDestroyFunction: Main Activity");
        if (server != null)
            server.onDestroy();

        recordClass.onDestroy();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Lifecycle", "MainActivity: onResume");

        if (doAfterAllPermissionsGranted != null) {
            doAfterAllPermissionsGranted.run();
            doAfterAllPermissionsGranted = null;
        } else {
            String[] neededPermissions = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };
            List<String> deniedPermissions = new ArrayList<>();
            for (String permission : neededPermissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission);
                }
            }
            if (deniedPermissions.isEmpty()) {
                // All permissions are granted
                doAfterAllPermissionsGranted();
            } else {
                String[] array = new String[deniedPermissions.size()];
                array = deniedPermissions.toArray(array);
                ActivityCompat.requestPermissions(this, array, REQUEST_PERMISSIONS);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            boolean permissionsAllGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    permissionsAllGranted = false;
                    break;
                }
            }
            if (permissionsAllGranted) {
                doAfterAllPermissionsGranted = new Runnable() {
                    @Override
                    public void run() {
                        doAfterAllPermissionsGranted();
                    }
                };
            } else {
                doAfterAllPermissionsGranted = new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), R.string.permissions_denied_exit, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                };
            }
        }
    }

    private void doAfterAllPermissionsGranted() {
        startSocketServer();
    }


}