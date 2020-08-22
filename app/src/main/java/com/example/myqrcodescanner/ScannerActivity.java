package com.example.myqrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class ScannerActivity extends AppCompatActivity {

    CodeScanner codeScanner;
    //CodeScanner class use for scan the QR code
    CodeScannerView scannerView;
    TextView resultScan;
    ImageButton gallery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        scannerView=(CodeScannerView) findViewById(R.id.scanner);
        resultScan=(TextView) findViewById(R.id.resultsc);
        codeScanner=new CodeScanner(this,scannerView);
        gallery=(ImageButton) findViewById(R.id.gallery);
        codeScanner.setDecodeCallback(new DecodeCallback() {        //Use setDecodeCallback for convert our scan code.
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {                      //Use for Every time new QR.
                    @Override
                    public void run() {
                        resultScan.setText(result.getText());       //It give our result data to resultScan.
                    }
                });
            }
        });


        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });

        scannerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                codeScanner.getAutoFocusMode();
                return false;
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        requestForPermission();
    }

    private void requestForPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(ScannerActivity.this,"Camera Permission is Required",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.cancelPermissionRequest();
            }
        }).check();
    }

//    public void btnBrowser(View view) {
//        Intent pickIntent = new Intent(Intent.ACTION_PICK);
//        pickIntent.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//
//        startActivityForResult(pickIntent, 111);
//    }
}