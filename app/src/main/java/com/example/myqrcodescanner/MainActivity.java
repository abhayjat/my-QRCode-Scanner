package com.example.myqrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class MainActivity extends AppCompatActivity {

    EditText qrValue;
    Button  btngenrator,btnscan;
    ImageView qrimgGen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qrValue=(EditText) findViewById(R.id.enter_val);
        btngenrator  =(Button) findViewById(R.id.genratebtn);
        qrimgGen=(ImageView) findViewById(R.id.scaGan);
        btnscan = (Button) findViewById(R.id.scanbtn);

        btngenrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data=qrValue.getText().toString();
                if(data.isEmpty()){
                    qrValue.setError("Enter some value");
                }else {
                    QRGEncoder qrgEncoder=new QRGEncoder(data,null, QRGContents.Type.TEXT,500);
                    Bitmap bitmap;
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrimgGen.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ScannerActivity.class));
            }
        });
    }
}