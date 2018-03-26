package com.test.runtimepermissionrequest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    Button checkPermission;
    private static final int PERMISSION_REQUEST_CODE=200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission=(Button)findViewById(R.id.check_permission);

        requestPermissions();

        checkPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_permission())
                {
                    Toast.makeText(MainActivity.this, "All permissions granted", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "Permissions not granted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean check_permission()
    {
        int result= ContextCompat.checkSelfPermission(getApplicationContext(),ACCESS_FINE_LOCATION);
        int result1=ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA);
        int result2=ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);

        return result== PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED && result2==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION,CAMERA,WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0){
                    boolean locationAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    boolean externalWriteAccepted=grantResults[2]==PackageManager.PERMISSION_GRANTED;

                    if(locationAccepted && cameraAccepted && externalWriteAccepted){
                        //Toast.makeText(this, "All permissions are provided by you", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(this, "App needs to access to your location,camera and storage permission", Toast.LENGTH_SHORT).show();
                    }

                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(CAMERA) || shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                            showMessageOKCancel("You need to allow all the permissions or you cannot access the application", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                                        requestPermissions(new String[]{ACCESS_FINE_LOCATION,CAMERA,WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
                                    }
                                }
                            });
                            return;
                        }
                    }
                }
            break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener){
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok",okListener)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }
}
