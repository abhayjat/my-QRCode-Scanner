package com.example.myqrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {

    CodeScanner codeScanner;
    //CodeScanner class use for scan the QR code
    CodeScannerView scannerView;
    TextView resultScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        scannerView=(CodeScannerView) findViewById(R.id.scanner);
        resultScan=(TextView) findViewById(R.id.resultsc);
        codeScanner=new CodeScanner(this,scannerView);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }
}