package com.multimedia.boletin3_carolinamesamartinez;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.security.Policy;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ASK_PERMISSIONS=111;

    boolean tieneflash=false;
    boolean linternaon[]={false};

    ToggleButton toggleButton;
    String []iddecamara={null};
    CameraManager camera;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleButton=(ToggleButton) findViewById(R.id.toggleButton);
        imageView=(ImageView) findViewById(R.id.imageView);
        try {
            mirarpermisos();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    public void mirarpermisos() throws CameraAccessException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            Toast.makeText(this, "versión inadecuada " + Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();

        } else {


            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);//guarda 0 o 1
            // 0 es granted
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {//si el número es diferente al que devuelve el granted
                Toast toast1 = Toast.makeText(this, "solicitando  linterna", Toast.LENGTH_LONG);
                toast1.show();

                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS); //lo pide
                //onrequestpermissionresult
                //aqui ponemos al permiso de cámara ese identificador


            } else {//si devuelve el mismo número es que el permiso ya había sido dado antes y hacemos la acción del flash
                Toast toast2 = Toast.makeText(this, "el permiso ya había sido dado ", Toast.LENGTH_LONG);
                toast2.show();
                abrirflash();

            }

            /*
            tieneflash=getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
            camera = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
            iddecamara[0]= camera.getCameraIdList()[0];
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(tieneflash){
                        if(isChecked){
                            try {
                                camera.setTorchMode(iddecamara[0],true);
                                linternaon[0]=true;
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }

                        }else{
                            try {
                                camera.setTorchMode(iddecamara[0],false);
                                linternaon[0]=false;

                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        */

        }
        return;

    }

    public void abrirflash() throws CameraAccessException {

        tieneflash=getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        camera = (CameraManager)getSystemService(Context.CAMERA_SERVICE);//servicios de la cámara
        iddecamara[0]= camera.getCameraIdList()[0];//ID DE LA CÁMARA
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){//si esta on
                    try {
                        camera.setTorchMode(iddecamara[0],true);
                        linternaon[0]=true;
                        imageView.setImageResource(R.drawable.on);

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                }else{
                    try {
                        camera.setTorchMode(iddecamara[0],false);
                        linternaon[0]=false;
                        imageView.setImageResource(R.drawable.pruebano);

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

    }


    //manejar el resultado de pedirlo
    @Override
    public void onRequestPermissionsResult(int codigo, String[] permisos, int[] grantResults) {
        //para indicar que quieres comtrolar al de la cámara si el código que estamos controland es el de la cámara
        if(REQUEST_CODE_ASK_PERMISSIONS == codigo) {//si el código se refiere al permiso de la cámara que nosotros hemos escrito(en esta caso) como un identificador en el mometo de preguntar coincide
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { //si el resultado guarda un número igual al granted es que han sido concedidos
                Toast toast3= Toast.makeText(this, "permisos concedidos ;O " , Toast.LENGTH_LONG);
                toast3.show();
                try {
                    abrirflash();//y abrimos el flash como siguiente acción
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            } else {// si no

                Toast toast4= Toast.makeText(this, "Permisos no concedidos ! :-( " , Toast.LENGTH_LONG);
                toast4.show();
            }
        }else{ //para otros permisos
            super.onRequestPermissionsResult(codigo, permisos, grantResults);
        }

    }







}