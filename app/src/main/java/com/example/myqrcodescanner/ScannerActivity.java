package com.example.myqrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
//import com.muddzdev.styleabletoast.StyleableToast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;


public class ScannerActivity extends AppCompatActivity {

    CodeScanner codeScanner;
    //CodeScanner class use for scan the QR code
    CodeScannerView scannerView;
    TextView resultScan;
    ImageButton gallery,backbtn;
    Intent intentToMainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        scannerView=(CodeScannerView) findViewById(R.id.scanner);
        resultScan=(TextView) findViewById(R.id.resultsc);
        codeScanner=new CodeScanner(this,scannerView);
        gallery=(ImageButton) findViewById(R.id.gallery);
        backbtn=(ImageButton) findViewById(R.id.back);
        intentToMainActivity=new Intent(this,MainActivity.class);

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

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToMainActivity=new Intent(v.getContext(),MainActivity.class);
                startActivity(intentToMainActivity);
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

    public void btnBrowser(View view) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        startActivityForResult(pickIntent, 111);
    }

    @Override

    protected void onActivityResult(int reqCode, int resultCode, Intent data) {

        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            try {

                final Uri imageUri = data.getData();

                final InputStream imageStream = getContentResolver().openInputStream(imageUri);

                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                try {

                    Bitmap bMap = selectedImage;

                    String contents = null;



                    int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];

                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);

                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    MultiFormatReader reader = new MultiFormatReader();

                    Result result = reader.decode(bitmap);

                    contents = result.getText();

                    Toast.makeText(getApplicationContext(),contents,Toast.LENGTH_LONG).show();
                    
                }catch (Exception e){

                    e.printStackTrace();

                }
                //  image_view.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
                Toast toast = null;
                Toast.makeText(ScannerActivity.this, "Something went wrong", Toast.LENGTH_LONG);
                toast.show();

            }
        }else {

            Toast.makeText(ScannerActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();

        }

    }

}